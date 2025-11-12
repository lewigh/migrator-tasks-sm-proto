package com.lewigh.migratortasksrfb.executors.migrate_project.report

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component

@Component
class CreateMigrationReportExecutor(override val goal: Task.Goal = Task.Goal.CREATE_MIGRATION_REPORT) : TaskExecutor {

    override fun execute(current: CurrentTask, planner: TaskPlanner) {
        Thread.sleep(2_000)
    }
}
