package com.lewigh.migratortasksrfb.engine.internal

import io.github.oshai.kotlinlogging.*

private const val READY_TO_PENDING: Int = 0;

data class TaskPlanner(private val parent: Task, private var currentStage: Int = 0) {

    private val logger = KotlinLogging.logger {}

    fun parralel(vararg tasks: PlannedTask): TaskPlanner {
        if (tasks.isEmpty()) {
            return this;
        }

        var plannedStatus = if (currentStage == READY_TO_PENDING) {
            Task.Status.PENDING
        } else {
            Task.Status.WAITING
        }

        for (task in tasks) {

            val domainId = task.domainId ?: parent.domainId

            val newSubtask = Task(
                description = task.description,
                goal = task.goal,
                status = plannedStatus,
                stage = currentStage,
                parent = parent,
                domainId = domainId,
                params = task.parameters
            )
            parent.subtasks.add(newSubtask)

            validate(task, newSubtask)
        }

        currentStage++
        return this;
    }

    private fun validate(task: PlannedTask, newSubtask: Task) {
        if (parent.goal == task.goal) {
            val message = "Couldn't plan child task with the same goal as parent. Parent: ${parent.goal} child $task"
            logger.error { message }
            newSubtask.error(message)
        }
    }
}
