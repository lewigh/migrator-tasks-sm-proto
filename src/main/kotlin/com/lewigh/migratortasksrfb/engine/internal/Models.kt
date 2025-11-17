package com.lewigh.migratortasksrfb.engine.internal

import com.fasterxml.jackson.databind.*
import com.lewigh.migratortasksrfb.engine.*
import com.lewigh.migratortasksrfb.engine.internal.Task.*


data class CurrentTask(val description: String, val parameters: Any? = null, val domainId: String?) {
}

data class PlannedTask(val goal: Goal, val description: String, val parameters: Any? = null, val domainId: String? = null)

