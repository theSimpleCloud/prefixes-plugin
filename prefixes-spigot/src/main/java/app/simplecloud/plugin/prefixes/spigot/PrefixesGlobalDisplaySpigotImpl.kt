package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesGlobalDisplay
import app.simplecloud.plugin.prefixes.spigot.packet.PacketTeam
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class PrefixesGlobalDisplaySpigotImpl : PrefixesGlobalDisplay<Component, Player, PacketTeam>() {
}