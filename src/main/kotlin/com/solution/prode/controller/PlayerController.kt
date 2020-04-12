package com.solution.prode.controller

import com.solution.prode.model.Player
import com.solution.prode.routes.ALL
import com.solution.prode.routes.BY_TEAM
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.PLAYER
import com.solution.prode.service.PlayerService
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
@RequestMapping(PLAYER)
class PlayerController(private val playerService: PlayerService) {

    @GetMapping(ALL)
    fun findAll(): List<Player> = playerService.findAll()

    @GetMapping(BY_TEAM + ID_PARAM)
    fun findAllByTeam(@PathVariable id: Long): List<Player> = playerService.findAllByTeam(id)

    @GetMapping(ID_PARAM)
    fun findById(@PathVariable id: Long): Player = playerService.findById(id)

    @PostMapping
    fun create(
        @Valid @RequestBody newPlayer: Player
    ): Player = playerService.save(newPlayer)

    @PutMapping(ID_PARAM)
    fun updateById(@PathVariable id: Long, @Valid @RequestBody newPlayer: Player): Player = playerService.update(id, newPlayer)

    @DeleteMapping(ID_PARAM)
    fun deleteById(@PathVariable(value = "id") id: Long) = playerService.delete(id)
}
