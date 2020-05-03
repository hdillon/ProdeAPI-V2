package com.solution.prode.controller

import com.solution.prode.model.Player
import com.solution.prode.model.toJson
import com.solution.prode.routes.ALL
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.PLAYER
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

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
            .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0].id").value(playerIdOne))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0].first_name").value(firstNameOne))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0].last_name").value(lastNameOne))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0].team_id").value(teamIdOne))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[1].id").value(playerIdTwo))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[1].first_name").value(firstNameTwo))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[1].last_name").value(lastNameTwo))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[1].team_id").value(teamIdTwo))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[2].id").value(playerIdThree))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[2].first_name").value(firstNameThree))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[2].last_name").value(lastNameThree))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[2].team_id").value(teamIdOne))
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
            .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(playerId))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(teamId))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `Create player`() {

        val firstName = "first_name_a"
        val lastName = "last_name_a"
        val teamId = 100L

        val newPlayer = Player(firstName = firstName, lastName = lastName, teamId = teamId)

        var request = MockMvcRequestBuilders.post(PLAYER)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(newPlayer.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        val responseAsString = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(newPlayer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(newPlayer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(newPlayer.teamId))
            .andReturn().response.contentAsString

        val responsePlayer = objectMapper.readValue(responseAsString, Player::class.java)

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, responsePlayer.id)
            .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(responsePlayer.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(newPlayer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(newPlayer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(newPlayer.teamId))
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
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(updatedPlayer.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(updatedPlayer.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(updatedPlayer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(updatedPlayer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(updatedPlayer.teamId))

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, id.toString())
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(updatedPlayer.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(updatedPlayer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(updatedPlayer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(updatedPlayer.teamId))
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
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(somePlayer.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.first_name").value(somePlayer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.last_name").value(somePlayer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.team_id").value(somePlayer.teamId))

        request = MockMvcRequestBuilders.delete(PLAYER + ID_PARAM, id.toString())
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)

        request = MockMvcRequestBuilders.get(PLAYER + ID_PARAM, id.toString())
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
