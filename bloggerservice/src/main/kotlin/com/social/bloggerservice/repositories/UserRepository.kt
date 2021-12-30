package com.social.bloggerservice.repositories

import com.social.bloggerservice.models.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, ObjectId>  {
    fun findByUsername(username: String?): Mono<User>
}