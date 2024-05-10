package app.simplecloud.plugin.prefixes.shared

import com.comphenix.protocol.wrappers.WrappedChatComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class ComponentSerializerImpl {
    companion object {
        private val impl =
            GsonComponentSerializer.builder().build()
        private val legacyImpl =
            LegacyComponentSerializer.builder().hexColors().character('§').useUnusualXRepeatedCharacterHexFormat()
                .hexCharacter('x').build()

        private fun serialize(component: Component): String {
            return impl.serialize(component)
        }

        fun serializeLegacy(component: Component): String {
            return legacyImpl.serialize(component)
        }

        fun deserializeLegacy(text: String): Component {
            return legacyImpl.deserialize(text)
        }

        fun serializeToPacket(component: Component): WrappedChatComponent {
            return WrappedChatComponent.fromJson(serialize(component))
        }
    }
}