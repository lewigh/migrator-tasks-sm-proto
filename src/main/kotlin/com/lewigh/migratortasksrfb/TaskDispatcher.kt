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
                    taskRepo.save(task)
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
        val plannedTasks = executor.execute(CurrentTask(currentTask.description))
        currentTask.executed = true

        if (plannedTasks.isEmpty()) {
            currentTask.done()
            return
        }

        for (task in plannedTasks.firstStage) {
            taskRepo.save(Task(description = task.description, goal = task.goal, status = Status.PENDING, stage = 0, parent = currentTask))
        }

        val extra = plannedTasks.extraStages()

        for (stageNumber in 0..<extra.size) {
            for (task in extra[stageNumber]) {
                taskRepo.save(Task(description = task.description, goal = task.goal, status = Status.WAITING, stage = stageNumber + 1, parent = currentTask))
            }
        }

        currentTask.waitTo()
    }

    private fun planTasks(task: Task) {

        if (task.isAllSubtaskDone()) {
            task.done()
            return
        }

        if (task.subtasks.all { it.status != Status.DONE || it.status != Status.WAITING }) {
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
