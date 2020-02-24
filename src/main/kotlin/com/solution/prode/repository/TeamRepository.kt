package com.solution.prode.repository

import com.solution.prode.model.Team
import org.springframework.data.repository.CrudRepository

interface TeamRepository : CrudRepository<Team, Long> {

    fun findTeamById(id: Long): Team? = findById(id).orElse(null)
}