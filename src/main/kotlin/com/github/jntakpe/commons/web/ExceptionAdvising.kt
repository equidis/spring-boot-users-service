package com.github.jntakpe.commons.web

import com.github.jntakpe.commons.context.CommonException
import com.github.jntakpe.commons.context.CommonExceptionDto
import com.github.jntakpe.commons.context.logger
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice(basePackages = ["com.github.jntakpe"])
class ExceptionAdvising {

    private val log = logger()

    @ExceptionHandler(CommonException::class)
    fun handle(exception: CommonException): ResponseEntity<CommonExceptionDto> {
        exception.logging(exception.message, exception)
        return ResponseEntity(CommonExceptionDto(exception.message, exception.status.value()), exception.status)
    }

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<CommonExceptionDto> {
        log.warn("An unexpected error occurred", exception)
        val body = CommonExceptionDto(exception.message ?: "Internal error", INTERNAL_SERVER_ERROR.value())
        return ResponseEntity(body, INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ServerWebInputException::class)
    fun handle(exception: ServerWebInputException): ResponseEntity<CommonExceptionDto> {
        log.info(exception.message, exception)
        return ResponseEntity(CommonExceptionDto(exception.message, HttpStatus.BAD_REQUEST.value()), exception.status)
    }
}
