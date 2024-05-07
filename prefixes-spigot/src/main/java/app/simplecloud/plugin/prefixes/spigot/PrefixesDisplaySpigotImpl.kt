package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesDisplay
import app.simplecloud.plugin.prefixes.spigot.packet.PacketTeam
import app.simplecloud.plugin.prefixes.spigot.packet.UpdateTeamMode
import com.comphenix.protocol.ProtocolManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import org.checkerframework.checker.units.qual.C

class PrefixesDisplaySpigotImpl(
    private val manager: ProtocolManager
) : PrefixesDisplay<Component, Player, PacketTeam> {

    private val teams: MutableMap<String, PacketTeam> = mutableMapOf()
    private val viewers: MutableSet<Player> = mutableSetOf()

    override fun createTeam(id: String, priority: Int): PacketTeam? {
        if (getTeam(id) != null) return null;
        val name = "${toPriorityString(priority)}$id"
        val team = PacketTeam(name, null, null, null, mutableListOf())
        teams[name] = team
        return team;
    }

    override fun getTeam(id: String): PacketTeam? {
        return teams.getOrDefault(teams.keys.find { it.endsWith(id) }, null)
    }

    override fun updatePriority(id: String, priority: Int): PacketTeam? {
        val newId = "${toPriorityString(priority)}$id"
        if(teams.containsKey(newId)) return null
        val team = getTeam(id) ?: return null
        val packets = team.updateId(newId)
        viewers.forEach { viewer ->
            packets.forEach { packet ->
                manager.sendServerPacket(viewer, packet)
            }
        }
        return team
    }

    override fun updateColor(id: String, color: NamedTextColor) {
        val team = getTeam(id) ?: return
        team.color = color
        val packet = team.getTeamUpdatePacket(UpdateTeamMode.UPDATE)
        viewers.forEach { viewer ->
            manager.sendServerPacket(viewer, packet)
        }
    }

    override fun getViewers(): Set<Player> {
        return viewers
    }

    override fun removeViewer(player: Player): Boolean {
        val result = viewers.remove(player)
        if (result) {
            teams.values.forEach { team ->
                manager.sendServerPacket(player, team.getTeamDeletePacket())
            }
        }
        return result
    }

    override fun addViewer(player: Player): Boolean {
        val result = viewers.add(player)
        if (result) {
            teams.values.forEach { team ->
                team.getUpdateDisplayNamePackets().forEach { packet ->
                    manager.sendServerPacket(player, packet)
                }
            }
        }
        return result
    }

    override fun setViewer(player: Player): Boolean {
        viewers.forEach { removeViewer(it) }
        return addViewer(player)
    }

    override fun removePlayer(player: Player) {
        teams.forEach {
            if (it.value.members.contains(player)) {
                it.value.members.remove(player)
                val packet = it.value.getRemoveTeamMembersPacket(player)
                val displayPacket = it.value.getUpdateDisplayNamePacket(player)
                viewers.forEach { viewer ->
                    manager.sendServerPacket(viewer, packet)
                    manager.sendServerPacket(viewer, displayPacket)
                }
            }
        }
    }

    override fun addPlayer(id: String, player: Player) {
        val team = getTeam(id) ?: return
        if(!team.members.contains(player)) {
            team.members.add(player)
            val displayPacket = team.getUpdateDisplayNamePacket(player)
                viewers.forEach { viewer ->
                    manager.sendServerPacket(viewer, displayPacket)
                }

        }
    }

    override fun update(id: String, prefix: Component, suffix: Component, priority: Int) {
        val exists = getTeam(id) != null
        val team = updatePriority(id, priority) ?: createTeam(id, priority) ?: return
        team.prefix = prefix
        team.suffix = suffix
        val packet = team.getTeamUpdatePacket(if(exists) UpdateTeamMode.UPDATE else UpdateTeamMode.CREATE)
        val displayPackets = team.getUpdateDisplayNamePackets()
        viewers.forEach { viewer ->
            manager.sendServerPacket(viewer, packet)
            displayPackets.forEach { display ->
                manager.sendServerPacket(viewer, display)
            }
        }
    }

    override fun updateSuffix(id: String, suffix: Component) {
        val team = getTeam(id) ?: return
        team.suffix = suffix
        val packet = team.getTeamUpdatePacket(UpdateTeamMode.UPDATE)
        val displayPackets = team.getUpdateDisplayNamePackets()
        viewers.forEach { viewer ->
            manager.sendServerPacket(viewer, packet)
            displayPackets.forEach { display ->
                manager.sendServerPacket(viewer, display)
            }
        }
    }

    override fun updatePrefix(id: String, prefix: Component) {
        val team = getTeam(id) ?: return
        team.prefix = prefix
        val packet = team.getTeamUpdatePacket(UpdateTeamMode.UPDATE)
        val displayPackets = team.getUpdateDisplayNamePackets()
        viewers.forEach { viewer ->
            manager.sendServerPacket(viewer, packet)
            displayPackets.forEach { display ->
                manager.sendServerPacket(viewer, display)
            }
        }
    }

}