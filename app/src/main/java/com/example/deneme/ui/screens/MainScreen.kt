package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.deneme.R
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel

enum class SortOrder {
    TITLE_ASC,
    TITLE_DESC,
    PAGES_ASC,
    PAGES_DESC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BookViewModel,
    readingGoalViewModel: ReadingGoalViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showStatsDialog by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }
    var currentSortOrder by remember { mutableStateOf(SortOrder.TITLE_ASC) }
    
    val books by viewModel.books.collectAsState(initial = emptyList())
    val sortedBooks = when (currentSortOrder) {
        SortOrder.TITLE_ASC -> books.sortedBy { it.title }
        SortOrder.TITLE_DESC -> books.sortedByDescending { it.title }
        SortOrder.PAGES_ASC -> books.sortedBy { it.pageCount }
        SortOrder.PAGES_DESC -> books.sortedByDescending { it.pageCount }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Library") },
                actions = {
                    IconButton(onClick = { showStatsDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chart),
                            contentDescription = "Statistics"
                        )
                    }
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = "Sort"
                        )
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Title (A-Z)") },
                            onClick = {
                                currentSortOrder = SortOrder.TITLE_ASC
                                showSortMenu = false
                            },
                            leadingIcon = { 
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sort),
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Title (Z-A)") },
                            onClick = {
                                currentSortOrder = SortOrder.TITLE_DESC
                                showSortMenu = false
                            },
                            leadingIcon = { 
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sort),
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Pages (Low to High)") },
                            onClick = {
                                currentSortOrder = SortOrder.PAGES_ASC
                                showSortMenu = false
                            },
                            leadingIcon = { 
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sort),
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Pages (High to Low)") },
                            onClick = {
                                currentSortOrder = SortOrder.PAGES_DESC
                                showSortMenu = false
                            },
                            leadingIcon = { 
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sort),
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { 
                        Icon(
                            painter = painterResource(id = R.drawable.ic_book),
                            contentDescription = null
                        )
                    },
                    label = { Text("Books") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { 
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = null
                        )
                    },
                    label = { Text("Reading") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { 
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chart),
                            contentDescription = null
                        )
                    },
                    label = { Text("Goals") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BookList(
                books = sortedBooks,
                onBookClick = { book ->
                    selectedBook = book
                    showEditDialog = true
                },
                onBookStatusChange = { book, status ->
                    viewModel.updateBook(book.copy(status = status))
                },
                onBookDelete = { book ->
                    viewModel.deleteBook(book)
                }
            )
        }
    }

    if (showAddDialog) {
        EditBookDialog(
            book = Book(
                title = "",
                author = "",
                pageCount = 0,
                status = ReadingStatus.TO_READ
            ),
            onDismiss = { showAddDialog = false },
            onBookUpdated = { book ->
                viewModel.addBook(book)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedBook != null) {
        EditBookDialog(
            book = selectedBook!!,
            onDismiss = {
                showEditDialog = false
                selectedBook = null
            },
            onBookUpdated = { book ->
                viewModel.updateBook(book)
                showEditDialog = false
                selectedBook = null
            }
        )
    }

    if (showStatsDialog) {
        StatsDialog(
            books = books,
            onDismiss = { showStatsDialog = false }
        )
    }
}

@Composable
fun StatsDialog(
    books: List<Book>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reading Statistics") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val totalBooks = books.size
                val readBooks = books.count { it.status == ReadingStatus.READ }
                val readingBooks = books.count { it.status == ReadingStatus.READING }
                val toReadBooks = books.count { it.status == ReadingStatus.TO_READ }
                val totalPages = books.sumOf { it.pageCount }
                val readPages = books.filter { it.status == ReadingStatus.READ }
                    .sumOf { it.pageCount }

                StatItem(
                    value = totalBooks,
                    label = "Total Books",
                    iconRes = R.drawable.ic_book
                )
                StatItem(
                    value = readBooks,
                    label = "Read Books",
                    iconRes = R.drawable.ic_book
                )
                StatItem(
                    value = readingBooks,
                    label = "Reading",
                    iconRes = R.drawable.ic_bookmark
                )
                StatItem(
                    value = toReadBooks,
                    label = "To Read",
                    iconRes = R.drawable.ic_book
                )
                StatItem(
                    value = totalPages,
                    label = "Total Pages",
                    iconRes = R.drawable.ic_book
                )
                StatItem(
                    value = readPages,
                    label = "Pages Read",
                    iconRes = R.drawable.ic_book
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = null
    )
}

@Composable
fun StatItem(
    value: Int,
    label: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
            Text(label)
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}