package com.lewigh.migratortasksrfb

import com.lewigh.migratortasksrfb.Task.*
import io.github.oshai.kotlinlogging.*
import jakarta.persistence.*
import org.springframework.data.repository.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.*

@Component
class TaskDispatcher(val repo: TaskRepository, val handlers: List<TaskExecutor>, private val executor: ExecutorService, val txManager: TransactionalComponent) {

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
                txManager.executeWithoutResult {
                    val task = requireNotNull(repo.findByIdOrNull(taskId))

                    txManager.executeInSeparated {
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
        val migrationTask = Task(goal = planned.goal, status = Status.PENDING, description = planned.description)

        return repo.save(migrationTask)
    }

}
