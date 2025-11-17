package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.engine.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    fun findAllByGoal(goal: TaskGoal): List<Task>

    fun findAllByParentIdIs(parentId: Long): List<Task>

    fun findAllByStatusIs(status: Task.Status): List<Task>
}
