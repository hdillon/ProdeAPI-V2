package com.solution.prode.security

import com.solution.prode.model.User
import com.solution.prode.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {

        val user: User? = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)

        return UserDetailsImpl.create(user)
    }

    @Transactional
    fun loadUserById(id: Long): UserDetails {

        val user: User? = userRepository.findUserById(id)

        return UserDetailsImpl.create(user)
    }
}