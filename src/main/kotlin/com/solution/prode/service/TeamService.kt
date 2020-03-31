package com.solution.prode.service

import com.solution.prode.constants.ErrorCodes
import com.solution.prode.exception.InternalException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TeamService {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    fun findAllTeams(): List<Team> = teamRepository.findAll().toList()

    fun findTeamById(id: Long): Team = validateTeamExists(id)

    fun saveTeam(newTeam: Team): Team {

        validateTeamNameNotExists(newTeam.name)

        return teamRepository.save(newTeam)
    }

    fun updateTeam(teamId: Long, updatedTeam: Team): Team {

        validateTeamNameNotExists(updatedTeam.name)

        val team = validateTeamExists(teamId)

        updatedTeam.id = team.id

        teamRepository.save(updatedTeam)

        return updatedTeam
    }

    fun deleteTeam(teamId: Long) {

        val team = validateTeamExists(teamId)

        teamRepository.delete(team)
    }

    fun validateTeamExists(teamId: Long): Team =
        teamRepository.findTeamById(teamId) ?: throw ResourceNotFoundException(Team.ENTITY_NAME, Team.ID, teamId)

    fun validateTeamNameNotExists(teamName: String) {

        val team = teamRepository.findTeamByName(teamName)

        if (team != null) {

            throw InternalException(ErrorCodes.INTERNAL_ERROR.value, "Team name $teamName already exists")
        }
    }
}
