package com.github.jntakpe.commons.context

import org.springframework.http.HttpStatus

class CommonException(
    override val message: String,
    val logging: (String, Throwable) -> Unit,
    val code: HttpStatus,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
