package com.solution.prode.payload.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LoginRequest (

    @NotBlank
    @Size(max = 40)
    @Email
    @JsonProperty("username_or_email")
    var usernameOrEmail: String? = null,

    @NotBlank
    @Size(min = 6, max = 20)
    var password: String? = null
)