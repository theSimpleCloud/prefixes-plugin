package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.spigot.loader.SpigotPrefixesChatLoader
import app.simplecloud.plugin.prefixes.spigot.loader.SpigotPrefixesLoader
import com.comphenix.protocol.ProtocolLibrary
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class PrefixesPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        val loader = SpigotPrefixesLoader(ProtocolLibrary.getProtocolManager(), this, SpigotPrefixesChatLoader(this))
        if (loader.load() == null) {
            throw NullPointerException("The Prefixes Plugin could not load correctly")
        }
    }
}