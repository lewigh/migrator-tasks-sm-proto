package com.lewigh.migratortasksrfb

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    fun findAllByGoal(goal: Task.Goal): List<Task>

    fun findAllByParentIdIs(parentId: Long): List<Task>

    fun findAllByStatusIs(status: Task.Status): List<Task>
}
