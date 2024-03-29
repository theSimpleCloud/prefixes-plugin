package app.simplecloud.plugin.prefixes.spigot

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class LegacyComponentSerializerImpl {
    companion object {
        private val impl =
            LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().character('§')
                .hexCharacter('x').build()

        fun deserialize(text: String): Component {
            return impl.deserialize(text)
        }

        fun serialize(component: Component): String {
            return impl.serialize(component)
        }
    }
}