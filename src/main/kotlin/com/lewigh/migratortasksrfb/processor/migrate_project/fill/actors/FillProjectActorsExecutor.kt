package com.lewigh.migratortasksrfb.processor.migrate_project.fill.actors

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectActorsExecutor(override val goal: Goal = Goal.FILL_PROJECT_ACTORS) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(3_000)
    }
}
