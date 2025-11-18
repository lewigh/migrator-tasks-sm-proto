package com.lewigh.migratortasksrfb.processor.example.migrate_project

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class MigrateProjectProcessor(override val goal: TaskGoal = TaskGoal.PROJECT_MIGRATE) : TaskProcessor {

    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        planner
            .parralel(PlannedTask(goal = TaskGoal.LOAD_PROJECT, title = "Загрузка всех данных проекта из внешенего источника Венера1"))
            .parralel(PlannedTask(goal = TaskGoal.PROJECT_FILL, title = "Выгрузка проекта в целевую платформу Меркурий"))
            .parralel(PlannedTask(goal = TaskGoal.CREATE_MIGRATION_REPORT, title = "Формирование отчета о миграции проекта Венера1 в Меркурий1"))

    }
}
