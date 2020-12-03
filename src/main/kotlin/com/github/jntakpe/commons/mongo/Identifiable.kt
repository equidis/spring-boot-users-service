package com.github.jntakpe.commons.mongo

import org.bson.types.ObjectId

interface Identifiable {

    val id: ObjectId
}
