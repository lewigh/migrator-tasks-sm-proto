package com.lewigh.migratortasksrfb.engine

import com.lewigh.migratortasksrfb.engine.internal.*

interface TaskProcessor {

    val goal: TaskGoal

    fun process(current: CurrentTask, planner: TaskPlanner)

    fun rollback(current: CurrentTask) {
        // по умолчанию откат не предусмотрен
    }
}
