package com.example.progresstracker.ui.model

val sampleTasks = listOf(
    Task(
        id = 1,
        title = "Learn Kotlin",
        description = "Study basic Kotlin syntax and concepts",
        type = TaskType.STUDY,
        status = TaskStatus.NOT_STARTED,
        progress = 0,

        dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000, // 7 days from now

    ),
    Task(
        id = 2,
        title = "Build UI",
        description = "Create Compose layout for the task list",
        type = TaskType.FEATURE,
        status = TaskStatus.IN_PROGRESS,
        progress = 50,
        streaks = 2,
        dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
    ),
    Task(
        id = 3,
        title = "Test App",
        description = "Test UI and logic integration",
        type = TaskType.BUG,
        status = TaskStatus.COMPLETED,
        progress = 100,
        streaks = 6,
        dueDate = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000
    ),
    Task(
        id = 4,
        title = "Learn Kotlin",
        description = "Study basic Kotlin syntax and concepts",
        type = TaskType.STUDY,
        status = TaskStatus.NOT_STARTED,
        progress = 0,
        dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000, // 7 days from now

    ),
    Task(
        id = 5,
        title = "Build UI",
        description = "Create Compose layout for the task list",
        type = TaskType.FEATURE,
        status = TaskStatus.IN_PROGRESS,
        progress = 50,
        dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
    ),
    Task(
        id = 6,
        title = "Test App",
        description = "Test UI and logic integration",
        type = TaskType.BUG,
        status = TaskStatus.COMPLETED,
        progress = 100,
        dueDate = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000
    )
)
