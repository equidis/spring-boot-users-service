package com.github.jntakpe.sbusers.endpoint

import com.github.jntakpe.sbusers.mapping.toDto
import com.github.jntakpe.sbusers.mapping.toEntity
import com.github.jntakpe.sbusers.model.dto.UserDto
import com.github.jntakpe.sbusers.service.UserService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserEndpoint(private val service: UserService) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<UserDto> {
        return service.findById(ObjectId(id))
            .map { it.toDto() }
    }

    @GetMapping
    fun findByUsername(@RequestParam username: String): Mono<UserDto> {
        return service.findByUsername(username)
            .map { it.toDto() }
    }

    @PostMapping
    fun create(@RequestBody @Valid request: UserDto): Mono<UserDto> {
        return service.create(request.toEntity())
            .map { it.toDto() }
    }
}
