package com.github.jntakpe.commons.mongo.test

import com.github.jntakpe.commons.mongo.Identifiable
import com.github.jntakpe.commons.mongo.SbReactiveMongoRepository
import com.github.jntakpe.commons.test.TestDataProvider
import reactor.core.publisher.Mono

abstract class MongoDao<T : Identifiable>(
    private val repository: SbReactiveMongoRepository<T>,
    private val dataProvider: TestDataProvider<T>,
) {

    fun init() = dataProvider.data().toList().also { clean().thenMany(repository.saveAll(it)).blockLast() }

    fun clean(): Mono<Void> = repository.deleteAll()

    fun count() = repository.count().block() ?: 0
}
