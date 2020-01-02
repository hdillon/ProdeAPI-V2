package com.solution.prode.controller

import com.solution.prode.repository.TeamRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/team")
class TeamController(private val repository: TeamRepository) {

    @GetMapping
    fun findAll() = repository.findAll()

}