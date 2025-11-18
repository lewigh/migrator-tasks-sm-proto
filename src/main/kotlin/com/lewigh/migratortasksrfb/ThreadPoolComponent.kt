package com.lewigh.migratortasksrfb

interface ThreadPoolComponent {
    fun launch(block: () -> Unit)
}
