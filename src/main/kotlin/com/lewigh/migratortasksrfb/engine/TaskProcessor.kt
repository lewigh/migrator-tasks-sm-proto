package com.lewigh.migratortasksrfb.engine

import com.lewigh.migratortasksrfb.engine.internal.*

interface TaskProcessor {

    val goal: TaskGoal

    fun process(current: CurrentTaskInfo, planner: TaskPlanner)

    fun rollback(current: CurrentTaskInfo) {
        // по умолчанию откат не предусмотрен
    }
}
