package com.lewigh.migratortasksrfb.executors.migrate_project.fill

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class FillProjectExecutor(override val goal: Task.Goal = Task.Goal.FILL_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = Goal.FILL_PROJECT_SCHEMA, description = "Выгрузка схем в Меркурий 1"),
            PlannedTask(goal = Goal.FILL_PROJECT_ACTORS, description = "Выгрузка акторов в Меркурий 1"),
            PlannedTask(goal = Goal.FILL_PROJECT_COMMENTS, description = "Выгрузка комментов в Меркурий 1")
        )
    }
}
