package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class LoadProjectSchemaExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT_SCHEMA) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Загрузка схемы проекта")
        throw RuntimeException("AAA")
        //Thread.sleep(20_000)
        println("Проект загружен")
    }
}
