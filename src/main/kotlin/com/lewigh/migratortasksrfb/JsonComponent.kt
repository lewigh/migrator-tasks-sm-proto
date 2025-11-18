package com.lewigh.migratortasksrfb

import com.fasterxml.jackson.core.type.*

interface JsonComponent {

    fun fromMap(map: Map<String, Any>): String

    fun toMap(json: String): Map<String, Any>
}
