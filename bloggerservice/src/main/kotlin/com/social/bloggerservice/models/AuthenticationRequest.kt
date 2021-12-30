package com.social.bloggerservice.models

import javax.validation.constraints.NotBlank

data class AuthenticationRequest(
     val username: @NotBlank String,
     val password: @NotBlank String,
)
