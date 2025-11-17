package com.lewigh.migratortasksrfb.processor.migrate_project.load.actors

import com.fasterxml.jackson.databind.ObjectMapper
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.processor.migrate_project.*
import org.springframework.stereotype.Component

@Component
class LoadProjectsActorsExecutor(override val goal: Goal = Goal.LOAD_PROJECT_ACTORS, private val objectMapper: ObjectMapper) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {

        var planned = mutableListOf<PlannedTask>()

        for (i in 0..500 step 100) {
            val params = PageModel(100, i)
            planned.add(PlannedTask(Goal.LOAD_PROJECT_ACTORS_BATCH, description = "Загрузка акторов с $i по ${i + 99}", parameters = params))
        }

        planner.parralel(*planned.toTypedArray())
    }
}
