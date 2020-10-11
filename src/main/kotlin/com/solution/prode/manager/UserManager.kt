package com.solution.prode.manager

import com.solution.prode.constants.ErrorCodes.USER_ALREADY_EXISTS
import com.solution.prode.exception.BadRequestException
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
import com.solution.prode.util.MessageTranslator
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

    @Autowired
    private lateinit var messageTranslator: MessageTranslator

    fun signUp(signUpRequest: SignUpRequest): User {

        validateUserNotExists(signUpRequest.username, signUpRequest.email)

        val userRole: Role = roleService.findByName(RoleName.ROLE_USER)
            ?: throw InternalException("User Role not exists")

        val user = User().apply {
            email = signUpRequest.email
            name = signUpRequest.name
            roles = setOf(userRole)
            username = signUpRequest.username
            password = (passwordEncoder.encode(signUpRequest.password))
        }

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

    private fun validateUserNotExists(username: String?, email: String) {

        val user = userService.findByUserNameOrEmail(username, email)

        user?.let { throw BadRequestException(messageTranslator.getMessage(USER_ALREADY_EXISTS)) }
    }
}
