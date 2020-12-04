package com.github.jntakpe.commons.mongo.test

import org.testcontainers.containers.MongoDBContainer

object MongoContainer {

    const val MONGO_DOCKER_IMAGE = "mongo"
    const val MONGO_VERSION = "4.2.10"
    val instance: MongoDBContainer by lazy {
        MongoDBContainer("$MONGO_DOCKER_IMAGE:$MONGO_VERSION").apply { start() }
    }
}
