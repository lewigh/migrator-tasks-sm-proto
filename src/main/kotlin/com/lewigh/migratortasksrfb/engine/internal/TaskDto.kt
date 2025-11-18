package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.engine.*

data class TaskDto(
    var id: Long,
    var title: String,
    var status: Task.Status,
    var goal: TaskGoal,
    var parentId: Long?,
    var contextId: String,
    var stage: Int,
    var executed: Boolean,
    var params: Map<String, Any>?,
    var error: String?,
    var subtasks: MutableList<TaskDto>,
)
