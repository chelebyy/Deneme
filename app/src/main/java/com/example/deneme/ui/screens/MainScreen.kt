package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deneme.ui.viewmodel.BookViewModel
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BookViewModel = hiltViewModel()
) {
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showReadingGoalDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var errorState by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            // Hata ayıklama için log
            Log.d("MainScreen", "ViewModel başlatılıyor: $viewModel")
        } catch (e: Exception) {
            Log.e("MainScreen", "ViewModel hatası: ${e.message}", e)
            errorState = "ViewModel Hatası: ${e.message}"
        }
    }

    if (errorState != null) {
        Text("Hata: $errorState")
        return
    }

    val navController = rememberNavController()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSearchDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.MenuBook, contentDescription = "Tüm Kitaplar") },
                    label = { Text("Tümü") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Bookmark, contentDescription = "Okunacaklar") },
                    label = { Text("Okunacak") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.AutoStories, contentDescription = "Okunuyor") },
                    label = { Text("Okunuyor") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Filled.Book, contentDescription = "Okunanlar") },
                    label = { Text("Okunan") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = "book_list"
            ) {
                composable("book_list") {
                    BookList(
                        viewModel = viewModel,
                        onAddBookClick = { showSearchDialog = true }
                    )

                    if (showAddBookDialog) {
                        AddBookDialog(
                            onDismiss = { showAddBookDialog = false },
                            viewModel = viewModel
                        )
                    }
                    if (showReadingGoalDialog) {
                        ReadingGoalDialog(
                            onDismiss = { showReadingGoalDialog = false }
                        )
                    }
                    if (showSearchDialog) {
                        SearchBookDialog(
                            onDismiss = { showSearchDialog = false },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}