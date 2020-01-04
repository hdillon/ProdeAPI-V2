package com.solution.prode.service

import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService(private val repository: TeamRepository) {

    fun findAllTeams(): Iterable<Team> = repository.findAll()

    fun findTeamById(id: Long): Optional<Team> = repository.findById(id)

    fun saveTeam(newTeam: Team) = repository.save(newTeam)

    fun deleteTeam(team: Team) = repository.delete(team)

}