package com.social.bloggerservice.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "post")
data class Post(
    @Id
    val id: ObjectId = ObjectId.get(),
    val username: String,
    val title: String,
    val content: String,
    val createdTime: LocalDateTime = LocalDateTime.now()
)