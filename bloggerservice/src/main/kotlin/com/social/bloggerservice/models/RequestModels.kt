package com.social.bloggerservice.models

import org.bson.types.ObjectId

data class Signup(
    val username : String,
    val password : String,
    val email : String
)

data class SavePost(
    val postId : String,
    val username: String,
    val title : String,
    val content: String,
    val userId: String
)

data class SaveComment(
    val parentId : String,
    val username: String,
    val content : String
)