package com.solution.prode

import com.ninjasquad.springmockk.MockkBean
import com.solution.prode.model.Team
import com.solution.prode.model.toJson
import com.solution.prode.repository.TeamRepository
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.util.*

@WebMvcTest
class TeamControllerTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var repository: TeamRepository

    @Test
    fun `List all teams`() {
        val someTeam = Team(1, "Argentina")
        val otherTeam = Team(2, "Brasil")
        every { repository.findAll() } returns listOf(someTeam, otherTeam)
        mockMvc.perform(get("/api/v1/team/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.[0].id").value(someTeam.id))
                .andExpect(jsonPath("\$.[0].name").value(someTeam.name))
                .andExpect(jsonPath("\$.[1].id").value(otherTeam.id))
                .andExpect(jsonPath("\$.[1].name").value(otherTeam.name))
    }

    @Test
    fun `Retrieve one team`() {
        val id: Long = 1
        val someTeam = Team(id, "Argentina")
        every { repository.findById(id) } returns Optional.of(someTeam)
        mockMvc.perform(get("/api/v1/team/$id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").value(someTeam.id))
                .andExpect(jsonPath("\$.name").value(someTeam.name))
    }

    @Test
    fun `Create team`() {
        val someTeam = Team(1, "Argentina")
        every { repository.save(someTeam) } returns someTeam
        mockMvc.perform(post("/api/v1/team")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(someTeam.toJson())
                .characterEncoding("utf-8"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id").value(someTeam.id))
                .andExpect(jsonPath("\$.name").value(someTeam.name))
    }

    @Test
    fun `Update one team`() {
        val id: Long = 1
        val newName = "Uruguay"
        val someTeam = Team(id, "Argentina")
        val updatedTeam = Team(id, newName)
        every { repository.findById(id) } returns Optional.of(someTeam)
        every { repository.save(updatedTeam) } returns updatedTeam
        mockMvc.perform(put("/api/v1/team/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedTeam.toJson()))
                .andExpect(status().isOk)
    }

    @Test
    fun `Delete one team`() {
        val id: Long = 1
        val someTeam = Team(id, "Argentina")
        every { repository.findById(id) } returns Optional.of(someTeam)
        every { repository.delete(someTeam) } returns Unit
        mockMvc.perform(delete("/api/v1/team/$id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
    }
}