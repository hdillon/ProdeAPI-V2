package com.solution.prode.model

import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Team(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        var name: String
)

fun Team.toJson(): String = ObjectMapper().writeValueAsString(this)