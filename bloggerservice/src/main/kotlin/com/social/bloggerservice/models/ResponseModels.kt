package com.social.bloggerservice.models

data class SignUpResponse(
    val message : String
)

data class SignInResponse(
    val user: User?,
    val accessToken: String?,
    val message: String?
)