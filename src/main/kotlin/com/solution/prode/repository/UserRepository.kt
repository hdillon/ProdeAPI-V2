package com.solution.prode.repository

import com.solution.prode.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsernameOrEmail(username: String, email: String): User?

    fun findUserById(id: Long): User? = findById(id).orElse(null)
}