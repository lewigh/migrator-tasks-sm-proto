package com.lewigh.migratortasksrfb.processor.example.migrate_project.fill.actors

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectActorsProcessor(override val goal: TaskGoal = TaskGoal.FILL_PROJECT_ACTORS) : TaskProcessor {

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(3_000)
    }
}
