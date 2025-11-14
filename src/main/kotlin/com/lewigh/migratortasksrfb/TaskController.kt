package com.lewigh.migratortasksrfb;

import com.lewigh.migratortasksrfb.Task.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController(
    val repo: TaskRepository,
    val service: TaskDispatcher,
    private val taskDispatcher: TaskDispatcher
) {

    private var started = false

    @GetMapping
    suspend fun getAllTasks(): List<TaskDto> {
        return repo.findAllByGoal(Task.Goal.MIGRATE_PROJECT)
            .map { mapT(it) }
    }

    @Transactional
    @GetMapping("start")
    suspend fun start() {
        service.planNew(PlannedTask(Goal.MIGRATE_PROJECT, "Миграция проекта Венера1 в Меркурий1"))
    }

    @Transactional
    @GetMapping("next")
    suspend fun next() {
        if (started) {
            service.planRound()
        } else {
            start()
            started = true
        }
    }

    class TaskDto(
        var id: Long,
        var description: String,
        var parentId: Long?,
        var goal: Goal,
        var status: Status,
        var stage: Int,
        var executed: Boolean,
        var error: String?,
        var subtasks: MutableList<TaskDto>
    )

    fun mapT(task: Task): TaskDto {
        return TaskDto(
            id = requireNotNull(task.id),
            parentId = task.parent?.id,
            goal = task.goal,
            status = task.status,
            description = task.description,
            stage = task.stage,
            executed = task.executed,
            subtasks = task.subtasks.map { mapT(it) }.toMutableList(),
            error = task.error
        )
    }
}
