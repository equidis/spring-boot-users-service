package com.github.jntakpe.sbusers.config

import com.github.jntakpe.sbusers.model.entity.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.Index
import org.springframework.data.mongodb.core.indexOps
import javax.annotation.PostConstruct

@Configuration
class MongoConfig(private val mongoTemplate: ReactiveMongoTemplate) {

    @PostConstruct
    fun createIndexes() {
        mongoTemplate.indexOps<User>().ensureIndex(Index(User::username.name, Sort.Direction.ASC).unique()).subscribe()
        mongoTemplate.indexOps<User>().ensureIndex(Index(User::email.name, Sort.Direction.ASC).unique()).subscribe()
    }
}
