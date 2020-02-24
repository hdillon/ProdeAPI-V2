package com.solution.prode.manager

import com.solution.prode.constants.ErrorCode
import com.solution.prode.exception.InternalException
import com.solution.prode.model.Role
import com.solution.prode.model.RoleName
import com.solution.prode.model.User
import com.solution.prode.payload.request.LoginRequest
import com.solution.prode.payload.request.SignUpRequest
import com.solution.prode.payload.response.LoginResponse
import com.solution.prode.security.JwtProvider
import com.solution.prode.service.RoleService
import com.solution.prode.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserManager {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    fun signUp(signUpRequest: SignUpRequest): User {

        val user = User()
        user.email = signUpRequest.email
        user.name = signUpRequest.name
        user.username = signUpRequest.username
        user.password = (passwordEncoder.encode(signUpRequest.password))

        val userRole: Role = roleService.findByName(RoleName.ROLE_USER)
                ?: throw InternalException(ErrorCode.INTERNAL_ERROR.value, "User Role not exists.")

        user.roles = setOf(userRole)

        return userService.save(user)
    }

    fun login(loginRequest: LoginRequest): LoginResponse {

        val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail,
                        loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val token: String = jwtProvider.generateToken(authentication)

        return LoginResponse(token)
    }
}