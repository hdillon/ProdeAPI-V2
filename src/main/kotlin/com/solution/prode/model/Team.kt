package com.solution.prode.model

import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Team(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1L,
        var name: String
) {
        companion object {
                const val ENTITY_NAME = "Team"
                const val ID = "id"
        }
}

fun Team.toJson(): String = ObjectMapper().writeValueAsString(this)