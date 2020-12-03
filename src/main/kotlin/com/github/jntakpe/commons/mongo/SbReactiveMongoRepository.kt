package com.github.jntakpe.commons.mongo

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface SbReactiveMongoRepository<T : Identifiable> : ReactiveMongoRepository<T, ObjectId>
