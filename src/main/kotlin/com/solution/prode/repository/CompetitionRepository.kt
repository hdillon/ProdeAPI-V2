package com.solution.prode.repository

import com.solution.prode.model.Competition
import org.springframework.data.repository.CrudRepository

interface CompetitionRepository : CrudRepository<Competition, Long> {

    fun findAllByOrderByIdAsc(): List<Competition>

    fun findByName(name: String): Competition?
}
