package app.simplecloud.plugin.prefixes.api

import net.kyori.adventure.text.format.NamedTextColor
import java.util.Optional
import java.util.UUID

open class PrefixesGlobalDisplay<C, P, T> {


    private val displays = mutableMapOf<UUID, PrefixesDisplay<C, P, T>>()


    private var defaultDisplay: PrefixesDisplay<C, P, T>? = null

    private fun executeFor(players: List<UUID>, action: (display: PrefixesDisplay<C, P, T>) -> Unit) {
        displays.filter { players.isEmpty() || players.contains(it.key) }.forEach { display ->
            action(display.value)
        }
    }

    fun getDisplay(player: UUID): Optional<PrefixesDisplay<C, P, T>> {
        return Optional.ofNullable(displays.getOrDefault(player, null))
    }

    fun getDefaultDisplay(): PrefixesDisplay<C, P, T>? {
        return defaultDisplay
    }

    fun setDefaultDisplay(display: PrefixesDisplay<C, P, T>) {
        this.defaultDisplay = display
    }

    fun register(uuid: UUID, display: PrefixesDisplay<C, P, T>) {
        displays[uuid] = display
    }

    fun createTeam(id: String) {
        executeFor(displays.keys.toList()) {
            it.createTeam(id)
        }
    }
    fun updatePrefix(id: String, prefix: C, vararg players: UUID) {
        executeFor(players.toList()) {
            it.updatePrefix(id, prefix)
        }
    }
    fun updateSuffix(id: String, suffix: C, vararg players: UUID) {
        executeFor(players.toList()) {
            it.updateSuffix(id, suffix)
        }
    }
    fun updatePriority(id: String, priority: Int, vararg players: UUID) {
        executeFor(players.toList()) {
            it.updatePriority(id, priority)
        }
    }
    fun update(id: String, prefix: C, suffix: C, priority: Int, vararg players: UUID) {
        executeFor(players.toList()) {
            println("updating")
            it.update(id, prefix, suffix, priority)
        }
    }
    fun addPlayer(id: String, player: P, vararg players: UUID) {
        executeFor(players.toList()) {
            println("adding player")
            it.addPlayer(id, player)
        }
    }
    fun removePlayer(player: P, vararg players: UUID) {
        executeFor(players.toList()) {
            it.removePlayer(player)
        }
    }

    fun updateColor(id: String, color: NamedTextColor, vararg players: UUID) {
        executeFor(players.toList()) {
            it.updateColor(id, color)
        }
    }

}