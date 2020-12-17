package com.github.jntakpe.sbusers.dao

import com.github.jntakpe.commons.mongo.test.MongoDao
import com.github.jntakpe.commons.test.TestDataProvider
import com.github.jntakpe.sbusers.model.entity.User
import com.github.jntakpe.sbusers.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDao(@Autowired repository: UserRepository) : MongoDao<User>(repository, PersistedData) {

    object PersistedData : TestDataProvider<User> {

        const val JDOE_ID = "5fdb5cdad07bba25f645cd87"
        const val JDOE_USERNAME = "jdoe"
        const val JDOE_MAIL = "jdoe@mail.com"
        const val MMOE_USERNAME = "mmoe"
        const val MMOE_MAIL = "mmoe@mail.com"
        val jdoe = User(JDOE_USERNAME, JDOE_MAIL, Locale.FRANCE.country, "John", "Doe", "+33123456789", ObjectId(JDOE_ID))
        val mmoe = User(MMOE_USERNAME, MMOE_MAIL, Locale.UK.country)

        override fun data() = listOf(jdoe, mmoe)
    }

    object TransientData : TestDataProvider<User> {

        const val RROE_USERNAME = "rroe"
        const val RROE_MAIL = "rroe@mail.com"
        const val JOHN_SMITH = "jsmith"
        const val JOHN_MAIL = "jsmith@mail.com"
        val rroe = User(RROE_USERNAME, RROE_MAIL, Locale.FRANCE.country, "Richard", "Roe", "+339877654321")
        val jsmith = User(JOHN_SMITH, JOHN_MAIL, Locale.UK.country)

        override fun data() = listOf(rroe, jsmith)
    }
}
