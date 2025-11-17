package com.lewigh.migratortasksrfb.processor.migrate_project.load.actors.load

import com.fasterxml.jackson.databind.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import com.lewigh.migratortasksrfb.processor.migrate_project.*
import org.springframework.stereotype.Component

@Component
class LoadProjectActorsExecutor(override val goal: Goal = Goal.LOAD_PROJECT_ACTORS_BATCH, private val objectMapper: ObjectMapper) : TaskProcessor {

    override fun process(current: CurrentTask, planner: TaskPlanner) {
        val value = objectMapper.readValue(current.parameters, PageModel::class.java)

        println("Получены параметры ${value}")
        Thread.sleep(5_000)
    }
}
