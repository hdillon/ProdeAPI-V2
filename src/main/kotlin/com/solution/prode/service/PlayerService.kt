package com.solution.prode.service

import com.solution.prode.constants.ErrorCodes.PLAYER_ALREADY_EXISTS
import com.solution.prode.exception.BadRequestException
import com.solution.prode.exception.ResourceNotFoundException
import com.solution.prode.model.Player
import com.solution.prode.repository.PlayerRepository
import com.solution.prode.util.MessageTranslator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlayerService {

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var messageTranslator: MessageTranslator

    fun findAll(): List<Player> = playerRepository.findAllByOrderByIdAsc().toList()

    fun findById(id: Long): Player = validatePlayerExists(id)

    fun findAllByTeam(teamId: Long): List<Player> {

        val team = teamService.validateTeamExists(teamId)

        return playerRepository.findAllByTeamId(team.id)
    }

    fun save(newPlayer: Player): Player {

        validatePlayerNotExists(newPlayer)

        teamService.validateTeamExists(newPlayer.teamId)

        return playerRepository.save(newPlayer)
    }

    fun update(id: Long, updatedPlayer: Player): Player {

        val player = validatePlayerExists(id)

        updatedPlayer.id = player.id

        playerRepository.save(updatedPlayer)

        return updatedPlayer
    }

    fun delete(id: Long) {

        val player = validatePlayerExists(id)

        playerRepository.delete(player)
    }

    fun validatePlayerExists(id: Long): Player =
        playerRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException(Player.ENTITY_NAME, Player.ID, id)

    fun validatePlayerNotExists(newPlayer: Player) {

        val player = playerRepository.findByFirstNameAndLastNameAndTeamId(newPlayer.firstName, newPlayer.lastName, newPlayer.teamId)

        player?.let {
            throw BadRequestException(messageTranslator.getMessage(
                PLAYER_ALREADY_EXISTS, arrayOf(newPlayer.firstName, newPlayer.lastName)
            ))
        }
    }
}
