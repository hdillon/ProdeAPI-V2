package com.solution.prode.controller

import com.solution.prode.model.Prode
import com.solution.prode.model.toJson
import com.solution.prode.routes.ALL
import com.solution.prode.routes.ID_PARAM
import com.solution.prode.routes.PRODE
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

class ProdeControllerTests : BaseControllerTests() {

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertCompetitions.sql", "/db/seeds/insertProdes.sql"]
    )
    fun `List all prodes`() {

        val competitionIdOne = 100L
        val competitionIdTwo = 101L
        val competitionIdThree = 102L

        val idOne = 100L
        val nameOne = "prode_a"
        val idTwo = 101L
        val nameTwo = "prode_b"
        val idThree = 102L
        val nameThree = "prode_c"

        val request = get(PRODE + ALL)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(idOne))
            .andExpect(jsonPath("\$.[0].name").value(nameOne))
            .andExpect(jsonPath("\$.[0].competition_id").value(competitionIdOne))
            .andExpect(jsonPath("\$.[1].id").value(idTwo))
            .andExpect(jsonPath("\$.[1].name").value(nameTwo))
            .andExpect(jsonPath("\$.[1].competition_id").value(competitionIdTwo))
            .andExpect(jsonPath("\$.[2].id").value(idThree))
            .andExpect(jsonPath("\$.[2].name").value(nameThree))
            .andExpect(jsonPath("\$.[2].competition_id").value(competitionIdThree))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertCompetitions.sql", "/db/seeds/insertProdes.sql"]
    )
    fun `Retrieve one prode`() {

        val id: Long = 100
        val name = "prode_a"
        val competitionId = 100L

        val someProde = Prode(id, name, competitionId)

        val request = get(PRODE + ID_PARAM, id.toString())
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(someProde.id))
            .andExpect(jsonPath("\$.name").value(someProde.name))
            .andExpect(jsonPath("\$.competition_id").value(someProde.competitionId))
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = ["/db/seeds/insertCompetitions.sql"])
    fun `Create prode`() {

        val name = "prode_z"
        val competitionId = 100L
        val newProde = Prode(name = name, competitionId = competitionId)

        var request = post(PRODE)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(newProde.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        val responseAsString = mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").isNotEmpty)
            .andExpect(jsonPath("\$.name").value(newProde.name))
            .andExpect(jsonPath("\$.competition_id").value(newProde.competitionId))
            .andReturn().response.contentAsString

        val responseProde = objectMapper.readValue(responseAsString, Prode::class.java)

        request = get(PRODE + ID_PARAM, responseProde.id)
            .accept(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(responseProde.id))
            .andExpect(jsonPath("\$.name").value(responseProde.name))
            .andExpect(jsonPath("\$.competition_id").value(responseProde.competitionId))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertCompetitions.sql", "/db/seeds/insertProdes.sql"]
    )
    fun `Update one prode`() {

        val id: Long = 100
        val newName = "prode_z"
        val conpetitionId = 100L

        val updatedProde = Prode(id, newName, conpetitionId)

        var request = put(PRODE + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(updatedProde.toJson())
            .characterEncoding(Charsets.UTF_8.name())

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedProde.id))
            .andExpect(jsonPath("\$.name").value(updatedProde.name))
            .andExpect(jsonPath("\$.competition_id").value(updatedProde.competitionId))

        request = get(PRODE + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(updatedProde.id))
            .andExpect(jsonPath("\$.name").value(updatedProde.name))
            .andExpect(jsonPath("\$.competition_id").value(updatedProde.competitionId))
    }

    @Test
    @Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        value = ["/db/seeds/insertCompetitions.sql", "/db/seeds/insertProdes.sql"]
    )
    fun `Delete one prode`() {

        val id: Long = 100
        val name = "prode_a"
        val competitionId = 100L

        val someProde = Prode(id, name, competitionId)

        var request = get(PRODE + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(someProde.id))
            .andExpect(jsonPath("\$.name").value(someProde.name))
            .andExpect(jsonPath("\$.competition_id").value(someProde.competitionId))

        request = delete(PRODE + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isOk)

        request = get(PRODE + ID_PARAM, id.toString())
            .contentType(APPLICATION_JSON)

        mockMvc.perform(request)
            .andExpect(status().isNotFound)
    }
}
