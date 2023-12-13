package app.simplecloud.plugin.prefixes.paper

import app.simplecloud.plugin.prefixes.api.PrefixesScoreboard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.*

class PrefixesScoreboardPaperImpl : PrefixesScoreboard<Component, String> {
    private lateinit var scoreboard: Scoreboard
    fun setScoreboard(scoreboard: Scoreboard) {
        this.scoreboard = scoreboard
    }

    private fun createTeamReturning(uniqueId: UUID): Team? {
        if (scoreboard.getTeam(uniqueId.toString()) == null)
            return scoreboard.registerNewTeam(uniqueId.toString())
        return null
    }

    override fun createTeam(uniqueId: UUID) {
        createTeamReturning(uniqueId)
    }

    override fun update(uniqueId: UUID, prefix: Component, suffix: Component) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix(prefix)
        team.suffix(suffix)
    }

    fun getTeam(uniqueId: UUID): Team? {
        return scoreboard.getEntryTeam(Bukkit.getPlayer(uniqueId)!!.name)
    }

    override fun remove(player: String) {
        val team: Team = scoreboard.getEntryTeam(player) ?: return
        team.removeEntry(player)
    }

    override fun apply(uniqueId: UUID, player: String) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.addEntry(player)
    }

    fun update(uniqueId: UUID, prefix: Component, suffix: Component, color: NamedTextColor) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix(prefix)
        team.suffix(suffix)
        team.color(color)
    }

    override fun updateSuffix(uniqueId: UUID, suffix: Component) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.suffix(suffix)
    }

    override fun updatePrefix(uniqueId: UUID, prefix: Component) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix(prefix)
    }


    fun updateColor(uniqueId: UUID, color: NamedTextColor) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.color(color)
    }

}