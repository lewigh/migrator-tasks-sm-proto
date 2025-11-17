package com.lewigh.migratortasksrfb.engine.internal

import com.fasterxml.jackson.databind.*
import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import io.github.oshai.kotlinlogging.*
import jakarta.persistence.*
import org.springframework.data.repository.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.*

@Component
class TaskDispatcher(
    val repo: TaskRepository, val handlers: List<TaskProcessor>, private val executor: ExecutorService, val txManager: TransactionalComponent, private val objectMapper: ObjectMapper
) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    fun planRound() {
        val tasks = repo.findAllByStatusIs(Status.PENDING)

        val (plannables, executables) = tasks.partition { it.executed }

        for (task in plannables) {
            planTasks(task)
        }

        for (task in executables) {
            executeInParralel(requireNotNull(task.id))
        }
    }

    private fun executeInParralel(taskId: Long) {
        executor.submit {
            try {
                txManager.with {
                    val task = requireNotNull(repo.findByIdOrNull(taskId))

                    txManager.withNew {
                        task.run()
                        logger.info { "Задача запущена: ${task.description}" }
                        repo.save(task)
                    }

                    try {
                        executeTask(task)
                    } catch (e: Exception) {
                        logger.error(e) {}
                        task.error(e)
                    }
                }
            } catch (e: PersistenceException) {
                logger.error(e) {}
            } catch (e: Exception) {
                logger.error(e) {}
                throw e
            }
        }
    }

    private fun executeTask(currentTask: Task) {

        val executor = handlers.find { it.goal == currentTask.goal } ?: throw Exception()

        val params = if (currentTask.params != null) {
            objectMapper.readValue(currentTask.params, Any::class.java)
        } else {
            null
        }

        executor.process(CurrentTask(currentTask.description, params, currentTask.domainId), TaskPlanner(currentTask, objectMapper = objectMapper))

        currentTask.executed = true

        if (currentTask.subtasks.isEmpty()) {
            currentTask.doneThenWakeUpParent()
            logger.info { "Задача завершена: ${currentTask.description}" }
            return
        }

        for (subtask in currentTask.subtasks) {
            repo.save(subtask)
        }

        currentTask.waitTo()
        repo.save(currentTask)
    }

    private fun planTasks(task: Task) {

        if (task.isAllSubtaskDone()) {
            task.doneThenWakeUpParent()
            return
        }

        if (task.isAnySubtaskFailured()) {
            task.toCorrupted()
            return
        }

        if (task.hasSubtasksToPlan()) {
            task.planNextStageWaitingTasks()
        }

        task.waitTo()
    }


    @Transactional
    fun planNew(planned: PlannedTask): Task {
        val migrationTask = Task(
            goal = planned.goal,
            status = Status.PENDING,
            description = planned.description,
            params = objectMapper.writeValueAsString(planned.parameters),
            domainId = planned.domainId,
        )

        return repo.save(migrationTask)
    }

}
