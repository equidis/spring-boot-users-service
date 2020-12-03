package com.github.jntakpe.commons.mongo

import com.github.jntakpe.commons.context.CommonException
import org.slf4j.Logger
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

fun <T : Identifiable> Throwable.insertError(entity: T, log: Logger): CommonException {
    return if (isDuplicateKey()) {
        CommonException("$entity already exists", log::info, HttpStatus.CONFLICT, this)
    } else {
        CommonException("Unable to store $entity", log::warn, HttpStatus.INTERNAL_SERVER_ERROR, this)
    }
}

private fun Throwable.isDuplicateKey() = this is DataIntegrityViolationException
