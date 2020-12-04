package com.github.jntakpe.sbusers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication(scanBasePackages = ["com.github.jntakpe"])
class SbUsersApplication

fun main(args: Array<String>) {
    runApplication<SbUsersApplication>(*args)
}
