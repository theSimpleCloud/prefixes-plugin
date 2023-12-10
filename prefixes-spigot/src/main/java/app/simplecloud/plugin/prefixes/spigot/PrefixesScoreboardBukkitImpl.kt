package app.simplecloud.plugin.prefixes.spigot

import app.simplecloud.plugin.prefixes.api.PrefixesScoreboard
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.*

class PrefixesScoreboardBukkitImpl : PrefixesScoreboard<String, String> {

    private lateinit var scoreboard: Scoreboard

    fun initScoreboard(scoreboard: Scoreboard)
    {
        if(!this::scoreboard.isInitialized)
        this.scoreboard = scoreboard
    }

    override fun createTeam(uniqueId: UUID) {
        createTeamReturning(uniqueId)
    }

    fun getTeam(uniqueId: UUID): Team?
    {
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

    private fun createTeamReturning(uniqueId: UUID): Team? {
        if(scoreboard.getTeam(uniqueId.toString()) == null)
            return scoreboard.registerNewTeam(uniqueId.toString())
        return null
    }

    override fun update(uniqueId: UUID, prefix: String, suffix: String) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix
        team.suffix = suffix
    }

    fun update(uniqueId: UUID, prefix: String, suffix: String, color: ChatColor) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix
        team.suffix = suffix
        team.color = color
    }

    override fun updateSuffix(uniqueId: UUID, suffix: String) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.suffix = suffix
    }

    override fun updatePrefix(uniqueId: UUID, prefix: String) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.prefix = prefix
    }


    fun updateColor(uniqueId: UUID, color: ChatColor) {
        val team: Team = scoreboard.getTeam(uniqueId.toString()) ?: createTeamReturning(uniqueId) ?: return
        team.color = color
    }

}