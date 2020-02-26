package com.solution.prode.payload.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignUpRequest (

    @NotBlank
    @Size(min = 4, max = 40)
    var name: String? = null,

    @NotBlank
    @Size(min = 3, max = 15)
    var username: String? = null,

    @NotBlank
    @Size(max = 40)
    @Email
    var email: String,

    @NotBlank
    @Size(min = 6, max = 20)
    var password: String
)