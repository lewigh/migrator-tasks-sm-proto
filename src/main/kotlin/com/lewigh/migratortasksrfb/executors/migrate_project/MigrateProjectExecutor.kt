package com.lewigh.migratortasksrfb.executors.migrate_project

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class MigrateProjectExecutor(override val goal: Task.Goal = Task.Goal.MIGRATE_PROJECT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        planner
            .parralel(PlannedTask(goal = Goal.LOAD_PROJECT, description = "Загрузка всех данных проекта из внешенего источника Венера1"))
            .parralel(PlannedTask(goal = Goal.FILL_PROJECT, description = "Выгрузка проекта в целевую платформу Меркурий"))
            .parralel(PlannedTask(goal = Goal.CREATE_MIGRATION_REPORT, description = "Формирование отчета о миграции проекта Венера1 в Меркурий1"))

    }
}
