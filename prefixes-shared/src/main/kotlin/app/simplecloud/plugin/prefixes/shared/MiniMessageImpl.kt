package app.simplecloud.plugin.prefixes.shared

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class MiniMessageImpl {
    companion object {
        private val miniMessage = MiniMessage.miniMessage()
        fun parse(text: String) : Component
        {
            return miniMessage.deserialize(text)
        }
    }
}