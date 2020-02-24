package com.solution.prode.service

import com.solution.prode.model.User
import com.solution.prode.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun save(user: User) = userRepository.save(user)
}