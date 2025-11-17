package com.lewigh.migratortasksrfb.engine

import com.lewigh.migratortasksrfb.engine.internal.*

interface TaskProcessor {

    val goal: Goal

    fun process(current: CurrentTask, planner: TaskPlanner)

    fun rollback(current: CurrentTask) {
    }
}
