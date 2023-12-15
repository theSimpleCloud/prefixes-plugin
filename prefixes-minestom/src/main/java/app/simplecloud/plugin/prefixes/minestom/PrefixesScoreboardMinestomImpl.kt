package app.simplecloud.plugin.prefixes.minestom

import app.simplecloud.plugin.prefixes.api.PrefixesScoreboard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.scoreboard.Team
import java.util.*

class PrefixesScoreboardMinestomImpl : PrefixesScoreboard<Component, String> {

    private val teamManager = MinecraftServer.getTeamManager()

    private fun createTeamReturning(uniqueId: UUID): Team? {
        if (teamManager.getTeam(uniqueId.toString()) == null)
            return teamManager.createTeam(uniqueId.toString())
        return null
    }

    override fun createTeam(uniqueId: UUID) {
        createTeamReturning(uniqueId)
    }

    override fun update(uniqueId: UUID, prefix: Component, suffix: Component) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix;
        team.suffix = suffix;
    }

    override fun remove(player: String) {
        teamManager.deleteTeam(player)
    }

    override fun apply(uniqueId: UUID, player: String) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.addMember(player)
    }

    fun update(uniqueId: UUID, prefix: Component, suffix: Component, color: NamedTextColor) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix
        team.suffix = suffix
        team.teamColor = color
    }

    override fun updateSuffix(uniqueId: UUID, suffix: Component) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.suffix = suffix
    }

    override fun updatePrefix(uniqueId: UUID, prefix: Component) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix
    }


    fun updateColor(uniqueId: UUID, color: NamedTextColor) {
        val team: Team = teamManager.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.teamColor = color
    }
}