package com.solution.prode.controller

import com.solution.prode.model.Team
import com.solution.prode.model.toJson
import com.solution.prode.routes.ALL
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.TEAM
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TeamControllerTests : BaseControllerTests() {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `List all teams`() {

        val idOne = 100L
        val nameOne = "team-a"
        val idTwo = 101L
        val nameTwo = "team-b"
        val idThree = 102L
        val nameThree = "team-c"

        val teamOne = Team(idOne, nameOne)
        val teamTwo = Team(idTwo, nameTwo)
        val teamThree = Team(idThree, nameThree)

        val request = get(TEAM + ALL)
                .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.[0].id").value(teamOne.id))
                .andExpect(jsonPath("\$.[0].name").value(teamOne.name))
                .andExpect(jsonPath("\$.[1].id").value(teamTwo.id))
                .andExpect(jsonPath("\$.[1].name").value(teamTwo.name))
                .andExpect(jsonPath("\$.[2].id").value(teamThree.id))
                .andExpect(jsonPath("\$.[2].name").value(teamThree.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `Retrieve one team`() {

        val id: Long = 100
        val name = "team-a"
        val someTeam = Team(id, name)

        val request = get(TEAM + ID_PARAM, id.toString())
                .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").value(someTeam.id))
                .andExpect(jsonPath("\$.name").value(someTeam.name))
    }

    @Test
    fun `Create team`() {

        val name = "team-z"
        val newTeam = Team(name = name)

        var request = post(TEAM)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(newTeam.toJson())
                .characterEncoding(Charsets.UTF_8.name())

        val responseAsString = mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").isNotEmpty)
                .andExpect(jsonPath("\$.name").value(newTeam.name))
                .andReturn().response.contentAsString

        val responseTeam = objectMapper.readValue(responseAsString, Team::class.java)

        request = get(TEAM + ID_PARAM, responseTeam.id)
                .accept(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").value(responseTeam.id))
                .andExpect(jsonPath("\$.name").value(newTeam.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `Update one team`() {

        val id: Long = 100
        val newName = "team-z"
        val updatedTeam = Team(id, newName)

        var request = put(TEAM + ID_PARAM, id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedTeam.toJson())
                .characterEncoding(Charsets.UTF_8.name())

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(updatedTeam.id))
                .andExpect(jsonPath("\$.name").value(updatedTeam.name))

        request = get(TEAM + ID_PARAM, id.toString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(updatedTeam.id))
                .andExpect(jsonPath("\$.name").value(updatedTeam.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertTeams.sql"])
    fun `Delete one team`() {

        val id: Long = 100
        val name = "team-a"
        val someTeam = Team(id, name)

        var request = get(TEAM + ID_PARAM, id.toString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id").value(someTeam.id))
                .andExpect(jsonPath("\$.name").value(someTeam.name))

        request = delete(TEAM + ID_PARAM, id.toString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isOk)

        request = get(TEAM + ID_PARAM, id.toString())
                .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(request)
                .andExpect(status().isNotFound)
    }
}
