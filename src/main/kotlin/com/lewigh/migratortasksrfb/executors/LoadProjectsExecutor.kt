package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class LoadProjectsExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECTS) : TaskExecutor {

    override fun execute(current: CurrentTask): List<List<PlannedTask>> {
        println("Загрузка проектов")

        return listOf(
            listOf(
                PlannedTask(goal = Goal.LOAD_PROJECT, description = "Загрузка проекта 1"),
                PlannedTask(goal = Goal.LOAD_PROJECT, description = "Загрузка проекта 2")
            )
        )
    }
}
