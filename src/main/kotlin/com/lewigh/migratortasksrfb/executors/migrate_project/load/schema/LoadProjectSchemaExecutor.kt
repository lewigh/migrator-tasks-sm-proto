package com.lewigh.migratortasksrfb.executors.migrate_project.load.schema

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class LoadProjectSchemaExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT_SCHEMA) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(1_000)
    }
}
