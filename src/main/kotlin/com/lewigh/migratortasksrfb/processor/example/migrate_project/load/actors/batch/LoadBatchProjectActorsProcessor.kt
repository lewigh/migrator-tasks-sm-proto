package com.lewigh.migratortasksrfb.processor.example.migrate_project.load.actors.batch

import com.fasterxml.jackson.databind.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.*
import io.github.oshai.kotlinlogging.*
import org.springframework.stereotype.Component

@Component
class LoadBatchProjectActorsProcessor(override val goal: TaskGoal = TaskGoal.LOAD_PROJECT_ACTORS_BATCH, private val objectMapper: ObjectMapper) : TaskProcessor {

    private val logger = KotlinLogging.logger {}


    override fun process(current: CurrentTaskInfo, planner: TaskPlanner) {
        val value = current.params

        logger.info { "Начинаю загрузку акторов в количестве ${value?.get("size")} от ${value?.get("offset")}" }
        Thread.sleep(5_000)
    }
}
