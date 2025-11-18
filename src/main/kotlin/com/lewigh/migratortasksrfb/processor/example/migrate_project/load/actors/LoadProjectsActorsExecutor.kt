package com.lewigh.migratortasksrfb.processor.example.migrate_project.load.actors

import com.fasterxml.jackson.databind.ObjectMapper
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.processor.example.migrate_project.*
import org.springframework.stereotype.Component

@Component
class LoadProjectsActorsExecutor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT_ACTORS, private val objectMapper: ObjectMapper) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {

        var planned = mutableListOf<PlannedTask>()

        for (i in 0..500 step 100) {
            planned.add(
                PlannedTask(
                    TaskGoal.LOAD_PROJECT_ACTORS_BATCH, title = "Загрузка акторов с $i по ${i + 99}", params = mapOf(
                        Parameters.size to 100,
                        Parameters.offset to i
                    )
                )
            )
        }

        planner.parralel(*planned.toTypedArray())
    }
}
