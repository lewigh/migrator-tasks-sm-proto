package com.lewigh.migratortasksrfb.processor.example.migrate_project.fill.comments

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectCommentsProcessor(override val goal: TaskGoal = TaskGoal.FILL_PROJECT_COMMENTS) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        //throw RuntimeException("AAA")
        Thread.sleep(5_000)
    }
}
