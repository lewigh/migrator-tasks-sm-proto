package com.lewigh.migratortasksrfb

import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import org.springframework.stereotype.*

@Component
class JsonComponentImpl(private val objectMapper: ObjectMapper) : JsonComponent {

    override fun fromMap(map: Map<String, Any>): String {
        return objectMapper.writeValueAsString(map)
    }

    override fun toMap(json: String): Map<String, Any> {
        return objectMapper.readValue(json, object : TypeReference<Map<String, Any>>() {})
    }
}
