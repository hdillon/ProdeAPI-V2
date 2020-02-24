package com.solution.prode.controller

import com.solution.prode.manager.UserManager
import com.solution.prode.model.User
import com.solution.prode.payload.request.LoginRequest
import com.solution.prode.payload.request.SignUpRequest
import com.solution.prode.payload.response.LoginResponse
import com.solution.prode.routes.AUTH
import com.solution.prode.routes.SIGNUP
import com.solution.prode.routes.LOGIN
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(AUTH)
class AuthController {

    @Autowired
    private lateinit var userManager: UserManager

    @PostMapping(SIGNUP)
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): User = userManager.signUp(signUpRequest)


    @PostMapping(LOGIN)
    fun login(@Valid @RequestBody loginRequest: LoginRequest): LoginResponse = userManager.login(loginRequest)
}