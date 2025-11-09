package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoadProjectExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask): List<List<PlannedTask>> {
        println("Загрузка проекта")
        Thread.sleep(20_000)
        println("Проект загружен")
        return listOf()
    }
}
