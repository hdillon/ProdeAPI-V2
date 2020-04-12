package com.solution.prode.queries

import org.springframework.stereotype.Component

@Component
class UserQueries {

    val selectById = """
    SELECT *
    FROM users
    WHERE users.id = ?
    LIMIT 1;
    """.trimIndent()
}
