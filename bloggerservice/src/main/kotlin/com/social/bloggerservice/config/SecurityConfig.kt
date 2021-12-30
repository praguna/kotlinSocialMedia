package com.social.bloggerservice.config


import com.social.bloggerservice.repositories.UserRepository
import com.social.bloggerservice.security.JwtTokenAuthenticationFilter
import com.social.bloggerservice.security.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono


@Configuration
class SecurityConfig {
    @Bean
    fun springWebFilterChain(
        http: ServerHttpSecurity, tokenProvider: JwtTokenProvider,
        reactiveAuthenticationManager: ReactiveAuthenticationManager
    ): SecurityWebFilterChain {
        val pathPosts = "/posts/**"
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .authenticationManager(reactiveAuthenticationManager)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange {
                it
                    .pathMatchers(HttpMethod.GET, pathPosts).permitAll()
                    .pathMatchers(HttpMethod.DELETE, pathPosts).hasRole("ADMIN")
                    .pathMatchers(pathPosts).authenticated()
                    .pathMatchers("/me").authenticated()
                    .pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                    .anyExchange().permitAll()
            }
            .addFilterAt(JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
            .build()
    }

    private fun currentUserMatchesPath(
        authentication: Mono<Authentication>,
        context: AuthorizationContext
    ): Mono<AuthorizationDecision?> {
        return authentication
            .map { a: Authentication ->
                context.variables["user"] == a.name
            }
            .map { granted: Boolean? ->
                AuthorizationDecision(
                    granted!!
                )
            }
    }

    @Bean
    fun reactiveAuthenticationManager(
        userDetailsService: ReactiveUserDetailsService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
        authenticationManager.setPasswordEncoder(passwordEncoder)
        return authenticationManager
    }

    @Bean
    fun userDetailsService(users: UserRepository): ReactiveUserDetailsService {
        return ReactiveUserDetailsService { username: String? ->
            users.findByUsername(username)
                .map { u ->
                    User.withUsername(u.username)
                        .password(u.password)
                        .authorities(*u.roles.toTypedArray())
                        .accountExpired(!u.active)
                        .credentialsExpired(!u.active)
                        .disabled(!u.active)
                        .accountLocked(!u.active)
                        .build()
                }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
