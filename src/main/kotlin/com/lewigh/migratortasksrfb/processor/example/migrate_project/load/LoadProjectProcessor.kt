package com.lewigh.migratortasksrfb.processor.example.migrate_project.load

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class LoadProjectProcessor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT) : TaskProcessor {

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = TaskGoal.LOAD_PROJECT_SCHEMA, title = "Загрузка схем из Венера1"),
            PlannedTask(goal = TaskGoal.LOAD_PROJECT_ACTORS, title = "Загрузка акторов из Венера1"),
            PlannedTask(goal = TaskGoal.LOAD_PROJECT_COMMENTS, title = "Загрузка комментариев из Венера1")
        )
    }
}
