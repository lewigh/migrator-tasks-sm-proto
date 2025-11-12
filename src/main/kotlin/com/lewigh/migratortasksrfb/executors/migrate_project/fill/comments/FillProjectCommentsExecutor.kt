package com.lewigh.migratortasksrfb.executors.migrate_project.fill.comments

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class FillProjectCommentsExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECT_COMMENTS) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(5_000)
    }
}
