package com.social.bloggerservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class BloggerserviceApplication

fun main(args: Array<String>) {
	runApplication<BloggerserviceApplication>(*args)
}
