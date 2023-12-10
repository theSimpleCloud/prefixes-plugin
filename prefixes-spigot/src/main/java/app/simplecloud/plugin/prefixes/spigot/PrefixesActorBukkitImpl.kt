package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class PrefixesActorBukkitImpl(private var scoreboard: PrefixesScoreboardBukkitImpl) : PrefixesActor {
    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup
    ) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.update(target, LegacyComponentSerializer.legacySection().serialize(group.getPrefix()), LegacyComponentSerializer.legacySection().serialize(group.getSuffix()))
        setColor(target, group.getColor())
        scoreboard.apply(target, player.name)
    }

    override fun remove(target: UUID) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.remove(player.name)
    }

    override fun setPrefix(target: UUID, prefix: Component) {
        scoreboard.updatePrefix(target, LegacyComponentSerializer.legacySection().serialize(prefix))
    }

    override fun setSuffix(target: UUID, suffix: Component) {
        scoreboard.updatePrefix(target, LegacyComponentSerializer.legacySection().serialize(suffix))
    }

    fun setColor(target: UUID, color: String) {
        scoreboard.updateColor(target, ChatColor.valueOf(color))
    }
}