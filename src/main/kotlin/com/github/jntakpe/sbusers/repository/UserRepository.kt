package com.github.jntakpe.sbusers.repository

import com.github.jntakpe.commons.mongo.SbReactiveMongoRepository
import com.github.jntakpe.sbusers.model.entity.User
import reactor.core.publisher.Mono

interface UserRepository : SbReactiveMongoRepository<User> {

    fun findByUsername(username: String): Mono<User>
}
