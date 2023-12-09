package app.simplecloud.plugin.prefixes.api.impl

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import app.simplecloud.plugin.prefixes.api.PrefixesNameElement

class PrefixesActorBlankImpl<T, E> : PrefixesActor<T, E> {

    override fun applyGroup(target: T, group: PrefixesGroup<E>) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun setPrefix(target: T, prefix: PrefixesNameElement<E>) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun setSuffix(target: T, suffix: PrefixesNameElement<E>) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun remove(target: T) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }
}