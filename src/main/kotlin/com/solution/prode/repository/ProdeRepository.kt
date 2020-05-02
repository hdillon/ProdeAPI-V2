package com.solution.prode.repository

import com.solution.prode.model.Prode
import java.util.Optional
import org.springframework.data.repository.CrudRepository

interface ProdeRepository : CrudRepository<Prode, Long> {

    fun findAllByCompetitionId(competitionId: Long): List<Prode>

    fun findByName(name: String): Optional<Prode>

    fun findProdeByName(name: String): Prode? = findByName(name).orElse(null)

    fun findByNameAndCompetitionId(name: String, competitionId: Long): Prode?

    fun findAllByOrderByIdAsc(): List<Prode>
}
