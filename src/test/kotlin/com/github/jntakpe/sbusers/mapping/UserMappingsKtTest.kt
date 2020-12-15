package com.github.jntakpe.sbusers.mapping

import com.github.jntakpe.commons.test.assertCommonException
import com.github.jntakpe.sbusers.dao.UserDao.PersistedData.JDOE_MAIL
import com.github.jntakpe.sbusers.dao.UserDao.PersistedData.JDOE_USERNAME
import com.github.jntakpe.sbusers.dao.UserDao.PersistedData.jdoe
import com.github.jntakpe.sbusers.dao.UserDao.PersistedData.mmoe
import com.github.jntakpe.sbusers.model.dto.UserDto
import com.github.jntakpe.sbusers.model.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.*

internal class UserMappingsKtTest {

    @Test
    fun `to entity should map partial request`() {
        val dto = UserDto(JDOE_USERNAME, JDOE_MAIL, Locale.FRANCE.country)
        val entity = dto.toEntity()
        val expected = User(JDOE_USERNAME, JDOE_MAIL, Locale.FRANCE.country)
        assertThat(entity).usingRecursiveComparison().ignoringFields(User::id.name).isEqualTo(expected)
    }

    @Test
    fun `to entity should map full request`() {
        val dto = UserDto(JDOE_USERNAME, JDOE_MAIL, Locale.FRANCE.country, jdoe.firstName, jdoe.lastName, jdoe.phoneNumber)
        val entity = dto.toEntity()
        assertThat(entity).usingRecursiveComparison().ignoringFields(User::id.name).isEqualTo(jdoe)
    }

    @Test
    fun `to entity should remove whitespace from phone number`() {
        val dto = UserDto(JDOE_USERNAME, JDOE_MAIL, Locale.FRANCE.country, phoneNumber = "+33 1 23 45 67 89")
        assertThat(dto.toEntity().phoneNumber).isEqualTo("+33123456789")
    }

    @Test
    fun `to entity should fail when country code not iso`() {
        val dto = UserDto(JDOE_USERNAME, JDOE_MAIL, "ZY")
        catchThrowable { dto.toEntity() }.assertCommonException(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `to dto should map partial`() {
        val expected = with(mmoe) { UserDto(username, email, countryCode, id = id.toString()) }
        assertThat(mmoe.toDto()).usingRecursiveComparison().isEqualTo(expected)
    }

    @Test
    fun `to dto should map full`() {
        val expected = with(jdoe) { UserDto(username, email, countryCode, firstName, lastName, phoneNumber, id.toString()) }
        assertThat(jdoe.toDto()).usingRecursiveComparison().isEqualTo(expected)
    }
}
