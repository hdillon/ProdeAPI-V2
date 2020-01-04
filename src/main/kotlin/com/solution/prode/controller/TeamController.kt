package com.solution.prode.controller

import com.solution.prode.model.Team
import com.solution.prode.repository.TeamRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/team")
class TeamController(private val repository: TeamRepository) {

    @GetMapping("/all")
    fun findAll() = repository.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = repository.findById(id)

    @PostMapping
    fun create(@Valid @RequestBody newTeam: Team): Team =
            repository.save(newTeam)

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newTeam: Team): ResponseEntity<Team> {
        return repository.findById(id).map { existingTeam ->
            val updatedTeam: Team = existingTeam.copy(name = newTeam.name)
            ResponseEntity.ok().body(repository.save(updatedTeam))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        return repository.findById(id).map { team  ->
            repository.delete(team)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }

}