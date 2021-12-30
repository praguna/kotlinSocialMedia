package com.social.bloggerservice.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors.joining
import javax.crypto.SecretKey


@Component
class JwtTokenProvider(
    @Autowired
    private val jwtProperties: JwtProperties
){
    private val AUTHORITIES_KEY = "roles"
    private var secretKey: SecretKey? = null
    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    init {
        val secret = Base64.getEncoder().encodeToString(jwtProperties.jwtSecret.encodeToByteArray())
        secretKey = Keys.hmacShaKeyFor(secret.encodeToByteArray())
    }

    fun createToken(authentication: Authentication): String {
        val username: String = authentication.name
        val authorities: Collection<GrantedAuthority> = authentication.authorities
        val claims = Jwts.claims().setSubject(username)
        if (!authorities.isEmpty()) {
            claims[AUTHORITIES_KEY] = authorities.stream().map { obj: GrantedAuthority -> obj.authority }
                .collect(joining(","))
        }
        val now = Date()
        val validity = Date(now.time + jwtProperties.jwtExpirationMS)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication? {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        val authoritiesClaim = claims[AUTHORITIES_KEY]
        val authorities: Collection<GrantedAuthority?> =
            if (authoritiesClaim == null) AuthorityUtils.NO_AUTHORITIES else AuthorityUtils.commaSeparatedStringToAuthorityList(
                authoritiesClaim.toString()
            )
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            val claims = Jwts
                .parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token)

            log.info("expiration date: {}", claims.body.expiration)
            return true
        } catch (e: JwtException) {
            log.info("Invalid JWT token: {}", e.message)
            log.trace("Invalid JWT token trace.", e)
        } catch (e: IllegalArgumentException) {
            log.info("Invalid JWT token: {}", e.message)
            log.trace("Invalid JWT token trace.", e)
        }
        return false
    }
}