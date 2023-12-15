package app.simplecloud.plugin.prefixes.api

import java.util.*

interface PrefixesScoreboard<T, P> {

    fun createTeam(uniqueId: UUID)

    fun updatePrefix(uniqueId: UUID, prefix: T)

    fun updateSuffix(uniqueId: UUID, suffix: T)

    fun update(uniqueId: UUID, prefix: T, suffix: T)

    fun apply(uniqueId: UUID, player: P)

    fun remove(player: P)

}