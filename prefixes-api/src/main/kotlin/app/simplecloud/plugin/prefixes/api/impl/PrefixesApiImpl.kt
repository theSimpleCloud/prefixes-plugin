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

    override fun setWholeName(uniqueId: UUID, group: PrefixesGroup) {
        actor.applyGroup(uniqueId, group)
    }

    override fun setWholeName(uniqueId: UUID, groupName: String) {
        setWholeName(
            uniqueId,
            groups.stream().filter { group -> group.getName() == groupName }.findFirst().orElse(null)
        )
    }

    override fun setPrefix(uniqueId: UUID, prefix: Component) {
        actor.setPrefix(uniqueId, prefix)
    }

    override fun setSuffix(uniqueId: UUID, suffix: Component) {
        actor.setSuffix(uniqueId, suffix)
    }

    override fun setColor(uniqueId: UUID, color: String) {
        actor.setColor(uniqueId, color)
    }

    override fun setConfig(config: PrefixesConfig) {
        this.config = config
    }

    fun getConfig(): PrefixesConfig {
        return config
    }

    fun formatChatMessage(target: UUID, format: String, message: Component): Component {
        return actor.formatMessage(target, format, message)
    }

    abstract fun indexGroups()

}