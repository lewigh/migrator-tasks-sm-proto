package com.lewigh.migratortasksrfb.processor.migrate_project.load.schema

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class LoadProjectSchemaExecutor(override val goal: Goal = Goal.LOAD_PROJECT_SCHEMA) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(1_000)
    }
}
