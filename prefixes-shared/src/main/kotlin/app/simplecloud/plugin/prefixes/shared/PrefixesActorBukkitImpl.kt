package app.simplecloud.plugin.prefixes.shared

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.api.PrefixesNameElement
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class PrefixesActorBukkitImpl(private var scoreboard: PrefixesScoreboardBukkitImpl) : PrefixesActor<UUID, TextComponent> {
    override fun applyGroup(
        target: UUID,
        group: PrefixesGroup<TextComponent>
    ) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.update(target, group.getPrefix().fallback(), group.getSuffix().fallback())
        setColor(target, group.getColor())
        scoreboard.apply(target, player.name)
    }

    override fun remove(target: UUID) {
        val player: Player = Bukkit.getPlayer(target) ?: return
        scoreboard.remove(player.name)
    }

    override fun setPrefix(target: UUID, prefix: PrefixesNameElement<TextComponent>) {
        scoreboard.updatePrefix(target, prefix.fallback())
    }

    override fun setSuffix(target: UUID, suffix: PrefixesNameElement<TextComponent>) {
        scoreboard.updatePrefix(target, suffix.fallback())
    }

    fun setColor(target: UUID, color: PrefixesNameElement<TextComponent>) {
        scoreboard.updateColor(target, ChatColor.valueOf(color.fallback()))
    }
}