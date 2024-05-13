package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesApi
import net.luckperms.api.LuckPerms
import net.luckperms.api.event.user.track.UserDemoteEvent
import net.luckperms.api.event.user.track.UserPromoteEvent
import org.bukkit.plugin.Plugin

class LuckPermsListener(
    private val plugin: Plugin,
    private val luckPerms: LuckPerms,
    private val api: PrefixesApi
) {

    fun init() {
        val eventBus = luckPerms.eventBus
        eventBus.subscribe(plugin, UserPromoteEvent::class.java, this::onUserPromote)
        eventBus.subscribe(plugin, UserDemoteEvent::class.java, this::onUserDemote)
    }

    private fun onUserPromote(event: UserPromoteEvent) {
        val group = api.getHighestGroup(event.user.uniqueId)
        api.setWholeName(event.user.uniqueId, group)
    }

    private fun onUserDemote(event: UserDemoteEvent) {
        val group = api.getHighestGroup(event.user.uniqueId)
        api.setWholeName(event.user.uniqueId, group)
    }

}