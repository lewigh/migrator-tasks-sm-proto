package com.lewigh.migratortasksrfb

import jakarta.persistence.*

@Table(name = "task")
@Entity()
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var description: String,
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var parent: Task? = null,
    var goal: Goal,
    var params: String? = null,
    var error: String? = null,
    @OneToMany(mappedBy = "parent")
    var subtasks: MutableList<Task> = mutableListOf(),
    var status: Status = Status.PENDING,
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

    fun done() {
        parent?.status = Status.PENDING
        status = Status.DONE
    }

    fun waitTo() {
        status = Status.WAITING
    }

    fun pending() {
        status = Status.PENDING
    }

    fun error(exception: Exception) {
        var text = exception.stackTrace
            .map { "${it.fileName}:${it.className}:${it.methodName}:${it.lineNumber}" }
            .joinToString { it }

        error(text)
    }

    fun error(text: String) {
        status = Status.ERROR
        error = text
        parent?.status = Status.PENDING
    }

    enum class Goal {
        MIGRATION,
        LOAD_PROJECTS,
        FILL_PROJECTS,
        LOAD_PROJECT,
        LOAD_PROJECT_SCHEMA,
        LOAD_PROJECT_ACTORS,
        LOAD_PROJECT_ISSUES,
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
