package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class MigrationExecutor(override val goal: Task.Goal = Task.Goal.MIGRATION) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Постановка основных задач")

        planner
            .stage(PlannedTask(goal = Goal.LOAD_PROJECTS, description = "Загрузка всех данных проектов"))
            .stage(PlannedTask(goal = Goal.FILL_PROJECTS, description = "Выгрузка проектов в целевые проекты"))

    }
}
