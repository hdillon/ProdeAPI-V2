package com.solution.prode.service

import com.solution.prode.exception.BadRequestException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Prode
import com.solution.prode.repository.ProdeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProdeService {

    @Autowired
    private lateinit var prodeRepository: ProdeRepository

    @Autowired
    private lateinit var competitionService: CompetitionService

    fun findAll(): List<Prode> = prodeRepository.findAllByOrderByIdAsc().toList()

    fun findById(id: Long): Prode = validateProdeExists(id)

    fun findAllByCompetition(competitionId: Long): List<Prode> {

        val competition = competitionService.validateCompetitionExists(competitionId)

        return prodeRepository.findAllByCompetitionId(competition.id)
    }

    fun save(newProde: Prode): Prode {

        validateProdeNotExists(newProde)

        competitionService.validateCompetitionExists(newProde.competitionId)

        return prodeRepository.save(newProde)
    }

    fun update(id: Long, updatedProde: Prode): Prode {

        val prode = validateProdeExists(id)

        updatedProde.id = prode.id

        prodeRepository.save(updatedProde)

        return updatedProde
    }

    fun delete(id: Long) {

        val prode = validateProdeExists(id)

        prodeRepository.delete(prode)
    }

    fun validateProdeExists(id: Long): Prode =
        prodeRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(Prode.ENTITY_NAME, Prode.ID, id)

    fun validateProdeNotExists(newProde: Prode) {

        val prode = prodeRepository.findByNameAndCompetitionId(newProde.name, newProde.competitionId)

        prode?.let { throw BadRequestException("Prode ${newProde.name} already exists") }
    }
}
