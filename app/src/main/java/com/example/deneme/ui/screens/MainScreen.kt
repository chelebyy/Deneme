package com.example.deneme.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    bookViewModel: BookViewModel,
    readingGoalViewModel: ReadingGoalViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var selectedStatus by remember { mutableStateOf<ReadingStatus?>(null) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kitaplığım") },
                actions = {
                    // Filtreleme Menüsü
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtrele")
                        }
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Yazara Göre Sırala") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                onClick = {
                                    selectedFilter = if (selectedFilter == "Yazar") null else "Yazar"
                                    showFilterMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sayfaya Göre Sırala") },
                                leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                                onClick = {
                                    selectedFilter = if (selectedFilter == "Sayfa") null else "Sayfa"
                                    showFilterMenu = false
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Filtreleri Temizle") },
                                leadingIcon = { Icon(Icons.Default.Clear, contentDescription = null) },
                                onClick = {
                                    selectedFilter = null
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                    IconButton(onClick = { backupBooks(context, bookViewModel) }) {
                        Icon(Icons.Default.Save, contentDescription = "Yedekle")
                    }
                    IconButton(onClick = { restoreBooks(context, bookViewModel) }) {
                        Icon(Icons.Default.Restore, contentDescription = "Geri Yükle")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                BookList(
                    viewModel = bookViewModel,
                    onEditClick = { book ->
                        selectedBook = book
                        showEditDialog = true
                    },
                    onAddBookClick = { showAddDialog = true },
                    currentTab = when(selectedStatus) {
                        ReadingStatus.TO_READ -> 1
                        ReadingStatus.READING -> 2
                        ReadingStatus.READ -> 3
                        else -> 0
                    },
                    showSearchBar = true,
                    selectedFilter = selectedFilter
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Tüm Kitaplar") },
                    label = { Text("Tümü") },
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Okunacak") },
                    label = { Text("Okunacak") },
                    selected = selectedStatus == ReadingStatus.TO_READ,
                    onClick = { selectedStatus = if (selectedStatus == ReadingStatus.TO_READ) null else ReadingStatus.TO_READ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = "Okunuyor") },
                    label = { Text("Okunuyor") },
                    selected = selectedStatus == ReadingStatus.READING,
                    onClick = { selectedStatus = if (selectedStatus == ReadingStatus.READING) null else ReadingStatus.READING }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Book, contentDescription = "Okundu") },
                    label = { Text("Okundu") },
                    selected = selectedStatus == ReadingStatus.READ,
                    onClick = { selectedStatus = if (selectedStatus == ReadingStatus.READ) null else ReadingStatus.READ }
                )
            }
        }
    )

    if (showAddDialog) {
        AddBookDialog(
            onDismiss = { showAddDialog = false },
            viewModel = bookViewModel
        )
    }

    if (showEditDialog && selectedBook != null) {
        EditBookDialog(
            book = selectedBook!!,
            onDismiss = { showEditDialog = false },
            viewModel = bookViewModel,
            onBookUpdated = { showEditDialog = false }
        )
    }
}

private fun backupBooks(context: Context, viewModel: BookViewModel) {
    val backupFile = File(context.getExternalFilesDir(null), "books_backup.json")
    viewModel.backupBooks(backupFile)
}

private fun restoreBooks(context: Context, viewModel: BookViewModel) {
    val backupFile = File(context.getExternalFilesDir(null), "books_backup.json")
    if (backupFile.exists()) {
        viewModel.restoreBooks(backupFile)
    }
}