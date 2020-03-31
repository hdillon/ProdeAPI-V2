package com.solution.prode.queries

const val GET_USER_BY_ID: String = """
    SELECT *
    FROM users
    WHERE users.id = :id
    LIMIT 1;
"""
