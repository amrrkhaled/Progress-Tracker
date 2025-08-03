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
import com.example.progresstracker.ui.model.Task
import com.example.progresstracker.ui.model.TaskStatus
import com.example.progresstracker.ui.model.TaskType
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
@Composable
fun TaskItemViewMode(
    task: Task,
    onEditTask: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val CompletedColor = Color(0xFF4CAF50)     // Green
    val InProgressColor = Color(0xFFFFC107)    // Amber
    val NotStartedColor = Color(0xFFB0BEC5)    // Soft Gray

    val borderColor = when (task.status) {
        TaskStatus.COMPLETED -> CompletedColor
        TaskStatus.IN_PROGRESS -> InProgressColor
        TaskStatus.NOT_STARTED -> NotStartedColor
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    task.type.name,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                task.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Due: ${formatDate(task.dueDate)}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Status: ${task.status.statusText}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Progress: ${task.progress}%", style = MaterialTheme.typography.bodyLarge)
                Row {
                    Text(" ${task.streaks} ", style = MaterialTheme.typography.bodyLarge)
                    AnimatedFireIcon()
                }
                Row {
                    IconButton(onClick = { onEditTask(task.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Task")
                    }
                    IconButton(onClick = { onDeleteTask(task.id) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete Task")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItemEditMode(task: Task, onSave: (Task) -> Unit, onCancel: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedProgress by remember { mutableStateOf(task.progress.toFloat()) }
    var editedDueDate by remember { mutableStateOf(task.dueDate) }
    var isCompleted by remember { mutableStateOf(task.status == TaskStatus.COMPLETED) }
    var streakDays by remember { mutableStateOf(task.streaks) }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(task.type) }
    var showDatePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
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
            Text(
                "Progress: ${editedProgress.toInt()}%",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = editedProgress,
                onValueChange = { editedProgress = it },
                valueRange = 0f..100f
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                StreakIncrementer(value = streakDays, onValueChange = { streakDays = it })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                    Text("Mark as completed")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Due: ${formatDate(editedDueDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(16.dp))
                if (showDatePicker) {
                    DueDatePickerDialog(
                        onDateSelected = { editedDueDate = it },
                        onDismiss = { showDatePicker = false }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDatePicker = true }) {
                        Text("Pick Due Date")
                    }
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onSave(
                            task.copy(
                                title = editedTitle,
                                description = editedDescription,
                                progress = editedProgress.toInt(),
                                streaks = streakDays,
                                status = if (isCompleted) TaskStatus.COMPLETED else if (editedProgress > 0) TaskStatus.IN_PROGRESS else TaskStatus.NOT_STARTED,
                                dueDate = editedDueDate,
                                type = selectedType
                            )
                        )
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TasksApp()
}