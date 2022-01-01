package com.social.bloggerservice.repositories

import com.social.bloggerservice.models.Comment
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CommentRepository : ReactiveMongoRepository<Comment , ObjectId> {
    fun findAllByParentIdOrderByCreatedTimeDesc(parentId : String) : Flux<Comment>

    fun deleteAllByParentId(parentId: String) : Mono<Void>
}