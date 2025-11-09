package com.lewigh.migratortasksrfb

interface TaskExecutor {

    val goal: Task.Goal

    fun execute(current: CurrentTask): PlannedTasks
}

data class CurrentTask(val description: String)


data class PlannedTask(val goal: Task.Goal, val description: String)

class PlannedTasks(vararg var firstStage: PlannedTask) {
    private val extraStages: MutableList<List<PlannedTask>> = mutableListOf()

    fun nextStage(vararg nextPriorityTasks: PlannedTask): PlannedTasks {
        if (nextPriorityTasks.isNotEmpty()) {
            extraStages.add(nextPriorityTasks.toMutableList())
        }

        return this
    }

    fun extraStages(): List<List<PlannedTask>> = extraStages

    fun isEmpty(): Boolean = firstStage.isEmpty() && extraStages.isEmpty()
}



