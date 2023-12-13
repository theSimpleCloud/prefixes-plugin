package app.simplecloud.plugin.prefixes.paper

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.shared.MiniMessageImpl
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

class PrefixesActorPaperImpl(private var scoreboard: PrefixesScoreboardPaperImpl) : PrefixesActor {
    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup
    ) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.update(
            target,
            group.getPrefix(), group.getSuffix()
        )
        setColor(target, group.getColor())
        scoreboard.apply(target, player.name)
    }

    override fun remove(target: UUID) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.remove(player.name)
    }

    override fun setPrefix(target: UUID, prefix: Component) {
        scoreboard.updatePrefix(target, prefix)
    }

    override fun setSuffix(target: UUID, suffix: Component) {
        scoreboard.updatePrefix(target, suffix)
    }

    override fun formatMessage(target: UUID, format: String, message: Component): Component {
        val team: Team? = scoreboard.getTeam(target)
        val tags = mutableListOf<TagResolver>()
        if (team != null) {
            tags.add(Placeholder.component("prefix", team.prefix()))
            tags.add(Placeholder.component("suffix", team.suffix()))
            tags.add(
                Placeholder.component(
                    "name_colored",
                    Component.text(Bukkit.getPlayer(target)!!.name).color(team.color())
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
        scoreboard.updateColor(target, NamedTextColor.nearestTo(TextColor.fromHexString(color)!!))
    }
}