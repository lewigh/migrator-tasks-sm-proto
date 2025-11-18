package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.engine.*


data class CurrentTask(
    val title: String,
    val domainId: String?,
    val params: Map<String, Any>? = null
)

data class PlannedTask(
    val goal: TaskGoal,
    val title: String,
    val params: Map<String, Any>? = null,
    val domainId: String? = null
) {
    var stage: Int = 0
}

