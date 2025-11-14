package com.lewigh.migratortasksrfb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.*
import org.springframework.boot.runApplication
import org.springframework.context.annotation.*
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.*
import java.util.concurrent.*

@EnableScheduling
@SpringBootApplication
class MigratorTasksRfbApplication {
    @Bean
    fun taskExecutor(): ExecutorService {
        return Executors.newFixedThreadPool(100)
    }


}

fun main(args: Array<String>) {
    runApplication<MigratorTasksRfbApplication>(*args)
}
