package com.example.progresstracker.ui.model


import androidx.annotation.StringRes

data class Task(
    val id:Int,
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    val type: TaskType = TaskType.STUDY,
    val progress: Int = 0,
    val streaks: Int = 0,
    val dueDate: Long? = null
)

enum class TaskStatus(val statusText: String) {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}
enum class TaskType (val typeText: String) {
    STUDY("Study"), WORK("Work"), BUG("Bug"), FEATURE("Feature"), OTHER("Other")
}


