package de.ddkfm.SticketStorage.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val objectMapper = jacksonObjectMapper()

fun Any.toJson() : String {
    return objectMapper.writeValueAsString(this)
}