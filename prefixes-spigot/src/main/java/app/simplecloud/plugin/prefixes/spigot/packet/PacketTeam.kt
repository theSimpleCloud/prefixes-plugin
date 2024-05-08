package app.simplecloud.plugin.prefixes.spigot.packet

import app.simplecloud.plugin.prefixes.spigot.LegacyComponentSerializerImpl
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedGameProfile
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class PacketTeam(
    private var id: String,
    var color: TextColor?,
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
        packet.integers.write(0, mode.getMode())
        if(mode == UpdateTeamMode.CREATE)
            packet.getSpecificModifier(Collection::class.java).write(0, members.map { it.name })

        packet.strings.write(0, id)
        if(packet.optionalStructures.size() > 0) {
            val optional = packet.optionalStructures.read(0).get()
            if(prefix != null) optional.chatComponents.write(1, LegacyComponentSerializerImpl.serializeToPacket(prefix!!))
            if(suffix != null) optional.chatComponents.write(2, LegacyComponentSerializerImpl.serializeToPacket(suffix!!))
            optional.getEnumModifier(ChatColor::class.java, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, if(color != null) ChatColor.valueOf(NamedTextColor.nearestTo(color!!).toString().uppercase()) else ChatColor.GRAY)
            packet.optionalStructures.write(0, Optional.of(optional))
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

    fun getAddTeamMemberPacket(player: Player): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM)
        if(packet.integers.size() > 1) {
            packet.integers.write(1, 3)
        }else if(packet.integers.size() > 0) {
            packet.integers.write(0, 3)
        }
        packet.strings.write(0, id)
        packet.getSpecificModifier(Collection::class.java).write(0, listOf(player.name))
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
        val base = if(prefix != null) prefix!!.append(Component.text(player.name).color(color)) else Component.text(player.name)
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