package com.lewigh.migratortasksrfb;

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.*

@RestController
public class TaskController(
    val repo: TaskRepository,
    val service: TaskDispatcher,
    private val error: View,
    private val taskDispatcher: TaskDispatcher
) {

    private var started = false

    @GetMapping
    fun getAllTasks(): List<TaskDto> {
        return repo.findAllByGoal(Goal.PROJECT_MIGRATE)
            .map { mapT(it) }
    }

    @Transactional
    @GetMapping("start")
    fun start() {
        service.planNew(PlannedTask(Goal.PROJECT_MIGRATE, "Миграция проекта Венера1 в Меркурий1", domainId = "0f60fc70-2d7b-4f2d-8254-fd3e7db09cd9"))
    }

    @Transactional
    @GetMapping("next")
    fun next() {
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
        var params: String?,
        var domainId: String?,
        var error: String?,
        var subtasks: MutableList<TaskDto>,
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
            error = task.error,
            domainId = task.domainId,
            params = task.params
        )
    }
}
