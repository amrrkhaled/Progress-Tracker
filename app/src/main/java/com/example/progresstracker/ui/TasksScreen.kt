package com.example.progresstracker.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.progresstracker.ui.model.Task
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.progresstracker.ui.model.TaskStatus
import com.example.progresstracker.ui.model.TaskType
import com.example.progresstracker.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksApp() {
    val viewModel: TasksScreenViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    Scaffold(topBar = { AppTopBar() }, content = { innerPadding ->
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
fun AppTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Progress Tracker") }, navigationIcon = {
        Icon(
            imageVector = Icons.Default.Menu, // or your preferred icon
            contentDescription = "App Icon",
            tint = MaterialTheme.colorScheme.onPrimary, // Follow your theme
            modifier = Modifier.padding(8.dp)
        )
    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color(0xFF444444),
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
    )
}


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
    val tasks = uiState.tasks
    LazyColumn {
        items(tasks) { task ->
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
    val isEditing = uiState.currentTaskId == task.id

    if (!isEditing) TaskItemViewMode(
        task = task, onDeleteTask = onDeleteTask, onEditTask = onEditTask, modifier = modifier
    )
    else {
        TaskItemEditMode(
            task = task, onSave = onSave, onCancel = onCancel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItemViewMode(
    task: Task,
    onEditTask: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6E6E6)
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title and Type Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title, style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold, color = Color(0xFF333333)
                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = task.type.name, style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium, color = Color(0xFF888888)
                    )
                )
            }

            Spacer(Modifier.height(8.dp))

            // Description
            Text(
                text = task.description, style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF444444)
                ), maxLines = 2, overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(12.dp))

            // Date and Status Row
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Due: ${formatDate(task.dueDate)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    )
                )
                Text(
                    text = "Status: ${task.status.statusText}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    )
                )
            }

            Spacer(Modifier.height(8.dp))

            // Progress and Icons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress: ${task.progress}%",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    )
                )
                Text(
                    text = "Streaks: ${task.streaks}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666)
                    )
                )
                Row {
                    IconButton(
                        onClick = { onEditTask(task.id) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Task",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { onDeleteTask(task.id) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Task",
                            tint = Color.Gray
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItemEditMode(
    task: Task, onSave: (Task) -> Unit, onCancel: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedProgress by remember { mutableStateOf(task.progress.toFloat()) }
    var editedDueDate by remember { mutableStateOf(task.dueDate) }
    var isCompleted by remember { mutableStateOf(task.status == TaskStatus.COMPLETED) }
    var streakDays by remember { mutableStateOf(task.streaks) }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(task.type) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title Input
            OutlinedTextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF999999), unfocusedBorderColor = Color(0xFF999999)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Input
            OutlinedTextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedType.typeText,
                    onValueChange = {},
                    label = { Text("Select Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TaskType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.typeText) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Progress
            Text(
                text = "Progress: ${editedProgress.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface
            )

            Slider(
                value = editedProgress,
                onValueChange = { editedProgress = it },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF999999), activeTrackColor = Color(0xFF999999)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Streak and Completion Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom streak incrementer
                StreakIncrementer(
                    value = streakDays, onValueChange = { streakDays = it })

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it },
                        colors = CheckboxDefaults.colors(checkedColor = colorScheme.primary)
                    )
                    Text("Mark as completed", color = colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel", color = colorScheme.onSurface)
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSave(
                            task.copy(
                                title = editedTitle,
                                description = editedDescription,
                                progress = editedProgress.toInt(),
                                streaks = streakDays,
                                status = if (isCompleted) TaskStatus.COMPLETED else TaskStatus.IN_PROGRESS,
                                dueDate = editedDueDate,
                                type = selectedType
                            )
                        )
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF999999)
                    )
                ) {
                    Text("Save", color = colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun StreakIncrementer(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
    ) {
        IconButton(
            onClick = { onValueChange(value - 1) }, enabled = value > 0
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = "$value days",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = { onValueChange(value + 1) }, enabled = true
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true, showSystemUi = true)
@Composable
fun TasksScreenPreview() {
    TasksApp()
}

