package app.simplecloud.plugin.prefixes.minestom

import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import java.util.*
import java.util.concurrent.CompletableFuture

class PrefixesGroupMinestomImpl(private val name: String, private val prefix: Component, private val suffix: Component, private val color: String, private val permission: String?) : PrefixesGroup {
    override fun getName(): String {
        return name
    }

    override fun getPrefix(): Component {
        return prefix
    }

    override fun getColor(): String {
        return color
    }

    override fun getSuffix(): Component {
        return suffix
    }

    override fun getPriority(): Int {
        return 0
    }

    override fun containsPlayer(uniqueId: UUID): Boolean {
        return permission == null || MinecraftServer.getConnectionManager().getPlayer(uniqueId)!!.hasPermission(permission)
    }

    override fun containsPlayerFuture(uniqueId: UUID): CompletableFuture<Boolean> {
        return CompletableFuture<Boolean>().completeAsync {
            return@completeAsync containsPlayer(uniqueId)
        }
    }
}