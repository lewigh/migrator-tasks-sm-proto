package com.lewigh.migratortasksrfb.engine.internal

import com.fasterxml.jackson.databind.*
import io.github.oshai.kotlinlogging.*

data class TaskPlanner(private val plannedTasks: MutableList<PlannedTask>, private var currentStage: Int = 0) {

    private val logger = KotlinLogging.logger {}

    fun parralel(vararg tasks: PlannedTask): TaskPlanner {
        if (tasks.isEmpty()) {
            return this;
        }

        for (task in tasks) {
            task.stage = currentStage
            plannedTasks.add(task)
        }

        currentStage++
        return this;
    }
}
