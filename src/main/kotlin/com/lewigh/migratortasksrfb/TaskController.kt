package com.lewigh.migratortasksrfb;

import com.lewigh.migratortasksrfb.Task.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController(val repo: TaskRepository, val service: TaskDispatcher) {

    @GetMapping
    fun getAllTasks(): List<TaskDto> {

        return repo.findAllByGoal(Task.Goal.MIGRATION)
            .map { mapT(it) }
    }

    @Transactional
    @GetMapping("start")
    fun start() {
        service.createMigration()
    }

    @Transactional
    @GetMapping("next")
    fun next() {
        service.planRound()
    }

    class TaskDto(
        var id: Long,
        var description: String,
        var parentId: Long?,
        var goal: Goal,
        var status: Status,
        var stage: Int,
        var executed: Boolean,
        var children: MutableList<TaskDto> = mutableListOf()
    )

    data class Info(val task: Task, val childred: List<Info>)

    fun mapT(task: Task): TaskDto {
        return TaskDto(
            id = requireNotNull(task.id),
            parentId = task.parent?.id,
            goal = task.goal,
            status = task.status,
            description = task.description,
            stage = task.stage,
            executed = task.executed,
            children = task.subtasks.map { mapT(it) }.toMutableList()
        )
    }
}
