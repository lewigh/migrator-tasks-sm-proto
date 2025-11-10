package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoadProjectExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Загрузка проекта")
        planner.stage(
            PlannedTask(goal = Goal.LOAD_PROJECT_SCHEMA, description = "Загрузка схемы 1 проекта"),
            PlannedTask(goal = Goal.LOAD_PROJECT_ACTORS, description = "Загрузка акторов проекта"),
            PlannedTask(goal = Goal.LOAD_PROJECT_COMMENTS, description = "Загрузка комментариев проекта")
        )
    }
}
