package com.lewigh.migratortasksrfb.processor.example.migrate_project.load.schema

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class LoadProjectSchemasProcessor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT_SCHEMA) : TaskProcessor {

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(1_000)
    }
}
