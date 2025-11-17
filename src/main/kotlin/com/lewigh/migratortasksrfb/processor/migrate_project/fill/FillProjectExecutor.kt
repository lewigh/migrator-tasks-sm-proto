package com.lewigh.migratortasksrfb.processor.migrate_project.fill

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import org.springframework.stereotype.Component

@Component
class FillProjectExecutor(override val goal: Goal = Goal.PROJECT_FILL) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = Goal.FILL_PROJECT_SCHEMA, description = "Выгрузка схем в Меркурий 1"),
            PlannedTask(goal = Goal.FILL_PROJECT_ACTORS, description = "Выгрузка акторов в Меркурий 1"),
            PlannedTask(goal = Goal.FILL_PROJECT_COMMENTS, description = "Выгрузка комментов в Меркурий 1")
        )
    }
}
