package com.lewigh.migratortasksrfb.executors.migrate_project.load

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class LoadProjectExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = Goal.LOAD_PROJECT_SCHEMA, description = "Загрузка схем из Венера1"),
            PlannedTask(goal = Goal.LOAD_PROJECT_ACTORS, description = "Загрузка акторов из Венера1"),
            PlannedTask(goal = Goal.LOAD_PROJECT_COMMENTS, description = "Загрузка комментариев из Венера1")
        )
    }
}
