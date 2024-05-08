package app.simplecloud.plugin.prefixes.spigot

import com.comphenix.protocol.wrappers.WrappedChatComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.MinecraftVersion

class LegacyComponentSerializerImpl {
    companion object {
        private val impl =
            GsonComponentSerializer.builder().build()

        fun deserialize(text: String): Component {
            return impl.deserialize(text)
        }

        fun serialize(component: Component): String {
            return impl.serialize(component)
        }

        fun serializeToPacket(component: Component): WrappedChatComponent {
            return WrappedChatComponent.fromJson(serialize(component))
        }
    }
}