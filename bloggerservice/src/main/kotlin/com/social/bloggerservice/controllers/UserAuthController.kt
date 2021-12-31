package com.social.bloggerservice.controllers

import com.social.bloggerservice.models.*
import com.social.bloggerservice.security.JwtTokenProvider
import com.social.bloggerservice.services.UserService
import kotlinx.coroutines.reactive.awaitSingle
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class UserAuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val tokenProvider: JwtTokenProvider,
) {
    private val log = LoggerFactory.getLogger(UserAuthController::class.java)

    @PostMapping("/signup")
    suspend fun performSignup(@Valid @RequestBody signup: Signup): ResponseEntity<SignUpResponse> {
        log.info("request :: $signup")
        if (userService.findByUsername(signup.username) != null) {
            return ResponseEntity.badRequest().body(SignUpResponse("username already exists"))
        }
        if (userService.findByEmailId(signup.email) != null) {
            return ResponseEntity.badRequest().body(SignUpResponse("email already exists"))
        }
        val user = User(password = passwordEncoder.encode(signup.password), emailId = signup.email, username = signup.username, roles = listOf("ADMIN", "USER"))
        try {
            userService.save(user)
                ?: return ResponseEntity.internalServerError().body(SignUpResponse("something went wrong"))
        }
        catch (e: Exception){
            log.error(e.message)
            return ResponseEntity.internalServerError().body(SignUpResponse("something went wrong : ${e.message}"))
        }
        return ResponseEntity.ok().body(SignUpResponse("registration succeeded !!!"))
    }

    @PostMapping("/signin")
    suspend fun performSignIn(@Valid @RequestBody login: AuthenticationRequest) : ResponseEntity<SignInResponse>{
        log.info("request :: $login")
        return try {
            val token = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(login.username, login.password))
                    .map { tokenProvider.createToken(it) }
                    .awaitSingle()
            val httpHeaders = HttpHeaders()
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer $token")
            val user = userService.findByUsername(login.username)!!
            user.password = null.toString()
            val tokenBody = SignInResponse(user, token, "login successfull !!!")
            ResponseEntity<SignInResponse>(tokenBody, httpHeaders, HttpStatus.OK)
        }catch (e : Exception){
            log.error(e.message)
            ResponseEntity.internalServerError().body(SignInResponse(null, null,  "${e.message}"))
        }
    }

}