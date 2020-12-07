package com.github.jntakpe.sbusers.model.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

data class UserDto(
    @field:Length(min = 3) val username: String,
    @field:Email val email: String,
    @field:Length(min = 2, max = 2) val countryCode: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val id: String? = null,
)
