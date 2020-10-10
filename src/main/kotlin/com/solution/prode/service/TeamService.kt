package com.solution.prode.service

import com.solution.prode.constants.CacheKeys.ALL_TEAMS_KEY
import com.solution.prode.exception.InternalException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.manager.ProdeCacheManager
import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TeamService {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var prodeCacheManager: ProdeCacheManager

    fun findAllTeams(): List<Team> {

        val teams = teamRepository.findAll().toList()
        prodeCacheManager.saveAllTeams(ALL_TEAMS_KEY, teams)

        return teams
    }

    fun findTeamById(id: Long): Team = validateTeamExists(id)

    fun saveTeam(newTeam: Team): Team {

        validateTeamNameNotExists(newTeam.name)
        prodeCacheManager.evictAllTeams(ALL_TEAMS_KEY)

        return teamRepository.save(newTeam)
    }

    fun updateTeam(teamId: Long, updatedTeam: Team): Team {

        validateTeamNameNotExists(updatedTeam.name)

        val team = validateTeamExists(teamId)

        updatedTeam.id = team.id

        teamRepository.save(updatedTeam)
        prodeCacheManager.evictAllTeams(ALL_TEAMS_KEY)

        return updatedTeam
    }

    fun deleteTeam(teamId: Long) {

        val team = validateTeamExists(teamId)

        teamRepository.delete(team)
        prodeCacheManager.evictAllTeams(ALL_TEAMS_KEY)
    }

    fun validateTeamExists(teamId: Long) =
        teamRepository.findTeamById(teamId) ?: throw ResourceNotFoundException(Team.ENTITY_NAME, Team.ID, teamId)

    fun validateTeamNameNotExists(teamName: String) {

        val team = teamRepository.findTeamByName(teamName)

        team?.let { throw InternalException("Team name $teamName already exists") }
    }
}
