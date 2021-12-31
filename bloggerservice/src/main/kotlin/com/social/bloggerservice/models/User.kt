package com.social.bloggerservice.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Document(collection = "user")
data class User(
    @Id
    val id : ObjectId  = ObjectId.get(),
    @NotBlank
    @Size(max = 20)
    var password : String,
    @NotBlank
    @Size(max = 20)
    val username : String,
    @NotBlank
    @Email
    val emailId : String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
    val description : String? = null,
    val imgAvatar : String? = null,
    val active: Boolean = true,
    val roles: List<String> = arrayListOf()
)