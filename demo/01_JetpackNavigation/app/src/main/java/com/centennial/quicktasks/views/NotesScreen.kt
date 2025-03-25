// ui/home/NotesScreen.kt
package com.centennial.quicktasks.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.centennial.quicktasks.data.Note
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog

import com.centennial.quicktasks.viewModel.NotesViewModel


@Composable
fun NotesScreen(navController: NavController, notesViewModel: NotesViewModel = viewModel()) {
    val notes = notesViewModel.notes
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(notes) { note ->
            NoteItem(note, navController)
        }
    }
}

@Composable
fun NoteItem(note: Note, navController: NavController) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("noteDetail/${note.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Text("Due Date: ${note.dueDate}")
        }
    }
}