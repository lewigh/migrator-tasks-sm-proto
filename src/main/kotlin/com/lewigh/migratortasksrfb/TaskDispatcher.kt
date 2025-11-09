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
                exec(task)
            } else {
                planTask(task)
            }
        }
    }

    private fun exec(currentTask: Task) {
        val executor = handlers.find { it.goal == currentTask.goal } ?: throw Exception()

        currentTask.status = Status.RUNNING
        val plannedTasks = executor.execute(CurrentTask(currentTask.description))
        currentTask.executed = true

        if (plannedTasks.isEmpty()) {
            currentTask.done()
            return
        }

        val priority = plannedTasks.first()

        for (p in priority) {
            taskRepo.save(Task(description = p.description, goal = p.goal, status = Status.PENDING, stage = 0, parent = currentTask))
        }

        for (i in 1..<plannedTasks.size) {
            for (t in plannedTasks[i]) {
                taskRepo.save(Task(description = t.description, goal = t.goal, status = Status.WAITING, stage = i, parent = currentTask))
            }
        }

        currentTask.status = Status.WAITING
    }

    private fun planTask(task: Task) {
        val children = task.children
        if (children.isEmpty()) {
            task.done()
            return
        }
        children.sortBy { it.stage }

        var c = -1
        var completed = true

        for (t in children) {
            if (t.status == Status.RUNNING) {
                task.status = Status.WAITING
                return
            }
            if (t.status == Status.DONE) {
                continue
            }
            if (t.status == Status.WAITING) {

                if (c == -1) {
                    c == t.stage
                }

                if (t.stage == c) {
                    t.status = Status.PENDING
                }

                completed = false
            }
        }

        if (completed) {
            task.status = Status.DONE
        } else {
            task.status = Status.WAITING
        }

    }


    @Transactional
    fun createMigration(): Task {
        val migrationTask = Task(goal = Task.Goal.MIGRATION, status = Status.PENDING, description = "Миграция данных")

        return taskRepo.save(migrationTask)
    }

}
