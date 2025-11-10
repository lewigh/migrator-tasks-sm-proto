package com.lewigh.migratortasksrfb.executors

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreProjectsExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECTS) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Постановка задач на выгрузку проектов")
    }
}
