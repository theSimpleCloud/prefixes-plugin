package app.simplecloud.plugin.prefixes.shared

import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.Component
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.group.Group
import java.util.*
import java.util.concurrent.CompletableFuture

class LuckPermsGroup(private var group: Group, private var luckPerms: LuckPerms) : PrefixesGroup {

    override fun getName(): String {
        return group.name;
    }

    override fun getPrefix(): Component {
        return MiniMessageImpl.parse((group.cachedData.metaData.prefix ?: ""))
    }

    override fun getColor(): String? {
        return group.cachedData.metaData.getMetaValue("color")
    }

    override fun getSuffix(): Component {
        return MiniMessageImpl.parse((group.cachedData.metaData.suffix ?: ""))
    }

    override fun getPriority(): Int {
        return group.weight.orElse(0)
    }

    override fun containsPlayer(uniqueId: UUID): Boolean {
        return containsPlayerFuture(uniqueId).join()
    }

    override fun containsPlayerFuture(uniqueId: UUID): CompletableFuture<Boolean> {
        return luckPerms.userManager.loadUser(uniqueId).thenApplyAsync { user ->
            return@thenApplyAsync user.primaryGroup == getName()
        }
    }
}