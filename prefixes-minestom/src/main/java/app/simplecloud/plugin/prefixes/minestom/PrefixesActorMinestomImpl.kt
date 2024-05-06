package app.simplecloud.plugin.prefixes.minestom

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.shared.MiniMessageImpl
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import java.util.*

class PrefixesActorMinestomImpl(private var scoreboard: PrefixesGlobalDisplayMinestomImpl) : PrefixesActor {
    override fun registerViewer(target: UUID, api: PrefixesApi) {
        scoreboard.register(target, PrefixesTablist())
    }

    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup,
        vararg viewers: UUID,
    ) {
        val player: Player = MinecraftServer.getConnectionManager().getPlayer(target) ?: return
        scoreboard.update(
            player.username,
            group.getPrefix(), group.getSuffix(), group.getPriority(),
            *viewers
        )
        if(group.getColor() != null)
            setColor(target, group.getColor()!!)
        scoreboard.addPlayer(player.username, player)
    }

    override fun remove(target: UUID) {
        val player: Player = MinecraftServer.getConnectionManager().getPlayer(target) ?: return
        scoreboard.removePlayer(player)
    }

    override fun setPrefix(target: UUID, prefix: Component, vararg viewers: UUID) {
        val player: Player = MinecraftServer.getConnectionManager().getPlayer(target) ?: return
        scoreboard.updatePrefix(player.username, prefix)
    }

    override fun setSuffix(target: UUID, suffix: Component, vararg viewers: UUID) {
        val player: Player = MinecraftServer.getConnectionManager().getPlayer(target) ?: return
        scoreboard.updateSuffix(player.username, suffix)
    }

    override fun formatMessage(target: UUID, viewer: UUID, format: String, message: Component): Component {
        val targetPlayer = MinecraftServer.getConnectionManager().getPlayer(target) ?: return message
        val display = scoreboard.getDisplay(viewer).orElse(null) ?: return message
        val team = display.getTeam(targetPlayer.username)
        val tags = mutableListOf<TagResolver>()
        if (team != null) {
            tags.add(Placeholder.component("prefix", team.prefix))
            tags.add(Placeholder.component("suffix", team.suffix))
            tags.add(
                Placeholder.component(
                    "name_colored",
                    Component.text(MinecraftServer.getConnectionManager().getPlayer(target)!!.username)
                        .color(team.teamColor)
                )
            )
            tags.add(
                Placeholder.component(
                    "name",
                    Component.text(MinecraftServer.getConnectionManager().getPlayer(target)!!.username)
                )
            )
        } else {
            tags.add(Placeholder.unparsed("name", "%s"))
        }
        tags.add(Placeholder.component("message", message))
        return MiniMessageImpl.parse(format, tags)
    }

    override fun setColor(target: UUID, color: String, vararg viewers: UUID) {
        val player: Player = MinecraftServer.getConnectionManager().getPlayer(target) ?: return
        scoreboard.updateColor(player.username, NamedTextColor.nearestTo(TextColor.fromHexString(color)!!), *viewers)
    }
}