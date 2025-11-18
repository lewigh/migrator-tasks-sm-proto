package com.lewigh.migratortasksrfb

import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*

class JsonComponent(private val objectMapper: ObjectMapper) {

    fun fromMap(map: Map<String, Any>): String {
        return objectMapper.writeValueAsString(map)
    }

    fun toMap(json: String): Map<String, Any> {
        return objectMapper.readValue(json, object : TypeReference<Map<String, Any>>() {})
    }
}
