package com.lewigh.migratortasksrfb.processor.example.migrate_project.load.comments

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class LoadProjectCommentsProcessor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT_COMMENTS) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {

        //throw RuntimeException("Венера не отвечает!")
        //Thread.sleep(5_000)
    }
}
