package com.lewigh.migratortasksrfb

import com.lewigh.migratortasksrfb.engine.internal.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TaskSchedulerComponent(val service: TaskDispatcher) {

    @Scheduled(fixedRate = 5000)
    fun handle() {
        service.dispatchRound()
    }
}
