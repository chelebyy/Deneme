package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BookViewModel,
    readingGoalViewModel: ReadingGoalViewModel
) {
    var currentTab by remember { mutableStateOf(0) }
    var showSearchBar by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kitaplığım") },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (showSearchBar) "Aramayı Kapat" else "Ara"
                        )
                    }
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtrele")
                        }
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Tümü") },
                                onClick = {
                                    selectedFilter = null
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Okunacak") },
                                onClick = {
                                    selectedFilter = "Okunacak"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Okunuyor") },
                                onClick = {
                                    selectedFilter = "Okunuyor"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Okundu") },
                                onClick = {
                                    selectedFilter = "Okundu"
                                    showFilterMenu = false
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Yazara Göre Sırala") },
                                onClick = {
                                    selectedFilter = "Yazar"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sayfa Sayısına Göre Sırala") },
                                onClick = {
                                    selectedFilter = "Sayfa"
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Tümü") },
                    label = { Text("Tümü") },
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Okunacak") },
                    label = { Text("Okunacak") },
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = "Okunuyor") },
                    label = { Text("Okunuyor") },
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Okunan") },
                    label = { Text("Okunan") },
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BookList(
                viewModel = viewModel,
                onAddBookClick = { showAddDialog = true },
                onEditClick = { book ->
                    selectedBook = book
                    showEditDialog = true
                },
                currentTab = currentTab,
                showSearchBar = showSearchBar,
                selectedFilter = selectedFilter
            )
        }
    }

    if (showAddDialog) {
        SearchBookDialog(
            onDismiss = { showAddDialog = false },
            viewModel = viewModel
        )
    }

    if (showEditDialog && selectedBook != null) {
        EditBookDialog(
            book = selectedBook!!,
            onDismiss = {
                showEditDialog = false
                selectedBook = null
            },
            viewModel = viewModel,
            onBookUpdated = {
                showEditDialog = false
                selectedBook = null
            }
        )
    }
}