package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.shared.MiniMessageImpl
import app.simplecloud.plugin.prefixes.spigot.packet.PacketTeam
import com.comphenix.protocol.ProtocolManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

class PrefixesActorSpigotImpl(
    private var manager: ProtocolManager,
    private var scoreboard: PrefixesGlobalDisplaySpigotImpl
) : PrefixesActor {
    override fun registerViewer(target: UUID, api: PrefixesApi) {
        val display = PrefixesDisplaySpigotImpl(manager)
        scoreboard.register(target, display)
        Bukkit.getOnlinePlayers().forEach {  player ->
            if(player.uniqueId != target) {
                val group = api.getHighestGroup(player.uniqueId)
                applyGroup(player.uniqueId, group, target)
            }
        }
    }

    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup,
        vararg viewers: UUID
    ) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.update(
            player.name,
            group.getPrefix(),
            group.getSuffix(),
            group.getPriority(),
            *viewers
        )
        if(group.getColor() != null)
            setColor(target, group.getColor()!!, *viewers)
        scoreboard.addPlayer(player.name, player, *viewers)
    }

    override fun remove(target: UUID) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.removePlayer(player)
    }

    override fun setPrefix(target: UUID, prefix: Component, vararg viewers: UUID) {
        val player = Bukkit.getPlayer(target) ?: return
        scoreboard.updatePrefix(player.name, prefix, *viewers)
    }

    override fun setSuffix(target: UUID, suffix: Component, vararg viewers: UUID) {
        val player = Bukkit.getPlayer(target) ?: return
        scoreboard.updatePrefix(player.name, suffix, *viewers)
    }

    override fun formatMessage(target: UUID, viewer: UUID, format: String, message: Component): Component {
        val display = scoreboard.getDisplay(viewer).orElseGet { scoreboard.getDefaultDisplay() }
        val targetPlayer = Bukkit.getPlayer(target) ?: return message
        val team: PacketTeam? = display.getTeam(targetPlayer.name)
        val tags = mutableListOf<TagResolver>()
        if (team != null) {
            tags.add(Placeholder.component("prefix", team.prefix ?: Component.text("")))
            tags.add(Placeholder.component("suffix", team.suffix ?: Component.text("")))
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

    override fun setColor(target: UUID, color: String, vararg viewers: UUID) {
        val player = Bukkit.getPlayer(target) ?: return
        scoreboard.updateColor(player.name, NamedTextColor.nearestTo(TextColor.fromHexString(color) ?: NamedTextColor.GRAY), *viewers)
    }
}