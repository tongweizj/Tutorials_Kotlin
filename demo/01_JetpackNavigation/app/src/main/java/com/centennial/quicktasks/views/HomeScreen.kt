package com.centennial.quicktasks.views
// ui/home/HomeScreen.kt

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.centennial.quicktasks.AppNavHost
import com.centennial.quicktasks.viewModel.NotesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val notesViewModel: NotesViewModel = viewModel()

    // 使用 Scaffold 包裹整个界面
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addNote") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            notesViewModel = notesViewModel,
            modifier = Modifier.padding(innerPadding)
        )

    }
}