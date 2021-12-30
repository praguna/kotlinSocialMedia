package com.social.bloggerservice.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtTokenAuthenticationFilter( @Autowired
                                     private val tokenProvider: JwtTokenProvider
                                     ) : WebFilter {
    private val HEADER_PREFIX = "Bearer "

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = resolveToken(exchange.request)
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            val authentication: Authentication? = tokenProvider.getAuthentication(token)
            return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
        }
        return chain.filter(exchange)
    }

    private fun resolveToken(request: ServerHttpRequest): String? {
        val bearerToken: String? = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return if (StringUtils.hasText(bearerToken) && bearerToken?.startsWith(HEADER_PREFIX) == true) {
            bearerToken.substring(7)
        } else null
    }


}