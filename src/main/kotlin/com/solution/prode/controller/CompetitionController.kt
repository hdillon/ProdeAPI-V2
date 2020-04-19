package com.solution.prode.controller

import com.solution.prode.model.Competition
import com.solution.prode.routes.ALL
import com.solution.prode.routes.COMPETITION
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.service.CompetitionService
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
@RequestMapping(COMPETITION)
class CompetitionController(private val competitionService: CompetitionService) {

    @GetMapping(ALL)
    fun findAll(): List<Competition> = competitionService.findAll()

    @GetMapping(ID_PARAM)
    fun findById(@PathVariable id: Long): Competition = competitionService.findById(id)

    @PostMapping
    fun create(
        @Valid @RequestBody newCompetition: Competition
    ): Competition = competitionService.save(newCompetition)

    @PutMapping(ID_PARAM)
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newCompetition: Competition): Competition = competitionService.update(id, newCompetition)

    @DeleteMapping(ID_PARAM)
    fun deleteById(@PathVariable(value = "id") id: Long) = competitionService.delete(id)
}
