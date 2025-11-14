package com.lewigh.migratortasksrfb

import org.springframework.data.repository.kotlin.*
import org.springframework.stereotype.Repository
import reactor.core.publisher.*

@Repository
interface TaskRepository : CoroutineCrudRepository<Task, Long> {

    suspend fun findAllByGoal(goal: Task.Goal): List<Task>

    suspend fun findAllByParentIdIs(parentId: Long): List<Task>

    suspend fun findAllByStatusIs(status: Task.Status): Flux<Task>
}
