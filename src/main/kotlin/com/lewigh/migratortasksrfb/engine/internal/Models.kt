package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.engine.*


data class CurrentTaskInfo(
    val title: String,
    val contextId: String,
    val params: Map<String, Any>? = null
)

data class PlannedTask(
    val goal: TaskGoal,
    val title: String,
    val params: Map<String, Any>? = null,
) {
    var stage: Int = 0
}

