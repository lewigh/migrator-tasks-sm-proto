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
    @OneToMany(mappedBy = "parent")
    var children: MutableList<Task> = mutableListOf(),
    var status: Status = Status.PENDING,
    var stage: Int = 0,
    var executed: Boolean = false,
) {

    fun done() {
        parent?.status = Status.PENDING
        status = Status.DONE
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
