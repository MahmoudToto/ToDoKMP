package peresintaion.composepole

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import domain.ToDoTask
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TaskView(
    task: ToDoTask,
    showActive: Boolean = true,
    onSelect: (ToDoTask) -> Unit ,
    onCompleted: (ToDoTask, Boolean) -> Unit,
    onFavorite:  (ToDoTask, Boolean) -> Unit,
    onDelete: (ToDoTask) -> Unit,
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if(showActive) onSelect(task)
                else onDelete(task)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row ( verticalAlignment = Alignment.CenterVertically){
            Checkbox(checked = task.completed,
                onCheckedChange = { onCompleted(task, !task.completed) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(modifier = Modifier.alpha(if (showActive) 1f else 0.5f),
                text = task.title,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = if (showActive) TextDecoration.None
                else TextDecoration.LineThrough
                )
        }
        IconButton(onClick = {
            if (showActive) onFavorite(task, !task.favorite)
            else onDelete(task)
        }
        ) {
            Icon(
                if (showActive) Icons.Default.Star
                else  Icons.Default.Delete,
                contentDescription = "Favorite Icon",
                tint = if (task.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.
                colorScheme.onSurface.copy(
                    alpha = 0.38f)
            )
        }

    }
}