package app.simplecloud.plugin.prefixes.api.impl

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesConfig
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.Component
import java.util.*

abstract class PrefixesApiImpl : PrefixesApi {

    private val groups: MutableList<PrefixesGroup> = mutableListOf()
    private var actor: PrefixesActor = PrefixesActorBlankImpl()
    private lateinit var config: PrefixesConfig

    override fun registerViewer(uniqueId: UUID) {
        actor.registerViewer(uniqueId, this)
    }
    override fun getGroups(): MutableList<PrefixesGroup> {
        return groups
    }

    override fun getHighestGroup(uniqueId: UUID): PrefixesGroup {
        return groups.stream().filter { group ->
            group.containsPlayer(uniqueId)
        }.findFirst().orElse(null)
    }

    override fun addGroup(group: PrefixesGroup) {
        groups.add(group)
    }

    override fun setActor(actor: PrefixesActor) {
        this.actor = actor
    }

    override fun setWholeName(uniqueId: UUID, group: PrefixesGroup, vararg viewers: UUID) {
        actor.applyGroup(uniqueId, group, *viewers)
    }

    override fun setWholeName(uniqueId: UUID, groupName: String, vararg viewers: UUID) {
        setWholeName(
            uniqueId,
            groups.stream().filter { group -> group.getName() == groupName }.findFirst().orElse(null),
            *viewers
        )
    }

    override fun setPrefix(uniqueId: UUID, prefix: Component, vararg viewers: UUID) {
        actor.setPrefix(uniqueId, prefix, *viewers)
    }

    override fun setSuffix(uniqueId: UUID, suffix: Component, vararg viewers: UUID) {
        actor.setSuffix(uniqueId, suffix, *viewers)
    }

    override fun setColor(uniqueId: UUID, color: String, vararg viewers: UUID) {
        actor.setColor(uniqueId, color, *viewers)
    }

    override fun setConfig(config: PrefixesConfig) {
        this.config = config
    }

    fun getConfig(): PrefixesConfig {
        return config
    }

    override fun formatChatMessage(target: UUID, viewer: UUID, format: String, message: Component): Component {
        return actor.formatMessage(target, viewer, format, message)
    }

    abstract fun indexGroups()

}