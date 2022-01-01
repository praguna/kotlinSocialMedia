package com.social.bloggerservice.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "comment")
data class Comment (
    @Id
    val id: ObjectId = ObjectId.get(),
    val username: String,
    val content: String,
    val parentId: String,
    val createdTime: LocalDateTime = LocalDateTime.now()
)