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
    private val repo: TaskRepository,
    private val processors: List<TaskProcessor>,
    private val executor: ExecutorService,
    private val txManager: TransactionalComponent,
    private val objectMapper: ObjectMapper
) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    fun planNewTask(planned: PlannedTask): Task {
        val migrationTask = plannedTaskToTask(planned, Status.PENDING, null)

        return repo.save(migrationTask)
    }

    @Transactional
    fun dispatchRound() {
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
                        logger.info { "Задача запущена: ${task.title}" }
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

        val processor = processors.find { it.goal == currentTask.goal } ?: throw Exception()

        val params = if (currentTask.params != null) {
            objectMapper.readValue(currentTask.params, Any::class.java)
        } else {
            null
        }

        val plannedTasksBuffer = mutableListOf<PlannedTask>();

        processor.process(CurrentTask(currentTask.title, params, currentTask.domainId), TaskPlanner(plannedTasksBuffer))

        currentTask.subtasks = plannedTasksToTasks(plannedTasksBuffer, currentTask)

        currentTask.executed = true

        if (currentTask.subtasks.isEmpty()) {
            currentTask.doneThenWakeUpParent()
            logger.info { "Задача завершена: ${currentTask.title}" }
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


    private fun plannedTasksToTasks(tasks: List<PlannedTask>, parent: Task): MutableList<Task> {

        val subtasks = mutableListOf<Task>();

        for (task in tasks) {

            var plannedStatus = if (task.stage == READY_TO_PENDING) {
                Task.Status.PENDING
            } else {
                Task.Status.WAITING
            }

            val newSubtask = plannedTaskToTask(task, plannedStatus, parent)

            subtasks.add(newSubtask)

            validate(task, newSubtask, parent)
        }

        return subtasks
    }

    private fun plannedTaskToTask(
        plannedTask: PlannedTask,
        plannedStatus: Status,
        parent: Task?,
    ) = Task(
        title = plannedTask.title,
        goal = plannedTask.goal,
        status = plannedStatus,
        stage = plannedTask.stage,
        parent = parent,
        domainId = plannedTask.domainId ?: parent?.domainId,
        params = plannedTask.parameters?.let { objectMapper.writeValueAsString(it) },
    )

    private fun validate(task: PlannedTask, newSubtask: Task, parent: Task) {
        if (parent.goal == task.goal) {
            val message = "Couldn't plan child task with the same goal as parent. Parent: ${parent.goal} child $task"
            logger.error { message }
            newSubtask.error(message)
        }
    }

}
