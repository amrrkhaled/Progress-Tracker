package com.example.progresstracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksApp() {
    val viewModel: TasksScreenViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    Scaffold(topBar = { AppTopBar(onIconClick = {viewModel.addNewTask()}) }, content = { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TasksList(
                uiState,
                onDeleteTask = { taskId ->
                    viewModel.deleteTask(taskId)
                },
                onCancel = { viewModel.cancelEditing() },
                onEditTask = { taskId ->
                    viewModel.startEditing(taskId)
                },
                onSave = { task ->
                    if (uiState.currentTaskId == null) {
                        viewModel.addTask(task)
                    } else {
                        viewModel.editTask(task)
                    }
                },
            )
        }
    })


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(onIconClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Progress Tracker",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            IconButton(onClick = onIconClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}




@RequiresApi(Build.VERSION_CODES.O)
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true, showSystemUi = true)
@Composable
fun TasksScreenPreview() {
    TasksApp()
}

