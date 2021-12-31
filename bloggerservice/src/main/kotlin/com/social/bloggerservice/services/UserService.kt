package com.social.bloggerservice.services

import com.social.bloggerservice.models.User
import com.social.bloggerservice.repositories.UserRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository){

    suspend fun findByUsername(username: String): User? {
        return repository.findByUsername(username).awaitSingleOrNull()
    }

    suspend fun findByEmailId(emailId: String): User? {
        return repository.findByEmailId(emailId).awaitSingleOrNull()
    }

    suspend fun save(user: User) : User?{
        return repository.save(user).awaitFirstOrNull()
    }
}