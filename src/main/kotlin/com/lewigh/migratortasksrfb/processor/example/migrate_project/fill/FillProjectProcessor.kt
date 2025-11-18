package com.lewigh.migratortasksrfb.processor.example.migrate_project.fill

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class FillProjectProcessor(override val goal: TaskGoal = TaskGoal.PROJECT_FILL) : TaskProcessor {

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        planner.parralel(
            PlannedTask(goal = TaskGoal.FILL_PROJECT_SCHEMA, title = "Выгрузка схем в Меркурий 1"),
            PlannedTask(goal = TaskGoal.FILL_PROJECT_ACTORS, title = "Выгрузка акторов в Меркурий 1"),
            PlannedTask(goal = TaskGoal.FILL_PROJECT_COMMENTS, title = "Выгрузка комментов в Меркурий 1")
        )
    }
}
