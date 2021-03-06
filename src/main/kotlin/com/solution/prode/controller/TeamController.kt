package com.solution.prode.controller

import com.solution.prode.model.Team
import com.solution.prode.routes.ALL
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.TEAM
import com.solution.prode.service.TeamService
import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TEAM)
class TeamController(private val teamService: TeamService) {

    @GetMapping(ALL)
    fun findAll() = teamService.findAllTeams()

    @GetMapping(ID_PARAM)
    fun findById(@PathVariable id: Long) = teamService.findTeamById(id)

    @PostMapping
    fun create(@Valid @RequestBody newTeam: Team): Team = teamService.saveTeam(newTeam)

    @PutMapping(ID_PARAM)
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newTeam: Team): Team = teamService.updateTeam(id, newTeam)

    @DeleteMapping(ID_PARAM)
    fun deleteById(@PathVariable(value = "id") id: Long) = teamService.deleteTeam(id)
}
