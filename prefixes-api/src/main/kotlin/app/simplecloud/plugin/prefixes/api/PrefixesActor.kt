package app.simplecloud.plugin.prefixes.api

interface PrefixesActor<T, E> {
    fun applyGroup(target: T, group: PrefixesGroup<E>)

    fun setPrefix(target: T, prefix: PrefixesNameElement<E>)

    fun setSuffix(target: T, suffix: PrefixesNameElement<E>)

    fun remove(target: T)
}