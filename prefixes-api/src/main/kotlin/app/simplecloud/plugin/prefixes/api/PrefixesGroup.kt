package app.simplecloud.plugin.prefixes.api

import java.util.*
import java.util.concurrent.CompletableFuture

interface PrefixesGroup<T> {
    fun getName(): String
    fun getPrefix(): PrefixesNameElement<T>
    fun getColor(): PrefixesNameElement<T>
    fun getSuffix(): PrefixesNameElement<T>
    fun getPriority(): Int
    fun containsPlayer(uniqueId: UUID): Boolean

    fun containsPlayerFuture(uniqueId: UUID): CompletableFuture<Boolean>
}