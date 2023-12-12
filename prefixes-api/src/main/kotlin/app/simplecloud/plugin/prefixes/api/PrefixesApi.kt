package app.simplecloud.plugin.prefixes.api

import net.kyori.adventure.text.Component
import java.util.*

interface PrefixesApi {

    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param group
     */
    fun setWholeName(uniqueId: UUID, group: PrefixesGroup)

    /**
     * Sets the prefix and suffix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param groupName
     */
    fun setWholeName(uniqueId: UUID, groupName: String)

    /**
     * Sets the prefix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param prefix prefix to set
     */
    fun setPrefix(uniqueId: UUID, prefix: Component)

    /**
     * Sets the prefix of a player in both Tab and Chat
     * @param uniqueId UUID of the target player
     * @param suffix suffix to set
     */
    fun setSuffix(uniqueId: UUID, suffix: Component)

    /**
     * Returns all registered [PrefixesGroup] ordered by priority
     */
    fun getGroups(): List<PrefixesGroup>

    /**
     * Returns the highest [PrefixesGroup] of a player
     * @param uniqueId UUID of the target player
     */
    fun getHighestGroup(uniqueId: UUID): PrefixesGroup

    /**
     * Adds a [PrefixesGroup]
     * @param group
     */
    fun addGroup(group: PrefixesGroup)

    /**
     * Changes the [PrefixesActor] of the server instance (e.g. to a bukkit actor)
     * @param actor
     */
    fun setActor(actor: PrefixesActor)

    /**
     * Changes the Scoreboard Team color of the target player (Used in 1.12+ to make player names colorful)
     * @param uniqueId UUID of the target player
     * @param color Color string (ChatColor on spigot, hex colors on other server implementations)
     */
    fun setColor(uniqueId: UUID, color: String)

    /**
     * Sets the used PrefixesConfig
     * @param config Specifies the new [PrefixesConfig]
     */
    fun setConfig(config: PrefixesConfig)

}