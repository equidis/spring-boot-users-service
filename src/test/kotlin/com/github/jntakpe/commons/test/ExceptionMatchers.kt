package com.github.jntakpe.commons.test

import com.github.jntakpe.commons.context.CommonException
import com.github.jntakpe.commons.context.CommonExceptionDto
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import reactor.test.StepVerifier

fun <T> StepVerifier.Step<T>.expectStatusException(status: HttpStatus, message: String? = null): StepVerifier {
    return consumeErrorWith { it.assertStatusException(status, message) }
}

fun Throwable.assertStatusException(expectedStatus: HttpStatus, expectedMessage: String? = null) {
    assertThat(this).isInstanceOf(CommonException::class.java)
    this as CommonException
    assertThat(status).isEqualTo(expectedStatus)
    expectedMessage?.also { assertThat(message).isEqualTo(it) }
}

fun CommonExceptionDto.assertStatusException(expectedStatus: HttpStatus, expectedMessage: String? = null) {
    assertThat(code).isEqualTo(expectedStatus.value())
    expectedMessage?.also { assertThat(message).isEqualTo(it) }
}
