package com.github.jntakpe.sbusers.service

import com.github.jntakpe.commons.test.expectCommonException
import com.github.jntakpe.sbusers.dao.UserDao
import com.github.jntakpe.sbusers.model.entity.User
import com.github.jntakpe.sbusers.repository.UserRepository
import com.mongodb.MongoWriteException
import com.mongodb.ServerAddress
import com.mongodb.WriteError
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonDocument
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.http.HttpStatus
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test

@SpringBootTest
internal class UserServiceTest(
    @Autowired private val service: UserService,
    @Autowired private val dao: UserDao,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val cacheManager: RedisCacheManager,
) {

    private val rawCache = cacheManager.getCache("users")!!

    @BeforeEach
    fun setup() {
        dao.init()
        rawCache.clear()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by id should return user`(user: User) {
        service.findById(user.id).test()
            .expectNext(user)
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by id should call repository since cache miss`(user: User) {
        val repoSpy = spyk(userRepository)
        UserService(repoSpy, cacheManager).findById(user.id).test()
            .expectNext(user)
            .then {
                verify { repoSpy.findById(user.id) }
                confirmVerified(repoSpy)
                assertThat(rawCache.get(user.id, User::class.java)).isNotNull.isEqualTo(user)
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by id should not call repository since retrieved from cache`(user: User) {
        rawCache.put(user.id, user)
        val repoSpy = spyk(userRepository)
        UserService(repoSpy, cacheManager).findById(user.id).test()
            .expectNext(user)
            .then {
                verify { repoSpy.findById(user.id) wasNot Called }
                confirmVerified(repoSpy)
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `find by id fail when user does not exists`(user: User) {
        service.findById(user.id).test()
            .expectCommonException(HttpStatus.NOT_FOUND)
            .verify()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by username should return user`(user: User) {
        service.findByUsername(user.username).test()
            .expectNext(user)
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by username should call repository since cache miss`(user: User) {
        val repoSpy = spyk(userRepository)
        UserService(repoSpy, cacheManager).findByUsername(user.username).test()
            .expectNext(user)
            .then {
                verify { repoSpy.findByUsername(user.username) }
                confirmVerified(repoSpy)
                assertThat(rawCache.get(user.username, User::class.java)).isNotNull.isEqualTo(user)
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `find by username should not call repository since retrieved from cache`(user: User) {
        rawCache.put(user.username, user)
        val repoSpy = spyk(userRepository)
        UserService(repoSpy, cacheManager).findByUsername(user.username).test()
            .expectNext(user)
            .then {
                verify { repoSpy.findByUsername(user.username) wasNot Called }
                confirmVerified(repoSpy)
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `find by username fail when user does not exists`(user: User) {
        service.findByUsername(user.username).test()
            .expectCommonException(HttpStatus.NOT_FOUND)
            .verify()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `create should return created document`(user: User) {
        service.create(user).test()
            .expectNext(user)
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.TransientData::class)
    fun `create should put item in cache`(user: User) {
        val retrieveWithId = { rawCache.get(user.id, User::class.java) }
        val retrieveWithUsername = { rawCache.get(user.username, User::class.java) }
        assertThat(retrieveWithId()).isNull()
        assertThat(retrieveWithUsername()).isNull()
        service.create(user).test()
            .expectNext(user)
            .then {
                assertThat(retrieveWithId()).isNotNull.isEqualTo(user)
                assertThat(retrieveWithUsername()).isNotNull.isEqualTo(user)
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @ArgumentsSource(UserDao.PersistedData::class)
    fun `create should fail with already exists code when integrity constraint violated`(user: User) {
        service.create(user).test()
            .expectCommonException(HttpStatus.CONFLICT)
            .verify()
    }

    @Test
    fun `create should fail with internal code when unexpected mongo exception occurs`() {
        val mockedRepository = mockkClass(UserRepository::class)
        val exception = MongoWriteException(WriteError(999, "", BsonDocument.parse("{}")), ServerAddress("localhost"))
        every { mockedRepository.insert(any<User>()) } returns exception.toMono()
        every { mockedRepository.insert(any<User>()) } returns exception.toMono()
        UserService(mockedRepository, mockkClass(RedisCacheManager::class, relaxed = true)).create(UserDao.TransientData.data().first())
            .test()
            .expectCommonException(HttpStatus.INTERNAL_SERVER_ERROR)
            .verify()
    }

    @Test
    fun `create should fail with internal code when exception differs from mongo exception`() {
        val mockedRepository = mockkClass(UserRepository::class)
        every { mockedRepository.insert(any<User>()) } returns NullPointerException("Oops").toMono()
        UserService(mockedRepository, mockkClass(RedisCacheManager::class, relaxed = true)).create(UserDao.TransientData.data().first())
            .test()
            .expectCommonException(HttpStatus.INTERNAL_SERVER_ERROR)
            .verify()
    }
}
