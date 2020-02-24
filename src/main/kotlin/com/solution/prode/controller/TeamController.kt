package com.solution.prode.controller

import com.solution.prode.model.Team
import com.solution.prode.service.TeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/team")
class TeamController(private val service: TeamService) {

    @GetMapping("/all")
    fun findAll() =
            service.findAllTeams()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
            service.findTeamById(id)

    @PostMapping
    fun create(@Valid @RequestBody newTeam: Team): Team =
            service.saveTeam(newTeam)

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newTeam: Team): ResponseEntity<Team> {
        return service.findTeamById(id).map { existingTeam ->
            val updatedTeam: Team = existingTeam.copy(name = newTeam.name)
            ResponseEntity.ok().body(service.saveTeam(updatedTeam))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        return service.findTeamById(id).map { team  ->
            service.deleteTeam(team)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }

}