package app.simplecloud.plugin.prefixes.spigot.loader

import app.simplecloud.plugin.prefixes.api.PrefixesChatLoader
import app.simplecloud.plugin.prefixes.api.impl.PrefixesApiImpl
import app.simplecloud.plugin.prefixes.shared.MiniMessageImpl
import app.simplecloud.plugin.prefixes.shared.ComponentSerializerImpl
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.Plugin

class SpigotPrefixesChatLoader(
  private val plugin: Plugin
): Listener, PrefixesChatLoader {

    private lateinit var api: PrefixesApiImpl
    override fun load(api: PrefixesApiImpl) {
        this.api = api
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        event.format = ComponentSerializerImpl.serializeLegacy(
            api.formatChatMessage(
                event.player.uniqueId,
                event.player.uniqueId,
                api.getConfig().getChatFormat(),
                MiniMessageImpl.parse(event.message)
            )
        )
    }
}