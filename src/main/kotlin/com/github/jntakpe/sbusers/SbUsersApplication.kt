package com.github.jntakpe.sbusers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class SbUsersApplication

fun main(args: Array<String>) {
    runApplication<SbUsersApplication>(*args)
}
