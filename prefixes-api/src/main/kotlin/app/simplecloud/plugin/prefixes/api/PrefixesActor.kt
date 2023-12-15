package app.simplecloud.plugin.prefixes.api

import net.kyori.adventure.text.Component
import java.util.*

interface PrefixesActor {
    fun applyGroup(target: UUID, group: PrefixesGroup)

    fun setPrefix(target: UUID, prefix: Component)

    fun setSuffix(target: UUID, suffix: Component)

    fun setColor(target: UUID, color: String)

    fun formatMessage(target: UUID, format: String, message: Component): Component

    fun remove(target: UUID)
}