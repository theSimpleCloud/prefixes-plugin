package app.simplecloud.plugin.prefixes.spigot.loader

import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesChatLoader
import app.simplecloud.plugin.prefixes.api.PrefixesPluginLoader
import app.simplecloud.plugin.prefixes.api.impl.PrefixesApiImpl
import app.simplecloud.plugin.prefixes.api.impl.PrefixesConfigImpl
import app.simplecloud.plugin.prefixes.shared.PrefixesApiLuckPermsImpl
import app.simplecloud.plugin.prefixes.shared.PrefixesConfigParser
import app.simplecloud.plugin.prefixes.spigot.LuckPermsListener
import app.simplecloud.plugin.prefixes.spigot.PrefixesActorSpigotImpl
import app.simplecloud.plugin.prefixes.spigot.PrefixesGlobalDisplaySpigotImpl
import app.simplecloud.plugin.prefixes.spigot.PrefixesPlugin
import app.simplecloud.plugin.prefixes.spigot.event.PrefixesConfigureEvent
import app.simplecloud.plugin.prefixes.spigot.event.PrefixesConfiguredEvent
import app.simplecloud.plugin.prefixes.spigot.packet.PlayerCreatePacketAdapter
import com.comphenix.protocol.ProtocolManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicePriority
import java.io.File

class SpigotPrefixesLoader(
    private val manager: ProtocolManager,
    private val plugin: Plugin,
    private val chatLoader: PrefixesChatLoader
) : PrefixesPluginLoader, Listener {

    private lateinit var api: PrefixesApiImpl
    override fun load(): PrefixesApiImpl? {
        val prefixesApiActor = PrefixesActorSpigotImpl(manager, PrefixesGlobalDisplaySpigotImpl())
        val luckPermsProvider: RegisteredServiceProvider<LuckPerms> =
            Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) ?: return null
        val luckPerms: LuckPerms = luckPermsProvider.provider
        api = PrefixesApiLuckPermsImpl(luckPerms)
        api.setActor(prefixesApiActor)
        api.setConfig(
            PrefixesConfigParser<PrefixesConfigImpl>(File(plugin.dataFolder, "config.json")).parse(
                PrefixesConfigImpl::class.java,
                PrefixesConfigImpl()
            )
        )
        plugin.saveResource("config.json", false)
        api.indexGroups()
        Bukkit.getPluginManager().registerEvents(this, plugin)
        Bukkit.getServicesManager().register(PrefixesApi::class.java, api, plugin, ServicePriority.Normal)
        manager.addPacketListener(PlayerCreatePacketAdapter(plugin, api))
        chatLoader.load(api)
        LuckPermsListener(plugin, luckPerms, api).init()
        return api
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable {
            if(!api.hasViewer(event.player.uniqueId)) {
                api.registerViewer(event.player.uniqueId)
                applyFirstName(api, plugin, event.player)
            }
        }, 10L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        api.removeViewer(event.player.uniqueId)
    }

    companion object {
        fun applyFirstName(api: PrefixesApi, plugin: Plugin, player: Player) {
            val group = api.getHighestGroup(player.uniqueId)
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val prefixData = PrefixesConfigureEvent(player, group.getPrefix(), group.getSuffix(), group.getColor(), group.getPriority())
                Bukkit.getPluginManager().callEvent(prefixData)
                api.setWholeName(player.uniqueId, prefixData.prefix ?: Component.text(""), prefixData.color ?: NamedTextColor.WHITE, prefixData.suffix ?: Component.text(""), prefixData.priority)
                Bukkit.getPluginManager().callEvent(PrefixesConfiguredEvent(player))
            })
        }
    }
}