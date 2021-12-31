package com.social.bloggerservice.controllers

import com.social.bloggerservice.models.AuthenticationRequest
import com.social.bloggerservice.repositories.UserRepository
import com.social.bloggerservice.security.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val tokenProvider: JwtTokenProvider,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/token")
    fun login(@RequestBody authRequest: @Valid Mono<AuthenticationRequest>): Mono<ResponseEntity<*>> {
        return authRequest
            .flatMap { login: AuthenticationRequest ->
                authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken(login.username, login.password))
                    .map { auth: Authentication ->
                        tokenProvider.createToken(auth)
                    }
            }
            .doOnError { println("${it.message}") }
            .map { jwt: String ->
                val httpHeaders = HttpHeaders()
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                val tokenBody = mutableMapOf("id_token" to jwt)
                return@map ResponseEntity<Any?>(tokenBody, httpHeaders, HttpStatus.OK)
            }
    }

    @PostMapping("/test")
    fun abc(@RequestBody authRequest: @Valid AuthenticationRequest): Mono<String> {
        return repository.findByUsername(authRequest.username).map { passwordEncoder.encode(it.password) }
    }


    @GetMapping("/me")
    fun current(@AuthenticationPrincipal principal: User): Mono<Map<String, Any>> {
        return Mono.just(mutableMapOf<String,Any>(
                "name" to principal.username,
                "roles" to AuthorityUtils.authorityListToSet(principal.authorities)
            ))
    }

    @GetMapping("/ping")
    suspend fun pingPong() : String{
        return "pong"
    }
}