package com.solution.prode.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Player(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    @JsonProperty("first_name")
    var firstName: String,

    @JsonProperty("last_name")
    var lastName: String,

    @JsonProperty("team_id")
    var teamId: Long

) {
    companion object {
        const val ENTITY_NAME = "Player"
        const val ID = "id"
    }
}

fun Player.toJson(): String = ObjectMapper().writeValueAsString(this)
