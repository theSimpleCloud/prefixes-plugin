package app.simplecloud.plugin.prefixes.api

interface PrefixesNameElement<T> {
    fun fallback() : String
    fun get() : T

}