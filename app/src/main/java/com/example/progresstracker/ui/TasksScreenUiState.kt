package com.example.progresstracker.ui

import com.example.progresstracker.model.Task

data class TasksScreenUiState (
    val tasks: List<Task> = emptyList(),
    val isEditing: Boolean = false,
    val isAddingNewTask: Boolean = false,
    val currentTaskId: Int? = null,
)