package com.github.jntakpe.sbusers.model.entity

import com.github.jntakpe.commons.mongo.Identifiable
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document
data class User(
    val username: String,
    val email: String,
    val countryCode: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    override val id: ObjectId = ObjectId(),
) : Identifiable, Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }

    override fun toString(): String {
        return "User(username='$username')"
    }
}
