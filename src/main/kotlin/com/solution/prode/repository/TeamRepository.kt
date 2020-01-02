package com.solution.prode.repository

import com.solution.prode.model.Team
import org.springframework.data.repository.CrudRepository

interface TeamRepository : CrudRepository<Team, Long> {

}