package com.lewigh.migratortasksrfb;

import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*
import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController(
    private val dispatcher: TaskDispatcher,
    private val taskService: TaskService
) {

    private var started = false

    @GetMapping
    fun getAllTasks(): List<TaskDto> {
        return taskService.findAllTasks()
    }

    @Transactional
    @GetMapping("start")
    fun start() {
        dispatcher.planRootTask("0f60fc70-2d7b-4f2d-8254-fd3e7db09cd9", PlannedTask(TaskGoal.PROJECT_MIGRATE, "Миграция проекта Венера1 в Меркурий1"))
    }

    @Transactional
    @GetMapping("restart/{taskId}")
    fun restart(@PathVariable taskId: Long) {
        taskService.restartTaskById(taskId)
    }

    @Transactional
    @GetMapping("next")
    fun next() {
        if (started) {
            dispatcher.dispatchRound()
        } else {
            start()
            started = true
        }
    }


}
