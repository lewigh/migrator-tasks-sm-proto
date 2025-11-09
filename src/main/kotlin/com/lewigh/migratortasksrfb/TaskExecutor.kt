package com.lewigh.migratortasksrfb

interface TaskExecutor {

    val goal: Task.Goal

    fun execute(current: CurrentTask): List<List<PlannedTask>>
}

data class CurrentTask(val description: String)


interface PlannableTask {
    fun planned(): List<List<PlannedTask>>
}

data class PlannedTask(val goal: Task.Goal, val description: String)

class StagedPlannedTasks(vararg fisrtPriorityTasks: PlannedTask) {

    private val stages: MutableList<List<PlannedTask>> = mutableListOf(fisrtPriorityTasks.toMutableList())

    fun nextStage(vararg nextPriorityTasks: PlannedTask): StagedPlannedTasks {
        stages.add(nextPriorityTasks.toMutableList())
        return this
    }
}
