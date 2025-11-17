package com.lewigh.migratortasksrfb.processor.migrate_project

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import org.springframework.stereotype.Component

@Component
class MigrateProjectProcessor(override val goal: Goal = Goal.PROJECT_MIGRATE) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        planner
            .parralel(PlannedTask(goal = Goal.LOAD_PROJECT, description = "Загрузка всех данных проекта из внешенего источника Венера1"))
            .parralel(PlannedTask(goal = Goal.PROJECT_FILL, description = "Выгрузка проекта в целевую платформу Меркурий"))
            .parralel(PlannedTask(goal = Goal.CREATE_MIGRATION_REPORT, description = "Формирование отчета о миграции проекта Венера1 в Меркурий1"))

    }
}
