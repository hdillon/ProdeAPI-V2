package com.solution.prode.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Prode(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    var name: String,

    @field:JsonProperty("competition_id")
    var competitionId: Long

) {
    companion object {
        const val ENTITY_NAME = "Prode"
        const val ID = "id"
    }
}

fun Prode.toJson(): String = ObjectMapper().writeValueAsString(this)
