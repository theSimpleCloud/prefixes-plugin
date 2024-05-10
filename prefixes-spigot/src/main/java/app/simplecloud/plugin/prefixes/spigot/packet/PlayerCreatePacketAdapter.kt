package app.simplecloud.plugin.prefixes.spigot.packet

import app.simplecloud.plugin.prefixes.api.PrefixesApi
import app.simplecloud.plugin.prefixes.api.PrefixesGroup
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.plugin.Plugin

class PlayerCreatePacketAdapter(plugin: Plugin, private val api: PrefixesApi) : PacketAdapter(
    plugin,
    ListenerPriority.NORMAL,
    PacketType.Play.Client.CHAT_SESSION_UPDATE
) {

    override fun onPacketReceiving(event: PacketEvent) {
        if (event.packetType == PacketType.Play.Client.CHAT_SESSION_UPDATE && !api.hasViewer(event.player.uniqueId)) {
            api.registerViewer(event.player.uniqueId)
            val playerGroup: PrefixesGroup = api.getHighestGroup(event.player.uniqueId)
            api.setWholeName(event.player.uniqueId, playerGroup)
        }
    }
}