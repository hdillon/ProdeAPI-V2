package com.solution.prode.repository

import com.solution.prode.model.Team
import org.springframework.data.repository.CrudRepository
import java.util.*

interface TeamRepository : CrudRepository<Team, Long> {

    fun findTeamById(id: Long): Team? = findById(id).orElse(null)

    fun findByName(name: String): Optional<Team>

    fun findTeamByName(name: String): Team? = findByName(name).orElse(null)
}