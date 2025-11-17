package com.lewigh.migratortasksrfb.processor.example.migrate_project.report

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.stereotype.Component

@Component
class CreateMigrationReportExecutor(override val goal: TaskGoal = TaskGoal.CREATE_MIGRATION_REPORT) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        Thread.sleep(2_000)
    }
}
