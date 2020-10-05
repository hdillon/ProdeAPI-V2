package com.solution.prode.controller

import com.solution.prode.model.Competition
import com.solution.prode.model.toJson
import com.solution.prode.routes.ALL
import com.solution.prode.routes.COMPETITION
import com.solution.prode.routes.ID_PARAM
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CompetitionControllerTests : BaseControllerTests() {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertCompetitions.sql"])
    fun `List all competitions`() {

        val idOne = 100L
        val nameOne = "competition_a"
        val idTwo = 101L
        val nameTwo = "competition_b"
        val idThree = 102L
        val nameThree = "competition_c"

        val competitionOne = Competition(idOne, nameOne)
        val competitionTwo = Competition(idTwo, nameTwo)
        val competitionThree = Competition(idThree, nameThree)

        val request = get(COMPETITION + ALL)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(competitionOne.id))
            .andExpect(jsonPath("\$.[0].name").value(competitionOne.name))
            .andExpect(jsonPath("\$.[1].id").value(competitionTwo.id))
            .andExpect(jsonPath("\$.[1].name").value(competitionTwo.name))
            .andExpect(jsonPath("\$.[2].id").value(competitionThree.id))
            .andExpect(jsonPath("\$.[2].name").value(competitionThree.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertCompetitions.sql"])
    fun `Retrieve one competition`() {

        val id: Long = 100
        val name = "competition_a"
        val someCompetition = Competition(id, name)

        val request = get(COMPETITION + ID_PARAM, id.toString())
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(someCompetition.id))
            .andExpect(jsonPath("\$.name").value(someCompetition.name))
    }

    @Test
    fun `Create competition`() {

        val name = "competition_z"
        val newCompetition = Competition(name = name)

        var request = post(COMPETITION)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(newCompetition.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        val responseAsString = mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").isNotEmpty)
            .andExpect(jsonPath("\$.name").value(newCompetition.name))
            .andReturn().response.contentAsString

        val responseCompetition = objectMapper.readValue(responseAsString, Competition::class.java)

        request = get(COMPETITION + ID_PARAM, responseCompetition.id)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(responseCompetition.id))
            .andExpect(jsonPath("\$.name").value(newCompetition.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertCompetitions.sql"])
    fun `Update one competition`() {

        val id: Long = 100
        val newName = "competition_z"
        val updatedCompetition = Competition(id, newName)

        var request = put(COMPETITION + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(updatedCompetition.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedCompetition.id))
            .andExpect(jsonPath("\$.name").value(updatedCompetition.name))

        request = get(COMPETITION + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedCompetition.id))
            .andExpect(jsonPath("\$.name").value(updatedCompetition.name))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertCompetitions.sql"])
    fun `Delete one competition`() {

        val id: Long = 100
        val name = "competition_a"
        val someCompetition = Competition(id, name)

        var request = get(COMPETITION + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(someCompetition.id))
            .andExpect(jsonPath("\$.name").value(someCompetition.name))

        request = delete(COMPETITION + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)

        request = get(COMPETITION + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isNotFound)
    }
}
