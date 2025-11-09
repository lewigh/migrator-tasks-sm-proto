package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class MigrationExecutor(override val goal: Task.Goal = Task.Goal.MIGRATION) : TaskExecutor {

    override fun execute(current: CurrentTask): List<List<PlannedTask>> {
        println("Постановка основных задач")

        return listOf(
            listOf(PlannedTask(goal = Goal.LOAD_PROJECTS, description = "Загрузка всех данных проектов")),
            listOf(PlannedTask(goal = Goal.FILL_PROJECTS, description = "Выгрузка проектов в целевые проекты"))
        )
    }
}
