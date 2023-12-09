package app.simplecloud.plugin.prefixes.api

import java.util.*

interface PrefixesApi<T> {

    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param group
     */
    fun setWholeName(uniqueId: UUID, group: PrefixesGroup<T>)

    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param groupName
     */
    fun setWholeName(uniqueId: UUID, groupName: String)

    /**
     * Returns all registered [PrefixesGroup] ordered by priority
     */
    fun getGroups(): List<PrefixesGroup<T>>

    /**
     * Returns the highest [PrefixesGroup] of a player
     * @param uniqueId UUID of the target player
     */
    fun getHighestGroup(uniqueId: UUID): PrefixesGroup<T>

    /**
     * Adds a [PrefixesGroup]
     * @param group
     */
    fun addGroup(group: PrefixesGroup<T>)

    /**
     * Indexes all [PrefixesGroup]s
     */
    fun indexGroups()

    /**
     * Changes the [PrefixesActor] of the server instance (e.g. to a bukkit actor)
     * @param actor
     */
    fun setActor(actor: PrefixesActor<UUID, T>);

}