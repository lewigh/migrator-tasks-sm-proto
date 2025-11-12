package com.lewigh.migratortasksrfb

import com.lewigh.migratortasksrfb.Task.*

interface TaskExecutor {

    val goal: Task.Goal

    fun execute(current: CurrentTask, planner: TaskPlanner)

    fun rollback(current: CurrentTask) {
    }
}

data class CurrentTask(val description: String)

data class PlannedTask(val goal: Task.Goal, val description: String)

private const val READY_TO_PENDING: Int = 0;

data class TaskPlanner(private val parent: Task, private var currentStage: Int = 0) {
    fun parralel(vararg tasks: PlannedTask): TaskPlanner {
        if (tasks.isEmpty()) {
            return this;
        }

        var plannedStatus = if (currentStage == READY_TO_PENDING) {
            Status.PENDING
        } else {
            Status.WAITING
        }

        for (task in tasks) {
            val newSubtask = Task(
                description = task.description,
                goal = task.goal,
                status = plannedStatus,
                stage = currentStage,
                parent = parent
            )
            parent.subtasks.add(newSubtask)
        }

        currentStage++
        return this;
    }
}


