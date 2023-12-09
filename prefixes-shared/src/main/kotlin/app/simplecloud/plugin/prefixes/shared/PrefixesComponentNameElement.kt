package app.simplecloud.plugin.prefixes.shared

import app.simplecloud.plugin.prefixes.api.PrefixesNameElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

class PrefixesComponentNameElement(private var component: TextComponent) : PrefixesNameElement<TextComponent> {
    constructor(component: String) : this(Component.text(component))
    constructor() : this(Component.text(""))
    override fun fallback(): String {
        return component.content()
    }
    override fun get(): TextComponent {
        return component
    }
}