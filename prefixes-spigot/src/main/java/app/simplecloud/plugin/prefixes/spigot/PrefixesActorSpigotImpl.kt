package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.shared.MiniMessageImpl
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

class PrefixesActorSpigotImpl(private var scoreboard: PrefixesScoreboardSpigotImpl) : PrefixesActor {
    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup
    ) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.update(
            target,
            LegacyComponentSerializerImpl.serialize(group.getPrefix()),
            LegacyComponentSerializerImpl.serialize(group.getSuffix())
        )
        setColor(target, group.getColor())
        scoreboard.apply(target, player.name)
    }

    override fun remove(target: UUID) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.remove(player.name)
    }

    override fun setPrefix(target: UUID, prefix: Component) {
        scoreboard.updatePrefix(target, LegacyComponentSerializerImpl.serialize(prefix))
    }

    override fun setSuffix(target: UUID, suffix: Component) {
        scoreboard.updatePrefix(target, LegacyComponentSerializerImpl.serialize(suffix))
    }

    override fun formatMessage(target: UUID, format: String, message: Component): Component {
        val team: Team? = scoreboard.getTeam(target)
        val tags = mutableListOf<TagResolver>()
        if (team != null) {
            tags.add(Placeholder.component("prefix", LegacyComponentSerializerImpl.deserialize(team.prefix)))
            tags.add(Placeholder.component("suffix", LegacyComponentSerializerImpl.deserialize(team.suffix)))
            tags.add(
                Placeholder.component(
                    "name_colored",
                    LegacyComponentSerializerImpl.deserialize(team.color.toString() + Bukkit.getPlayer(target)!!.name)
                )
            )
            tags.add(Placeholder.component("name", Component.text(Bukkit.getPlayer(target)!!.name)))
        } else {
            tags.add(Placeholder.unparsed("name", "%s"))
        }
        tags.add(Placeholder.component("message", message))
        return MiniMessageImpl.parse(format, tags)
    }

    override fun setColor(target: UUID, color: String) {
        scoreboard.updateColor(target, ChatColor.valueOf(color))
    }
}