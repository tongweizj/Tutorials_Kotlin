package com.centennial.quicktasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.centennial.quicktasks.viewModel.NotesViewModel
import com.centennial.quicktasks.views.AddNoteScreen
import com.centennial.quicktasks.views.NoteDetailScreen
import com.centennial.quicktasks.views.NotesScreen
import java.time.LocalDate
@Composable
fun AppNavHost(
    navController: NavHostController,
    notesViewModel: NotesViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "notes",
        modifier = modifier
    ) {
        composable("notes") {
            NotesScreen(navController, notesViewModel)
        }
        composable("addNote") {
            AddNoteScreen(navController, notesViewModel)
        }
        composable("noteDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "No ID"

            NoteDetailScreen(
                id = id,
                navController = navController,
                notesViewModel,
                onSave = { id, newTitle, newContent, newDueDate ->
                    notesViewModel.updateNote(id, newTitle, newContent, newDueDate)
                }
            )
        }
    }
}
