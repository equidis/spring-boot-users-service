package com.github.jntakpe.commons.mongo.test

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class MongoContainerConfig {

    @Bean
    @Primary
    fun mongoContainerClient(settings: MongoClientSettings): MongoClient {
        val container = MongoContainer.instance
        val containerSettings = MongoClientSettings.builder(settings)
            .applyConnectionString(ConnectionString("mongodb://${container.containerIpAddress}:${container.firstMappedPort}")).build()
        return MongoClients.create(containerSettings)
    }
}
