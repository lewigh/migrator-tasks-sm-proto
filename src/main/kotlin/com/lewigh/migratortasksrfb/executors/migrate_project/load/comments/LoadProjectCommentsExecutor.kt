package com.lewigh.migratortasksrfb.executors.migrate_project.load.comments

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class LoadProjectCommentsExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT_COMMENTS) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(5_000)
    }
}
