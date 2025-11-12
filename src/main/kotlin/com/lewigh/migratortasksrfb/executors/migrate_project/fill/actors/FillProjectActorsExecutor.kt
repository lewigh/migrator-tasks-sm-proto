package com.lewigh.migratortasksrfb.executors.migrate_project.fill.actors

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class FillProjectActorsExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECT_ACTORS) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(3_000)
    }
}
