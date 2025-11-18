package com.lewigh.migratortasksrfb.processor.example.migrate_project.fill.schema

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectSchemasProcessor(override val goal: TaskGoal = TaskGoal.FILL_PROJECT_SCHEMA) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(2_000)
    }
}
