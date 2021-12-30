package com.social.bloggerservice.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "blogger.app")
class JwtProperties {

     lateinit var jwtSecret : String

    // validity in milliseconds
     var jwtExpirationMS by Delegates.notNull<Long>()
}