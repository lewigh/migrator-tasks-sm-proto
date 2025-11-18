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
    private val taskDispatcher: TaskDispatcher,
    private val jsonComponent: JsonComponent
) {

    private var started = false

    @GetMapping
    fun getAllTasks(): List<TaskDto> {
        return repo.findAllByGoal(TaskGoal.PROJECT_MIGRATE)
            .map { mapToDto(it) }
    }

    @Transactional
    @GetMapping("start")
    fun start() {
        service.planRootTask("0f60fc70-2d7b-4f2d-8254-fd3e7db09cd9", PlannedTask(TaskGoal.PROJECT_MIGRATE, "Миграция проекта Венера1 в Меркурий1"))
    }

    @Transactional
    @GetMapping("next")
    fun next() {
        if (started) {
            service.dispatchRound()
        } else {
            start()
            started = true
        }
    }

    data class TaskDto(
        var id: Long,
        var title: String,
        var status: Status,
        var goal: TaskGoal,
        var parentId: Long?,
        var contextId: String,
        var stage: Int,
        var executed: Boolean,
        var params: Map<String, Any>?,
        var error: String?,
        var subtasks: MutableList<TaskDto>,
    )

    fun mapToDto(task: Task): TaskDto {
        return TaskDto(
            id = requireNotNull(task.id),
            parentId = task.parent?.id,
            goal = task.goal,
            status = task.status,
            title = task.title,
            stage = task.stage,
            executed = task.executed,
            subtasks = task.subtasks.map { mapToDto(it) }.toMutableList(),
            error = task.error,
            contextId = task.contextId,
            params = task.params?.let { jsonComponent.toMap(it) }
        )
    }
}
