package peresintaion.composepole

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.RequestState
import domain.ToDoTask

@Composable
fun DisplayTasks(
    modifier: Modifier = Modifier,
    tasks: RequestState<List<ToDoTask>>,
    showActive: Boolean = true,
    onSelect: ((ToDoTask) -> Unit)? = null,
    onFavorite: ((ToDoTask, Boolean) -> Unit)? = null,
    onComplete: (ToDoTask, Boolean) -> Unit,
    onDelete: ((ToDoTask) -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete: ToDoTask? by remember { mutableStateOf(null) }

    if (showDialog) {
        AlertDialog(
            title = {
                Text(text = "Delete", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            },
            text = {
                Text(
                    text = "Are you sure you want to remove '${taskToDelete!!.title}' task?",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },
            confirmButton = {
                Button(onClick = {
                    onDelete?.invoke(taskToDelete!!)
                    showDialog = false
                    taskToDelete = null
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        taskToDelete = null
                        showDialog = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            onDismissRequest = {
                taskToDelete = null
                showDialog = false
            }
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = if (showActive) "Active Tasks" else "Completed Tasks",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))
        tasks.DisplayResult(
            onLoading = { LoadingScreen() },
            onError = { ErrorScreen(massage = it) },
            onSuccess = {
                if (it.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                        items(
                            items = it,
                            key = { task -> task._id.toHexString() }
                        ) { task ->
                            TaskView(
                                showActive = showActive,
                                task = task,
                                onSelect = { onSelect?.invoke(task) },
                                onCompleted = { selectedTask, completed ->
                                    onComplete(selectedTask, completed)
                                },
                                onFavorite = { selectedTask, favorite ->
                                    onFavorite?.invoke(selectedTask, favorite)
                                },
                                onDelete = { selectedTask ->
                                    taskToDelete = selectedTask
                                    showDialog = true
                                }
                            )
                        }
                    }
                } else {
                    ErrorScreen()
                }
            }
        )
    }
}