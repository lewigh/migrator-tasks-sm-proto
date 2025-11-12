package com.lewigh.migratortasksrfb.executors.migrate_project.fill.schema

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class FillProjectSchemaExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECT_SCHEMA) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(8_000)
    }
}
