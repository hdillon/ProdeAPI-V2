package com.solution.prode.service

import com.solution.prode.exception.BadRequestException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Competition
import com.solution.prode.repository.CompetitionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CompetitionService {

    @Autowired
    private lateinit var competitionRepository: CompetitionRepository

    fun findAll(): List<Competition> = competitionRepository.findAllByOrderByIdAsc().toList()

    fun findById(id: Long): Competition = validateCompetitionExists(id)

    fun save(newCompetition: Competition): Competition {

        validateCompetitionNotExists(newCompetition)

        return competitionRepository.save(newCompetition)
    }

    fun update(id: Long, updatedCompetiton: Competition): Competition {

        val competition = validateCompetitionExists(id)

        updatedCompetiton.id = competition.id

        competitionRepository.save(updatedCompetiton)

        return updatedCompetiton
    }

    fun delete(id: Long) {

        val competition = validateCompetitionExists(id)

        competitionRepository.delete(competition)
    }

    fun validateCompetitionExists(id: Long): Competition =
        competitionRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(Competition.ENTITY_NAME, Competition.ID, id)

    fun validateCompetitionNotExists(newCompetition: Competition) {

        val competition = competitionRepository.findByName(newCompetition.name)

        competition?.let { throw BadRequestException("Competition with name ${newCompetition.name} already exists") }
    }
}
