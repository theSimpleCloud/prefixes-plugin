package app.simplecloud.plugin.prefixes.api.impl

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.TextComponent
import java.util.*

abstract class PrefixesComponentApiImpl() : PrefixesApi<TextComponent> {

    private var groups: MutableList<PrefixesGroup<TextComponent>> = mutableListOf()
    private var actor: PrefixesActor<UUID, TextComponent> = PrefixesActorBlankImpl()

    override fun getGroups(): List<PrefixesGroup<TextComponent>> {
        return groups
    }

    override fun getHighestGroup(uniqueId: UUID): PrefixesGroup<TextComponent> {
        return groups.stream().filter {group ->
            group.containsPlayer(uniqueId)
        }.findFirst().orElse(null)
    }

    override fun addGroup(group: PrefixesGroup<TextComponent>) {
        groups.add(group)
    }

    override fun setActor(actor: PrefixesActor<UUID, TextComponent>) {
        this.actor = actor
    }

    override fun setWholeName(uniqueId: UUID, group: PrefixesGroup<TextComponent>) {
       actor.applyGroup(uniqueId, group)
    }

    override fun setWholeName(uniqueId: UUID, groupName: String) {
        setWholeName(uniqueId, groups.stream().filter { group -> group.getName() == groupName }.findFirst().orElse(null))
    }
}