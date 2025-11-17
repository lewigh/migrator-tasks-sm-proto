package com.lewigh.migratortasksrfb.processor.migrate_project.load

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import org.springframework.stereotype.Component

@Component
class LoadProjectExecutor(override val goal: Goal = Goal.LOAD_PROJECT) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = Goal.LOAD_PROJECT_SCHEMA, description = "Загрузка схем из Венера1"),
            PlannedTask(goal = Goal.LOAD_PROJECT_ACTORS, description = "Загрузка акторов из Венера1"),
            PlannedTask(goal = Goal.LOAD_PROJECT_COMMENTS, description = "Загрузка комментариев из Венера1")
        )
    }
}
