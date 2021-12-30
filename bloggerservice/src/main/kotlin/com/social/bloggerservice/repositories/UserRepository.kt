package com.social.bloggerservice.repositories

import com.social.bloggerservice.models.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User, ObjectId>  {
}