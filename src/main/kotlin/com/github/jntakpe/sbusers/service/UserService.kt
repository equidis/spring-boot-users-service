package com.github.jntakpe.sbusers.service

import com.github.jntakpe.commons.cache.RedisReactiveCache
import com.github.jntakpe.commons.context.CommonException
import com.github.jntakpe.commons.context.logger
import com.github.jntakpe.commons.mongo.insertError
import com.github.jntakpe.sbusers.model.entity.User
import com.github.jntakpe.sbusers.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class UserService(private val repository: UserRepository, cacheManager: RedisCacheManager) {

    private val log = logger()
    private val usersCache = RedisReactiveCache("users", cacheManager)

    fun findById(id: ObjectId): Mono<User> {
        return usersCache.orPutOnCacheMiss(id) { repository.findById(id) }
            .doOnSubscribe { log.debug("Searching user by id {}", id) }
            .doOnNext { log.debug("{} retrieved using it's id", it) }
            .switchIfEmpty(missingIdError(id).toMono())
    }

    fun findByUsername(username: String): Mono<User> {
        return usersCache.orPutOnCacheMiss(username) { repository.findByUsername(username) }
            .doOnSubscribe { log.debug("Searching user by username {}", username) }
            .doOnNext { log.debug("{} retrieved using it's username", it) }
            .switchIfEmpty(missingUsernameError(username).toMono())
    }

    fun create(user: User): Mono<User> {
        return repository.insert(user)
            .doOnSubscribe { log.debug("Creating {}", user) }
            .doOnNext { log.info("{} created", it) }
            .onErrorMap { it.insertError(user, log) }
            .doOnNext {
                usersCache.putAndForget(user.id, user)
                usersCache.putAndForget(user.username, user)
            }
    }

    private fun missingUsernameError(username: String) = CommonException("No user found for username $username", log::debug, NOT_FOUND)

    private fun missingIdError(id: ObjectId) = CommonException("No user found for id $id", log::debug, NOT_FOUND)
}
