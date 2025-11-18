package com.lewigh.migratortasksrfb.processor.example.migrate_project.load.comments

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class LoadProjectCommentsProcessor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT_COMMENTS) : TaskProcessor {

    private var failed: Boolean = false

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {

        if (!failed) {
            failed = true;
            throw RuntimeException("Венера не отвечает!")
        }
        failed = false
        //Thread.sleep(5_000)
    }
}
