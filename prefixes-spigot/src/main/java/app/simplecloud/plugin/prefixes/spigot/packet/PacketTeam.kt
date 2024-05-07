package app.simplecloud.plugin.prefixes.spigot.packet

import WrappedScoreboardTeam
import app.simplecloud.plugin.prefixes.spigot.LegacyComponentSerializerImpl
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.InternalStructure
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import com.google.common.base.Optional
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.EnumSet

class PacketTeam(
    private var id: String,
    var color: NamedTextColor?,
    var prefix: Component?,
    var suffix: Component?,
    var members: MutableList<Player> = mutableListOf()
) {
    fun updateId(id: String): List<PacketContainer> {
        val toReturn = mutableListOf<PacketContainer>()
        toReturn.add(getTeamDeletePacket())
        this.id = id
        toReturn.add(getTeamUpdatePacket(UpdateTeamMode.CREATE))
        return toReturn
    }

    fun getTeamUpdatePacket(mode: UpdateTeamMode): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
        val optionals = packet.modifier.withType<Optional<Any>>(Optional::class.java)
        if(packet.integers.size() > 1) {
            packet.integers.write(1, mode.getMode())
        }else if(packet.integers.size() > 0) {
            packet.integers.write(0, mode.getMode())
        }
        if(mode == UpdateTeamMode.CREATE)
            packet.getSpecificModifier(Collection::class.java).write(0, members.map { it.name })

        packet.strings.writeSafely(0, id)
        packet.strings.writeSafely(1, id)
        if(prefix != null) packet.strings.writeSafely(2, LegacyComponentSerializerImpl.serialize(prefix!!))
        if(suffix != null) packet.strings.writeSafely(3, LegacyComponentSerializerImpl.serialize(suffix!!))

        packet.chatComponents.writeSafely(0, WrappedChatComponent.fromText(id))
        packet.chatComponents.writeSafely(1, LegacyComponentSerializerImpl.serializeToPacket(prefix ?: Component.text("")))
        packet.chatComponents.writeSafely(2, LegacyComponentSerializerImpl.serializeToPacket(suffix ?: Component.text("")))

        if(optionals.size() > 0) {
            val packetTeam = WrappedScoreboardTeam.fromHandle(optionals.read(0).get())
            packetTeam.displayName = WrappedChatComponent.fromText(id)
            packetTeam.prefix = LegacyComponentSerializerImpl.serializeToPacket(prefix ?: Component.text(""))
            packetTeam.suffix = LegacyComponentSerializerImpl.serializeToPacket(suffix ?: Component.text(""))
            if(color != null)
                packetTeam.teamColor = ChatColor.valueOf(color.toString().uppercase())
            println(packetTeam.handle)
            optionals.write(0, Optional.of(packetTeam.handle))
        }

        return packet
    }
    fun getTeamDeletePacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
        if(packet.integers.size() > 1) {
            packet.integers.write(1, 1)
        }else if(packet.integers.size() > 0) {
            packet.integers.write(0, 1)
        }
        packet.strings.write(0, id)
        return packet
    }

    fun getUpdateTeamMembersPackets(): List<PacketContainer> {
        return listOf(getRemoveTeamMembersPacket(*members.toTypedArray()), getAddTeamMembersPacket())
    }

    fun getAddTeamMembersPacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
        if(packet.integers.size() > 1) {
            packet.integers.write(1, 3)
        }else if(packet.integers.size() > 0) {
            packet.integers.write(0, 3)
        }
        packet.strings.write(0, id)
        packet.getSpecificModifier(Collection::class.java).write(0, members.map { it.name })
        return packet
    }
    fun getRemoveTeamMembersPacket(vararg player: Player): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
        if(packet.integers.size() > 1) {
            packet.integers.write(1, 4)
        }else if(packet.integers.size() > 0) {
            packet.integers.write(0, 4)
        }
        packet.strings.write(0, id)
        packet.getSpecificModifier(Collection::class.java).write(0, player.map { it.name })
        return packet
    }
    fun getUpdateDisplayNamePackets(): List<PacketContainer> {
        return members.map { getUpdateDisplayNamePacket(it) }
    }

    fun getUpdateDisplayNamePacket(player: Player): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.PLAYER_INFO)
        packet.playerInfoActions.write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME))
        val infoData = mutableListOf<PlayerInfoData>()
        val profile = WrappedGameProfile.fromPlayer(player)
        infoData.add(PlayerInfoData(profile, player.ping, EnumWrappers.NativeGameMode.fromBukkit(player.gameMode), LegacyComponentSerializerImpl.serializeToPacket(constructDisplayName(player))))
        packet.playerInfoDataLists.write(1, infoData)
        return packet
    }

    private fun constructDisplayName(player: Player): Component {
        if(!members.contains(player)) return Component.text(player.name)
        val base = if(prefix != null) prefix!!.append(Component.text(player.name)) else Component.text(player.name)
        if(suffix != null) base.append(suffix!!)
        return base
    }
}

enum class UpdateTeamMode(private val mode: Int) {
    CREATE(0),
    UPDATE(2);

    fun getMode(): Int {
        return mode;
    }
}