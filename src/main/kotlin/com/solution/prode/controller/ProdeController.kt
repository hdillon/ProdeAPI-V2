package com.solution.prode.controller

import com.solution.prode.model.Prode
import com.solution.prode.routes.ALL
import com.solution.prode.routes.BY_COMPETITION
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.PRODE
import com.solution.prode.service.ProdeService
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
@RequestMapping(PRODE)
class ProdeController(private val prodeService: ProdeService) {

    @GetMapping(ALL)
    fun findAll(): List<Prode> = prodeService.findAll()

    @GetMapping(BY_COMPETITION + ID_PARAM)
    fun findAllByCompetition(@PathVariable id: Long): List<Prode> = prodeService.findAllByCompetition(id)

    @GetMapping(ID_PARAM)
    fun findById(@PathVariable id: Long): Prode = prodeService.findById(id)

    @PostMapping
    fun create(
        @Valid @RequestBody newProde: Prode
    ): Prode = prodeService.save(newProde)

    @PutMapping(ID_PARAM)
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newProde: Prode): Prode = prodeService.update(id, newProde)

    @DeleteMapping(ID_PARAM)
    fun deleteById(@PathVariable(value = "id") id: Long) = prodeService.delete(id)
}
