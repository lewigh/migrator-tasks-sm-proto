package com.lewigh.migratortasksrfb.executors.migrate_project.fill

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class FillProjectExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Постановка задач на выгрузку проектов")

        planner.stage(
            PlannedTask(goal = Goal.FILL_PROJECT_SCHEMA, description = "Загрузка схемы 1 проекта"),
            PlannedTask(goal = Goal.FILL_PROJECT_ACTORS, description = "Загрузка акторов проекта"),
            PlannedTask(goal = Goal.FILL_PROJECT_COMMENTS, description = "Загрузка комментариев проекта")
        )
    }
}
