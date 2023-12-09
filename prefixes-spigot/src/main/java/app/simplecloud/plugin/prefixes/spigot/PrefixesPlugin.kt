package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.shared.PrefixesActorBukkitImpl
import app.simplecloud.plugin.prefixes.shared.PrefixesScoreboardBukkitImpl
import net.kyori.adventure.text.TextComponent
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class PrefixesPlugin : JavaPlugin(), Listener {

    private lateinit var prefixesApi: PrefixesComponentApiSpigotImpl
    private var scoreboard: PrefixesScoreboardBukkitImpl = PrefixesScoreboardBukkitImpl()
    private var prefixesApiActor: PrefixesActorBukkitImpl = PrefixesActorBukkitImpl(scoreboard)

    override fun onEnable() {
        val luckPermsProvider: RegisteredServiceProvider<LuckPerms> = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) ?: return
        val luckPerms: LuckPerms = luckPermsProvider.provider
        prefixesApi = PrefixesComponentApiSpigotImpl(luckPerms)
        prefixesApi.setActor(prefixesApiActor)
        prefixesApi.indexGroups()
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getServicesManager().register(prefixesApi.javaClass, prefixesApi, this, ServicePriority.High)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent)
    {
        scoreboard.initScoreboard(Bukkit.getScoreboardManager()!!.mainScoreboard)
        val uniqueId: UUID = event.player.uniqueId
        val playerGroup: PrefixesGroup<TextComponent> = prefixesApi.getHighestGroup(uniqueId)
        prefixesApi.setWholeName(uniqueId, playerGroup)
    }


}