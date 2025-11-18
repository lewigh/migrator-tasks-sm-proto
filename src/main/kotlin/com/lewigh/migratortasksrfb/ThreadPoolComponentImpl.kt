package com.lewigh.migratortasksrfb

import org.springframework.stereotype.Component
import java.util.concurrent.*

@Component
class ThreadPoolComponentImpl(private val executor: ExecutorService) : ThreadPoolComponent {

    override fun launch(block: () -> Unit) {
        executor.submit(block)
    }
}
