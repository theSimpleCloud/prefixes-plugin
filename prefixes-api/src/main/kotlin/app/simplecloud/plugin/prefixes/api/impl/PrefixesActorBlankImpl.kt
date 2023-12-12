package app.simplecloud.plugin.prefixes.api.impl

import app.simplecloud.plugin.prefixes.api.PrefixesActor
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import net.kyori.adventure.text.Component
import java.util.UUID

class PrefixesActorBlankImpl : PrefixesActor {

    override fun applyGroup(target: UUID, group: PrefixesGroup) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun setPrefix(target: UUID, prefix: Component) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun setSuffix(target: UUID, suffix: Component) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun setColor(target: UUID, color: String) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun formatMessage(target: UUID, format: String, message: Component): Component {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }

    override fun remove(target: UUID) {
        throw NotImplementedError("You need to define a PrefixesActor to use this")
    }
}