package com.social.bloggerservice.repositories

import com.social.bloggerservice.models.Post
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostRepository : ReactiveMongoRepository<Post, ObjectId> {
    fun findByUsernameOrderByCreatedTimeDesc(username : String, pageable : Pageable) : Flux<Post>

    fun findByUsernameNotOrderByCreatedTimeDesc(username : String, pageable : Pageable) : Flux<Post>

    fun countByUsername(username : String) : Mono<Long>

    fun countByUsernameNot(username : String) : Mono<Long>


}