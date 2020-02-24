package com.solution.prode.repository

import com.solution.prode.model.Role
import com.solution.prode.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository  : JpaRepository<Role, Long> {

    fun findByName(roleName: RoleName): Role?
}