package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.*
import com.lewigh.migratortasksrfb.TaskController.*
import com.lewigh.migratortasksrfb.engine.*
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl(private val repo: TaskRepository, private val jsonComponent: JsonComponent) : TaskService {
    override fun restartTaskById(id: Long) {
        val task = repo.findById(id).orElseThrow()
        task.toRerun()
        repo.save(task)
    }

    override fun findAllTasks(): List<TaskDto> {
        return repo.findAllByGoal(TaskGoal.PROJECT_MIGRATE)
            .map { mapToDto(it) }
    }

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
