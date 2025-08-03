package com.example.progresstracker.ui

import androidx.lifecycle.ViewModel
import com.example.progresstracker.ui.model.Task
import com.example.progresstracker.ui.model.TaskStatus
import com.example.progresstracker.ui.model.TaskType
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
    fun addNewTask() {
        val newTaskId = (_uiState.value.tasks.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(id = newTaskId,
            title = "",
            description = "",
            type = TaskType.STUDY, // Default type
            status = TaskStatus.NOT_STARTED, // Default status
            progress = 0, // Default progress
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 // Default due date (7 days from now)
        )

        _uiState.update { currentState ->
            currentState.copy(
                isEditing = true,
                isAddingNewTask = true,
                currentTaskId = newTaskId,
                tasks = listOf(newTask) + currentState.tasks            )
        }
    }

    fun editTask(updatedTask: Task) {
        _uiState.update { currentState ->
            val updatedList = currentState.tasks.map { task ->
                if (task.id == updatedTask.id) updatedTask else task
            }
            currentState.copy(tasks = updatedList, isAddingNewTask = false)
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
        if(_uiState.value.isAddingNewTask) {
            deleteTask(_uiState.value.currentTaskId ?: -1)
        }
        _uiState.update { currentState ->
            currentState.copy(isEditing = false, isAddingNewTask = false, currentTaskId = null)
        }
    }

    fun startEditing(taskId: Int) {
        _uiState.update { currentState ->
            currentState.copy(isEditing = true, currentTaskId = taskId)
        }
    }
}
