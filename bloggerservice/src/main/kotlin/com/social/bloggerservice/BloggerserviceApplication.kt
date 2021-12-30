package com.social.bloggerservice

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import reactor.core.publisher.Hooks

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableAutoConfiguration
class BloggerserviceApplication

fun main(args: Array<String>) {
	Hooks.onOperatorDebug();
	runApplication<BloggerserviceApplication>(*args)
}
