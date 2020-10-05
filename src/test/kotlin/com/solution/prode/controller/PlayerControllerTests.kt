package com.solution.prode.controller

import com.solution.prode.model.Player
import com.solution.prode.model.toJson
import com.solution.prode.routes.ALL
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.PLAYER
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PlayerControllerTests : BaseControllerTests() {

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertTeams.sql", "/db/seeds/insertPlayers.sql"]
    )
    fun `List all players`() {

        val teamIdOne = 100L
        val teamIdTwo = 101L

        val playerIdOne = 100L
        val firstNameOne = "first_name_a"
        val lastNameOne = "last_name_a"

        val playerIdTwo = 101L
        val firstNameTwo = "first_name_b"
        val lastNameTwo = "last_name_b"

        val playerIdThree = 102L
        val firstNameThree = "first_name_c"
        val lastNameThree = "last_name_c"

        val request = MockMvcRequestBuilders.get(PLAYER + ALL)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(playerIdOne))
            .andExpect(jsonPath("\$.[0].first_name").value(firstNameOne))
            .andExpect(jsonPath("\$.[0].last_name").value(lastNameOne))
            .andExpect(jsonPath("\$.[0].team_id").value(teamIdOne))
            .andExpect(jsonPath("\$.[1].id").value(playerIdTwo))
            .andExpect(jsonPath("\$.[1].first_name").value(firstNameTwo))
            .andExpect(jsonPath("\$.[1].last_name").value(lastNameTwo))
            .andExpect(jsonPath("\$.[1].team_id").value(teamIdTwo))
            .andExpect(jsonPath("\$.[2].id").value(playerIdThree))
            .andExpect(jsonPath("\$.[2].first_name").value(firstNameThree))
            .andExpect(jsonPath("\$.[2].last_name").value(lastNameThree))
            .andExpect(jsonPath("\$.[2].team_id").value(teamIdOne))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertTeams.sql", "/db/seeds/insertPlayers.sql"]
    )
    fun `Retrieve one player`() {

        val playerId = 100L
        val firstName = "first_name_a"
        val lastName = "last_name_a"
        val teamId = 100L

        val request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, playerId)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(playerId))
            .andExpect(jsonPath("\$.first_name").value(firstName))
            .andExpect(jsonPath("\$.last_name").value(lastName))
            .andExpect(jsonPath("\$.team_id").value(teamId))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `Create player`() {

        val firstName = "first_name_a"
        val lastName = "last_name_a"
        val teamId = 100L

        val newPlayer = Player(firstName = firstName, lastName = lastName, teamId = teamId)

        var request = MockMvcRequestBuilders.post(PLAYER)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(newPlayer.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        val responseAsString = mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").isNotEmpty)
            .andExpect(jsonPath("\$.first_name").value(newPlayer.firstName))
            .andExpect(jsonPath("\$.last_name").value(newPlayer.lastName))
            .andExpect(jsonPath("\$.team_id").value(newPlayer.teamId))
            .andReturn().response.contentAsString

        val responsePlayer = objectMapper.readValue(responseAsString, Player::class.java)

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, responsePlayer.id)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(responsePlayer.id))
            .andExpect(jsonPath("\$.first_name").value(newPlayer.firstName))
            .andExpect(jsonPath("\$.last_name").value(newPlayer.lastName))
            .andExpect(jsonPath("\$.team_id").value(newPlayer.teamId))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertTeams.sql", "/db/seeds/insertPlayers.sql"]
    )
    fun `Update one player`() {

        val id: Long = 100
        val firstName = "first_name_z"
        val lastName = "last_name_z"
        val teamId = 101L

        val updatedPlayer = Player(id, firstName, lastName, teamId)

        var request = MockMvcRequestBuilders.put(PLAYER + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(updatedPlayer.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedPlayer.id))
            .andExpect(jsonPath("\$.first_name").value(updatedPlayer.firstName))
            .andExpect(jsonPath("\$.last_name").value(updatedPlayer.lastName))
            .andExpect(jsonPath("\$.team_id").value(updatedPlayer.teamId))

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedPlayer.id))
            .andExpect(jsonPath("\$.first_name").value(updatedPlayer.firstName))
            .andExpect(jsonPath("\$.last_name").value(updatedPlayer.lastName))
            .andExpect(jsonPath("\$.team_id").value(updatedPlayer.teamId))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertTeams.sql", "/db/seeds/insertPlayers.sql"]
    )
    fun `Delete one player`() {

        val id: Long = 100
        val firstName = "first_name_a"
        val lastName = "last_name_a"
        val teamId = 100L

        val somePlayer = Player(id, firstName, lastName, teamId)

        var request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(somePlayer.id))
            .andExpect(jsonPath("\$.first_name").value(somePlayer.firstName))
            .andExpect(jsonPath("\$.last_name").value(somePlayer.lastName))
            .andExpect(jsonPath("\$.team_id").value(somePlayer.teamId))

        request = MockMvcRequestBuilders.delete(PLAYER + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isNotFound)
    }
}
