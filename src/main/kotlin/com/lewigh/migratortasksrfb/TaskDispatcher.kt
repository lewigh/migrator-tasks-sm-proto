package com.lewigh.migratortasksrfb

import com.lewigh.migratortasksrfb.Task.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskDispatcher(val taskRepo: TaskRepository, val handlers: List<TaskExecutor>) {

    @Transactional
    fun planRound() {
        for (task in taskRepo.findAllByStatusIs(Status.PENDING)) {
            if (!task.executed) {
                try {
                    executeTask(task)
                } catch (e: Exception) {
                    task.error(e)
                }

            } else {
                planTasks(task)
            }
        }
    }

    private fun executeTask(currentTask: Task) {
        val executor = handlers.find { it.goal == currentTask.goal } ?: throw Exception()

        currentTask.status = Status.RUNNING
        taskRepo.flush()

        executor.execute(CurrentTask(currentTask.description), TaskPlanner(currentTask))

        currentTask.executed = true

        if (currentTask.subtasks.isEmpty()) {
            currentTask.done()
            return
        }

        for (subtask in currentTask.subtasks) {
            taskRepo.save(subtask)
        }

        currentTask.waitTo()
    }

    private fun planTasks(task: Task) {

        if (task.isAllSubtaskDone()) {
            task.done()
            return
        }

        if (task.isAnySubtaskFailured()) {
            task.toCorrupted()
            return
        }

        if (task.subtasks.all { it.status == Status.DONE || it.status == Status.WAITING }) {
            task.subtasks.asSequence()
                .filter { it.isWaiting() }
                .groupBy { it.stage }
                .toSortedMap()
                .firstEntry().value
                .forEach { it.pending() }
        }
        task.waitTo()
    }


    @Transactional
    fun createMigration(): Task {
        val migrationTask = Task(goal = Task.Goal.MIGRATION, status = Status.PENDING, description = "Миграция данных")

        return taskRepo.save(migrationTask)
    }

}
