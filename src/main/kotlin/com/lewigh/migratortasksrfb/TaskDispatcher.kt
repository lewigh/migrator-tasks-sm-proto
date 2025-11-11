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
        currentTask.run()
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

        if (task.hasSubtasksToPlan()) {
            task.planNextStageWaitingTasks()
        }

        task.waitTo()
    }


    @Transactional
    fun planNew(planned: PlannedTask): Task {
        val migrationTask = Task(goal = planned.goal, status = Status.PENDING, description = planned.description)

        return taskRepo.save(migrationTask)
    }

}
