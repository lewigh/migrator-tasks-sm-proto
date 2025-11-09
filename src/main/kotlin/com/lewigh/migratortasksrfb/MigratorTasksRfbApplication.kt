package com.lewigh.migratortasksrfb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MigratorTasksRfbApplication

fun main(args: Array<String>) {
    runApplication<MigratorTasksRfbApplication>(*args)
}
