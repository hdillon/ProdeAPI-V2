package com.solution.prode.service

import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TeamService {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    private val entityName: String = "Team"

    private val entityIdField: String = "id"

    fun findAllTeams(): List<Team> = teamRepository.findAll().toList()

    fun findTeamById(id: Long): Team? = teamRepository.findTeamById(id)

    fun saveTeam(newTeam: Team): Team {

        return teamRepository.save(newTeam)
    }

    fun updateTeam(teamId: Long, updatedTeam: Team): Team {

        val team = validateTeamExists(teamId)

        updatedTeam.id = team.id

        teamRepository.save(updatedTeam)

        return updatedTeam
    }

    fun deleteTeam(teamId: Long) {

        val team = validateTeamExists(teamId)

        teamRepository.delete(team)
    }

    private fun validateTeamExists(teamId: Long): Team =
        teamRepository.findTeamById(teamId) ?: throw ResourceNotFoundException(entityName, entityIdField, teamId)
}