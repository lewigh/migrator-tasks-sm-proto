package com.lewigh.migratortasksrfb.engine.internal

import com.lewigh.migratortasksrfb.engine.*
import jakarta.persistence.*

@Table(name = "task")
@Entity()
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var parent: Task? = null,
    var contextId: String,
    var title: String,
    var goal: TaskGoal,
    var status: Status,
    var executed: Boolean = false,
    var stage: Int = 0,
    var params: String?,
    @Lob
    var error: String? = null,
    @OneToMany(mappedBy = "parent")
    var subtasks: MutableList<Task> = mutableListOf()
) {

    fun isRunning(): Boolean = status == Status.RUNNING

    fun isPending(): Boolean = status == Status.PENDING

    fun isDone(): Boolean = status == Status.DONE

    fun isError(): Boolean = status == Status.ERROR

    fun isWaiting(): Boolean = status == Status.WAITING

    fun isCorrupted(): Boolean = status == Status.CORRUPTED

    fun isAllSubtaskDone(): Boolean = subtasks.all { it.isDone() }

    fun isAnySubtaskFailured(): Boolean = subtasks.any { it.isError() || it.isCorrupted() }

    fun hasSubtasksToRun(): Boolean = subtasks.all { it.status == Status.DONE || it.status == Status.WAITING }

    fun toDoneAndWakeUpParent() {
        wakeUpParent()
        status = Status.DONE
    }

    fun toRun() {
        status = Status.RUNNING
    }

    fun toWait() {
        status = Status.WAITING
    }

    fun toPending() {
        status = Status.PENDING
    }

    fun toCorrupted() {
        status = Status.CORRUPTED
    }

    fun toError(exception: Exception) {

        var cur: Throwable? = exception
        var res: String = ""

        while (cur != null) {
            var st = cur.stackTrace.first() ?: null
            res += "${cur.message}:${st?.fileName}:${st?.methodName}:${st?.lineNumber}"
            cur = cur.cause
        }

        toError(res)
    }

    fun toError(text: String) {
        status = Status.ERROR
        error = text
        parent?.status = Status.PENDING
    }

    fun planNextStageWaitingTasksToPending() {
        subtasks.asSequence()
            .filter { it.isWaiting() }
            .groupBy { it.stage }
            .toSortedMap()
            .firstEntry().value
            .forEach { it.toPending() }
    }

    fun toRerun() {
        if (subtasks.isNotEmpty()) {
        } else {
            this.error = null
            toPending()
            wakeUpParent()
        }
    }

    fun wakeUpParent() {
        parent?.status = Status.PENDING
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
