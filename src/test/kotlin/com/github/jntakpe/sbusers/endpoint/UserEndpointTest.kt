package com.github.jntakpe.sbusers.endpoint

import com.github.jntakpe.commons.context.CommonExceptionDto
import com.github.jntakpe.commons.test.assertStatusException
import com.github.jntakpe.sbusers.dao.UserDao
import com.github.jntakpe.sbusers.mapping.toDto
import com.github.jntakpe.sbusers.model.dto.UserDto
import com.github.jntakpe.sbusers.model.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
@AutoConfigureWebTestClient
internal class UserEndpointTest(
    @Autowired private val dao: UserDao,
    @Autowired private val client: WebTestClient,
    @Autowired private val cacheManager: RedisCacheManager,
) {

    private val rawCache = cacheManager.getCache("users")!!
    private val usersPath = "/users"

    @BeforeEach
    fun setup() {
        dao.init()
        rawCache.clear()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by id should return ok response`(user: User) {
        client.get()
            .uri("$usersPath/{id}", user.id.toString())
            .exchange()
            .expectBody<UserDto>()
            .consumeWith { assertThat(it.responseBody?.id).isNotEmpty.isEqualTo(user.id.toString()) }
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `find by id should fail when user does not exist`(user: User) {
        client.get()
            .uri("$usersPath/{id}", user.id.toString())
            .exchange()
            .expectBody<CommonExceptionDto>()
            .consumeWith { it.responseBody!!.assertStatusException(HttpStatus.NOT_FOUND) }
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by username should return ok response`(user: User) {
        client.get()
            .uri { it.path(usersPath).queryParam("username", user.username).build() }
            .exchange()
            .expectBody<UserDto>()
            .consumeWith {
                val response = it.responseBody!!
                assertThat(response.id).isNotNull
                assertThat(response.username).isEqualTo(user.username)
            }
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `find by username should fail when user does not exist`(user: User) {
        client.get()
            .uri { it.path(usersPath).queryParam("username", user.username).build() }
            .exchange()
            .expectBody<CommonExceptionDto>()
            .consumeWith { it.responseBody!!.assertStatusException(HttpStatus.NOT_FOUND) }
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `create should return ok response`(user: User) {
        val initSize = dao.count()
        client.post()
            .uri(usersPath)
            .bodyValue(user.toDto())
            .exchange()
            .expectBody<UserDto>()
            .consumeWith {
                assertThat(it.responseBody!!.id).isNotNull
                assertThat(dao.count()).isEqualTo(initSize + 1)
            }
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `create should fail when user already exists`(user: User) {
        val initSize = dao.count()
        client.post()
            .uri(usersPath)
            .bodyValue(user.toDto())
            .exchange()
            .expectBody<CommonExceptionDto>()
            .consumeWith {
                it.responseBody!!.assertStatusException(HttpStatus.CONFLICT)
                assertThat(dao.count()).isEqualTo(initSize)
            }
    }

    @Test
    fun `create should fail when missing username`() {
        client.post()
            .uri(usersPath)
            .bodyValue(User("", "jdoe@mail.com", "FR"))
            .exchange()
            .expectBody<CommonExceptionDto>()
            .consumeWith { it.responseBody!!.assertStatusException(HttpStatus.BAD_REQUEST) }
    }

    @Test
    fun `create should fail when invalid email`() {
        client.post()
            .uri(usersPath)
            .bodyValue(User("invalid", "wrong.mail", "FR"))
            .exchange()
            .expectBody<CommonExceptionDto>()
            .consumeWith { it.responseBody!!.assertStatusException(HttpStatus.BAD_REQUEST) }
    }
}
