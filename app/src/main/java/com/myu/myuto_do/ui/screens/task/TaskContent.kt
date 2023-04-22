package com.myu.myuto_do.ui.screens.task

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.myu.myuto_do.R
import com.myu.myuto_do.components.PriorityDropDown
import com.myu.myuto_do.data.models.Priority
import com.myu.myuto_do.ui.theme.LARGE_PADDING
import com.myu.myuto_do.ui.theme.MEDIUM_PADDING

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = onTitleChange,
            label = { Text(text = stringResource(id = R.string.title)) },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Divider(
            modifier = Modifier
                .height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(text = stringResource(id = R.string.description)) },
            textStyle = MaterialTheme.typography.body1,
        )
    }
}

@Composable
@Preview
private fun TaskContentPreview() {
    TaskContent(
        title = "",
        onTitleChange = {
            Log.d("TaskContentPreview", "title: " + it)
        },
        description = "",
        onDescriptionChange = {
            Log.d("TaskContentPreview", "description: " + it)

        },
        priority = Priority.LOW,
        onPrioritySelected = {
            Log.d("TaskContentPreview", "priorty: " + it.name)

        }
    )
}