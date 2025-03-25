// ui/add/AddNoteScreen.kt
package com.centennial.quicktasks.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.centennial.quicktasks.data.Note
import com.centennial.quicktasks.viewModel.NotesViewModel
import java.time.LocalDate
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavController, notesViewModel: NotesViewModel) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    // Initial state setup for the DatePickerDialog. Specifies to show the picker initially
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    // State to hold the selected date as a String
    val selectedDateLabel = remember { mutableStateOf("") }
    // State to control the visibility of the DatePickerDialog
    val openDialog = remember { mutableStateOf(false) }
    // Define the main color for the calendar picker
    val calendarPickerMainColor = Color(0xFF722276)



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )
            DatePickerDocked(dueDate, onSave = { newDueDate ->
                dueDate = newDueDate
            })
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (title.isNotEmpty() && content.isNotEmpty()) {
                            notesViewModel.addNote(title, content, dueDate)
                            navController.popBackStack("notes", false)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}