package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import io.github.oshai.kotlinlogging.*
import jakarta.persistence.*
import org.springframework.data.repository.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskDispatcher(
    private val repo: TaskRepository,
    private val processors: List<TaskProcessor>,
    private val threadPoolComponent: ThreadPoolComponent,
    private val txManager: TransactionalComponent,
    private val jsonComponent: JsonComponent
) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    fun planRootTask(domainId: String, planned: PlannedTask): Task {
        val rootTask = plannedTaskToEntityTask(planned, Status.PENDING, domainId, null)

        return repo.save(rootTask)
    }

    @Transactional
    fun dispatchRound() {
        val tasks = repo.findAllByStatusIs(Status.PENDING)

        val (plannables, executables) = tasks.partition { it.executed }

        for (task in plannables) {
            planTask(task)
        }

        for (task in executables) {
            executeInParralel(requireNotNull(task.id))
        }
    }

    private fun executeInParralel(taskId: Long) {
        threadPoolComponent.launch {
            try {
                txManager.with {
                    val task = requireNotNull(repo.findByIdOrNull(taskId))

                    txManager.withNew {
                        task.run()
                        onTaskRun(task)
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

        val processor = processors
            .find { it.goal == currentTask.goal }
            ?: throw IllegalStateException("Couldn't execute task - processor for goal:${currentTask.goal} not found")

        val params = currentTask.params?.let { jsonComponent.toMap(it) }

        val plannedTasksBuffer = mutableListOf<PlannedTask>();

        processor.process(CurrentTask(currentTask.title, currentTask.domainId, params), TaskPlanner(plannedTasksBuffer))

        currentTask.subtasks = plannedTasksToEntityTasks(plannedTasksBuffer, currentTask)

        currentTask.executed = true

        if (currentTask.subtasks.isEmpty()) {
            currentTask.doneThenWakeUpParent()
            onTaskCompleted(currentTask)
            return
        }

        for (subtask in currentTask.subtasks) {
            repo.save(subtask)
        }

        currentTask.toWait()

        repo.save(currentTask)
    }

    private fun planTask(task: Task) {

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

        task.toWait()
    }


    private fun plannedTasksToEntityTasks(tasks: List<PlannedTask>, parent: Task): MutableList<Task> {

        val subtasks = mutableListOf<Task>();

        for (task in tasks) {

            var plannedStatus = if (task.stage == READY_TO_PENDING) {
                Task.Status.PENDING
            } else {
                Task.Status.WAITING
            }

            val newSubtask = plannedTaskToEntityTask(task, plannedStatus, parent.domainId, parent)

            subtasks.add(newSubtask)

            validate(task, newSubtask, parent)
        }

        return subtasks
    }

    private fun plannedTaskToEntityTask(
        plannedTask: PlannedTask,
        plannedStatus: Status,
        domainId: String,
        parent: Task?,
    ) = Task(
        title = plannedTask.title,
        goal = plannedTask.goal,
        status = plannedStatus,
        stage = plannedTask.stage,
        parent = parent,
        domainId = domainId,
        params = plannedTask.params?.let { jsonComponent.fromMap(it) },
    )

    private fun validate(task: PlannedTask, newSubtask: Task, parent: Task) {
        if (parent.goal == task.goal) {
            val message = "Failed task: Couldn't plan child task with the same goal as parent. Parent: ${parent.goal} child $task"
            logger.error { message }
            newSubtask.error(message)
        }
    }

    private fun onTaskRun(task: Task) {
        logger.info { "Started task: ${task.title}" }
    }

    private fun onTaskCompleted(task: Task) {
        logger.info { "Completed task: ${task.title}" }
    }

}
