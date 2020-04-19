package com.solution.prode.repository

import com.solution.prode.model.Player
import org.springframework.data.repository.CrudRepository

interface PlayerRepository : CrudRepository<Player, Long> {

    fun findAllByTeamId(teamId: Long): List<Player>

    fun findByFirstNameAndLastNameAndTeamId(firstName: String, lastName: String, teamId: Long): Player?

    fun findAllByOrderByIdAsc(): List<Player>
}
