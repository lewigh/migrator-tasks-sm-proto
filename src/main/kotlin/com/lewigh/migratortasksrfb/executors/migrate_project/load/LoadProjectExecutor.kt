package com.lewigh.migratortasksrfb.executors.migrate_project.load

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class LoadProjectExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Загрузка проекта 1")
        planner.stage(
            PlannedTask(goal = Goal.LOAD_PROJECT_SCHEMA, description = "Загрузка схемы 1 проекта"),
            PlannedTask(goal = Goal.LOAD_PROJECT_ACTORS, description = "Загрузка акторов проекта"),
            PlannedTask(goal = Goal.LOAD_PROJECT_COMMENTS, description = "Загрузка комментариев проекта")
        )
    }
}
