package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.impl.PrefixesApiImpl
import app.simplecloud.plugin.prefixes.shared.LuckPermsGroup
import net.luckperms.api.LuckPerms

class PrefixesApiSpigotImpl(private var luckPerms: LuckPerms) : PrefixesApiImpl() {
    override fun indexGroups() {
        getGroups().clear()
        luckPerms.groupManager.loadAllGroups().newIncompleteFuture<Unit>().completeAsync {
            luckPerms.groupManager.loadedGroups.forEach {
                addGroup(LuckPermsGroup(it, luckPerms))
            }
        }
    }
}