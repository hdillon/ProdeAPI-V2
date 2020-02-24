package com.solution.prode.service

import com.solution.prode.model.RoleName
import com.solution.prode.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    fun findByName(role: RoleName) = roleRepository.findByName(role)
}