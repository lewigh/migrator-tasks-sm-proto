package com.lewigh.migratortasksrfb.processor.migrate_project.fill.comments

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectCommentsExecutor(override val goal: Goal = Goal.FILL_PROJECT_COMMENTS) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(5_000)
    }
}
