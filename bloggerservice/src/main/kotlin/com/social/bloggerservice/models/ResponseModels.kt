package com.social.bloggerservice.models

data class SignUpResponse(
    val message : String
)

data class SignInResponse(
    val user: User?,
    val accessToken: String?,
    val message: String?
)

data class CommentResponse(
    val commentId: String,
    val username: String,
    val content: String,
    val parentId: String,
    val createdTime: String
)


data class PostResponse(
    val postId : String,
    val username: String,
    val title : String,
    val content: String,
    val createdTime: String
)


data class PostUpdateResponse(
    val postId : String
)