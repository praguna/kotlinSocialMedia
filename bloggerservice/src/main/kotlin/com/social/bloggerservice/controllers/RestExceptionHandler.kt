package com.social.bloggerservice.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.validation.FieldError
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import java.util.function.Consumer

@Component
@Order(-2)
class RestExceptionHandler : WebExceptionHandler {
    private val objectMapper: ObjectMapper? = null
    private val log = LoggerFactory.getLogger(RestExceptionHandler::class.java)
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        if (ex is WebExchangeBindException) {
            val webExchangeBindException = ex
            log.debug("errors:" + webExchangeBindException.fieldErrors)
            val errors = Errors("validation_failure", "Validation failed.")
            webExchangeBindException.fieldErrors
                .forEach(Consumer { e: FieldError ->
                    errors.add(
                        e.field,
                        e.code,
                        e.defaultMessage
                    )
                })
            log.debug("handled errors::$errors")
            return try {
                exchange.response.statusCode = HttpStatus.UNPROCESSABLE_ENTITY
                exchange.response.headers.contentType = MediaType.APPLICATION_JSON
                val db = DefaultDataBufferFactory().wrap(objectMapper!!.writeValueAsBytes(errors))
                exchange.response.writeWith(Mono.just(db))
            } catch (e: JsonProcessingException) {
                exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                exchange.response.setComplete()
            }
        }
        return Mono.error(ex)
    }
}


internal data class Errors @JsonCreator constructor(private val code: String, private val message: String) {
    private val errors: MutableList<Error> = ArrayList()
    fun add(path: String?, code: String?, message: String?) {
        errors.add(Error(path, code, message))
    }
}


internal data class Error @JsonCreator constructor(
    private val path: String?,
    private val code: String?,
    private val message: String?
)