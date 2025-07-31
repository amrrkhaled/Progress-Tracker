package com.example.progresstracker.ui

import com.example.progresstracker.ui.model.Task

data class TasksScreenUiState (
    val tasks: List<Task> = emptyList(),
    val isEditing: Boolean = true,
    val currentTaskId: Int? = 1,
)