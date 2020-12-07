package com.github.jntakpe.commons.context

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CommonException(
    override val message: String,
    val logging: (String, Throwable) -> Unit,
    status: HttpStatus,
    cause: Throwable? = null,
) : ResponseStatusException(status, message, cause)
