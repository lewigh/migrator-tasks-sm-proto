package com.lewigh.migratortasksrfb.executors.migrate_project.load.comments

import com.lewigh.migratortasksrfb.*
import org.springframework.stereotype.Component

@Component
class LoadProjectCommentsExecutor(override val goal: Task.Goal = Task.Goal.LOAD_PROJECT_COMMENTS) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        println("Загрузка комментариев проекта Меркурий")
        //throw RuntimeException("AAA")
        //Thread.sleep(20_000)
        println("Комментарии проекта Меркурий загружены")
    }
}
