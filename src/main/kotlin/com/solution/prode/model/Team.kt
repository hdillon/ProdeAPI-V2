package com.solution.prode.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Team(
        @Id
        var id: String,
        var name: String
)