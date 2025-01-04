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
import com.example.deneme.data.model.Book
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BookViewModel = hiltViewModel()
) {
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showReadingGoalDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var currentTab by remember { mutableStateOf(0) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var showSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            Log.d("MainScreen", "ViewModel başlatılıyor: $viewModel")
            viewModel.loadBooks() // Kitapları yükle
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
        topBar = {
            TopAppBar(
                title = { Text("Kitaplarım") },
                navigationIcon = {
                    if (showSearchBar) {
                        IconButton(onClick = { showSearchBar = false }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "Ara")
                    }
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = { Icon(Icons.Filled.MenuBook, contentDescription = "Tüm Kitaplar") },
                    label = { Text("Tümü") }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = { Icon(Icons.Filled.Bookmark, contentDescription = "Okunacaklar") },
                    label = { Text("Okunacak") }
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = { Icon(Icons.Filled.AutoStories, contentDescription = "Okunuyor") },
                    label = { Text("Okunuyor") }
                )
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    icon = { Icon(Icons.Filled.Book, contentDescription = "Okunanlar") },
                    label = { Text("Okundu") }
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
                        onAddBookClick = { showSearchDialog = true },
                        onEditClick = { book ->
                            selectedBook = book
                            showEditDialog = true
                        },
                        currentTab = currentTab,
                        showSearchBar = showSearchBar
                    )

                    if (showAddBookDialog) {
                        AddBookDialog(
                            onDismiss = { 
                                showAddBookDialog = false
                                viewModel.loadBooks() // Kitapları yeniden yükle
                            },
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
                            onDismiss = { 
                                showSearchDialog = false
                                viewModel.loadBooks() // Kitapları yeniden yükle
                            },
                            viewModel = viewModel
                        )
                    }
                    if (showEditDialog && selectedBook != null) {
                        EditBookDialog(
                            book = selectedBook!!,
                            onDismiss = { 
                                showEditDialog = false
                                selectedBook = null
                                viewModel.loadBooks() // Kitapları yeniden yükle
                            },
                            viewModel = viewModel,
                            onBookUpdated = {
                                showEditDialog = false
                                selectedBook = null
                                viewModel.loadBooks()
                            }
                        )
                    }
                }
            }
        }
    }
}