package com.example.progresstracker.ui

import DueDatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progresstracker.model.Task
import com.example.progresstracker.model.TaskStatus
import com.example.progresstracker.model.TaskType
import com.example.progresstracker.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksList(
    uiState: TasksScreenUiState,
    modifier: Modifier = Modifier,
    onDeleteTask: (Int) -> Unit,
    onCancel: () -> Unit,
    onSave: (Task) -> Unit,
    onEditTask: (Int) -> Unit,
) {
    LazyColumn {
        items(uiState.tasks) { task ->
            TaskItem(
                uiState = uiState,
                task = task,
                onDeleteTask = onDeleteTask,
                onCancel = onCancel,
                onSave = onSave,
                onEditTask = onEditTask,
                modifier = modifier
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(
    uiState: TasksScreenUiState,
    task: Task,
    onDeleteTask: (Int) -> Unit,
    onCancel: () -> Unit,
    onEditTask: (Int) -> Unit,
    onSave: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.currentTaskId == task.id) {
        TaskItemEditMode(task = task, onSave = onSave, onCancel = onCancel)
    } else {
        TaskItemViewMode(
            task = task,
            onDeleteTask = onDeleteTask,
            onEditTask = onEditTask,
            modifier = modifier
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TasksApp()
}