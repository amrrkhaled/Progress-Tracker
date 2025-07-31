package com.example.progresstracker.ui

import androidx.lifecycle.ViewModel
import com.example.progresstracker.ui.model.Task
import com.example.progresstracker.ui.model.sampleTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TasksScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TasksScreenUiState())
    val uiState: StateFlow<TasksScreenUiState> = _uiState


    init {
        _uiState.update {
            it.copy(tasks = sampleTasks) // Load sample tasks initially
        }
    }

    fun addTask(task: Task) {
        _uiState.update { currentState ->
            currentState.copy(tasks = currentState.tasks + task)
        }
    }

    fun editTask(updatedTask: Task) {
        _uiState.update { currentState ->
            val updatedList = currentState.tasks.map { task ->
                if (task.id == updatedTask.id) updatedTask else task
            }
            currentState.copy(tasks = updatedList)
        }
        cancelEditing()
    }

    fun deleteTask(taskId: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.tasks.filter { it.id != taskId }
            currentState.copy(tasks = updatedList)
        }
    }

    fun cancelEditing() {
        _uiState.update { currentState ->
            currentState.copy(isEditing = false, currentTaskId = null)
        }
    }

    fun startEditing(taskId: Int) {
        _uiState.update { currentState ->
            currentState.copy(isEditing = true, currentTaskId = taskId)
        }
    }
}
