package com.lewigh.migratortasksrfb

import com.lewigh.migratortasksrfb.Task.*
import io.github.oshai.kotlinlogging.*
import kotlinx.coroutines.reactor.*
import org.springframework.data.repository.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.*
import java.util.concurrent.*

@Component
class TaskDispatcher(val repo: TaskRepository, val handlers: List<TaskExecutor>, private val executor: ExecutorService, val f: TransactionalOperator) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    suspend fun planRound() {
        val tasks = repo.findAllByStatusIs(Status.PENDING).collectList().awaitSingle()

        val (plannables, executables) = tasks.partition { it.executed }

        for (task in plannables) {
            planTasks(task)
        }

        for (task in executables) {
            executeInParralel(requireNotNull(task.id))
        }
    }

    private suspend fun executeInParralel(taskId: Long) {
        try {
            f.executeAndAwait {
                val task = repo.findById(taskId) ?: throw RuntimeException()

                f.executeAndAwait {
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
        } catch (e: Exception) {
            logger.error(e) {}
            throw e
        }

    }

    private suspend fun executeTask(currentTask: Task) {

        val executor = handlers.find { it.goal == currentTask.goal } ?: throw Exception()

        executor.execute(CurrentTask(currentTask.description), TaskPlanner(currentTask))

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

    private suspend fun planTasks(task: Task) {

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
    suspend fun planNew(planned: PlannedTask): Task {
        val migrationTask = Task(goal = planned.goal, status = Status.PENDING, description = planned.description)

        return repo.save(migrationTask)
    }

}
