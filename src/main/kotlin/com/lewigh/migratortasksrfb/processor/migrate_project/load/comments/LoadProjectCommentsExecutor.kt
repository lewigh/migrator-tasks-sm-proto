package com.lewigh.migratortasksrfb.processor.migrate_project.load.comments

import com.fasterxml.jackson.databind.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.processor.migrate_project.*
import org.springframework.stereotype.Component

@Component
class LoadProjectCommentsExecutor(override val goal: Goal = Goal.LOAD_PROJECT_COMMENTS) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {

        //throw RuntimeException("Венера не отвечает!")
        //Thread.sleep(5_000)
    }
}
