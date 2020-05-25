package com.solution.prode.service

import com.solution.prode.constants.CacheKeys.ALL_TEAMS
import com.solution.prode.exception.InternalException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TeamService {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Cacheable(value = [ALL_TEAMS], keyGenerator = "CacheKeyGenerator", cacheManager = "cacheManagerOneDay")
    fun findAllTeams(): List<Team> = teamRepository.findAll().toList()

    fun findTeamById(id: Long): Team = validateTeamExists(id)

    @CacheEvict(value = [ALL_TEAMS], allEntries = true)
    fun saveTeam(newTeam: Team): Team {

        validateTeamNameNotExists(newTeam.name)

        return teamRepository.save(newTeam)
    }

    @CacheEvict(value = [ALL_TEAMS], allEntries = true)
    fun updateTeam(teamId: Long, updatedTeam: Team): Team {

        validateTeamNameNotExists(updatedTeam.name)

        val team = validateTeamExists(teamId)

        updatedTeam.id = team.id

        teamRepository.save(updatedTeam)

        return updatedTeam
    }

    @CacheEvict(value = [ALL_TEAMS], allEntries = true)
    fun deleteTeam(teamId: Long) {

        val team = validateTeamExists(teamId)

        teamRepository.delete(team)
    }

    fun validateTeamExists(teamId: Long) =
        teamRepository.findTeamById(teamId) ?: throw ResourceNotFoundException(Team.ENTITY_NAME, Team.ID, teamId)

    fun validateTeamNameNotExists(teamName: String) {

        val team = teamRepository.findTeamByName(teamName)

        if (team != null) {

            throw InternalException("Team name $teamName already exists")
        }
    }

    @CacheEvict(value = [ALL_TEAMS], allEntries = true)
    fun cleanTeamsCache() { }
}
