package com.github.jntakpe.sbusers.model.dto

data class UserDto(
    val username: String,
    val email: String,
    val countryCode: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val id: String? = null,
)
