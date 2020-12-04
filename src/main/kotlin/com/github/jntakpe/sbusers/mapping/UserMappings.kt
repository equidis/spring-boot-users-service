package com.github.jntakpe.sbusers.mapping

import com.github.jntakpe.commons.context.CommonException
import com.github.jntakpe.commons.context.ScriptLogger
import com.github.jntakpe.sbusers.model.dto.UserDto
import com.github.jntakpe.sbusers.model.entity.User
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import java.util.*

private val log = ScriptLogger.log
private val isoCodes = Locale.getISOCountries().toList()

fun UserDto.toEntity() = User(
    username,
    email,
    countryCode.resolveCountry(),
    firstName,
    lastName,
    phoneNumber.removeWhitespaces(),
    id?.let { ObjectId(it) } ?: ObjectId()
)

fun User.toResponse() = UserDto(username, email, countryCode, firstName, lastName, phoneNumber, id.toString())

private fun String?.removeWhitespaces() = this?.filter { !it.isWhitespace() }

private fun String.resolveCountry(): String {
    return takeIf { isoCodes.contains(this) }
        ?: throw CommonException("Country field should an ISO3166-1 alpha-2 code", log::debug, HttpStatus.BAD_REQUEST)
}
