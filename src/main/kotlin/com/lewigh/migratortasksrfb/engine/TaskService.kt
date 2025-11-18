package com.lewigh.migratortasksrfb.engine

import com.lewigh.migratortasksrfb.TaskController.*
import com.lewigh.migratortasksrfb.engine.internal.*

interface TaskService {
    fun restartTaskById(id: Long)

    fun findAllTasks(): List<TaskDto>
}
