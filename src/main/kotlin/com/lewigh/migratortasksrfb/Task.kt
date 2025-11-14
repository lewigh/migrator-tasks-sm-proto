package com.lewigh.migratortasksrfb

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*



@Table(name = "task", schema = "core")
class Task(
    @Id
    var id: Long? = null,
    var description: String,
    @Transient
    var parent: Task? = null,
    var goal: Goal,
    var params: String? = null,
    var error: String? = null,
    @Transient
    var subtasks: MutableList<Task> = mutableListOf(),
    var status: Status,
    var stage: Int = 0,
    var executed: Boolean = false,
) {

    fun isRunning(): Boolean = status == Status.RUNNING
    fun isPending(): Boolean = status == Status.PENDING
    fun isDone(): Boolean = status == Status.DONE
    fun isError(): Boolean = status == Status.ERROR
    fun isWaiting(): Boolean = status == Status.WAITING
    fun isCorrupted(): Boolean = status == Status.CORRUPTED
    fun isAllSubtaskDone(): Boolean = subtasks.all { it.isDone() }
    fun isAnySubtaskFailured(): Boolean = subtasks.any { it.isError() || it.isCorrupted() }
    fun hasSubtasksToPlan(): Boolean = subtasks.all { it.status == Status.DONE || it.status == Status.WAITING }

    fun doneThenWakeUpParent() {
        parent?.status = Status.PENDING
        status = Status.DONE
    }

    fun run() {
        status = Status.RUNNING
    }

    fun waitTo() {
        status = Status.WAITING
    }

    fun pending() {
        status = Status.PENDING
    }

    fun toCorrupted() {
        status = Status.CORRUPTED
    }

    fun error(exception: Exception) {

        var cur: Throwable? = exception
        var res: String = ""

        while (cur != null) {
            var st = cur.stackTrace.first() ?: null
            res += "${cur.message}:${st?.fileName}:${st?.methodName}:${st?.lineNumber}"
            cur = cur.cause
        }

        error(res)
    }

    fun error(text: String) {
        status = Status.ERROR
        error = text
        parent?.status = Status.PENDING
    }

    fun planNextStageWaitingTasks() {
        subtasks.asSequence()
            .filter { it.isWaiting() }
            .groupBy { it.stage }
            .toSortedMap()
            .firstEntry().value
            .forEach { it.pending() }
    }

    enum class Goal {
        MIGRATE_PROJECT,
        LOAD_PROJECTS,
        FILL_PROJECT,
        FILL_PROJECT_ACTORS,
        FILL_PROJECT_SCHEMA,
        FILL_PROJECT_COMMENTS,
        LOAD_PROJECT,
        LOAD_PROJECT_SCHEMA,
        LOAD_PROJECT_ACTORS,
        LOAD_PROJECT_COMMENTS,
        CREATE_MIGRATION_REPORT,
    }

    enum class Status {
        PENDING,
        WAITING,
        RUNNING,
        DONE,
        CORRUPTED,
        ERROR
    }

}
