package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.impl.PrefixesComponentApiImpl
import app.simplecloud.plugin.prefixes.shared.LuckPermsGroup
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit

class PrefixesComponentApiSpigotImpl(private var luckPerms: LuckPerms) : PrefixesComponentApiImpl() {
    override fun indexGroups() {
        luckPerms.groupManager.loadAllGroups().newIncompleteFuture<Unit>().completeAsync {
            luckPerms.groupManager.loadedGroups.forEach {
                addGroup(LuckPermsGroup(it, luckPerms))
            }
        }
    }
}