package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookList(
    viewModel: BookViewModel,
    onAddBookClick: () -> Unit,
    onEditClick: (Book) -> Unit,
    currentTab: Int,
    showSearchBar: Boolean = false
) {
    var searchQuery by remember { mutableStateOf("") }
    val books by viewModel.books.collectAsState()

    // Arama çubuğu kapandığında sorguyu sıfırla
    LaunchedEffect(showSearchBar) {
        if (!showSearchBar) {
            searchQuery = ""
        }
    }

    // Kitapları duruma göre filtrele
    val filteredBooks = when (currentTab) {
        0 -> books // Tümü
        1 -> books.filter { it.status == ReadingStatus.TO_READ } // Okunacak
        2 -> books.filter { it.status == ReadingStatus.READING } // Okunuyor
        3 -> books.filter { it.status == ReadingStatus.READ } // Okunan
        else -> books
    }.filter { book ->
        if (searchQuery.isBlank()) true
        else {
            book.title.contains(searchQuery, ignoreCase = true) ||
            book.author.contains(searchQuery, ignoreCase = true)
        }
    }

    Column {
        if (showSearchBar) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Kitaplığında ara...") },
                singleLine = true
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredBooks) { book ->
                BookItem(
                    book = book,
                    onEditClick = onEditClick,
                    onDeleteClick = { 
                        viewModel.deleteBook(book)
                        viewModel.loadBooks()
                    },
                    onStatusChange = { newStatus ->
                        val updatedBook = book.copy(status = newStatus)
                        viewModel.updateBook(updatedBook)
                        viewModel.loadBooks()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: Book,
    onEditClick: (Book) -> Unit,
    onDeleteClick: () -> Unit,
    onStatusChange: (ReadingStatus) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${book.pageCount} sayfa",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Durum: ${book.status}",
                style = MaterialTheme.typography.bodySmall
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Düzenle")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Okunacak") },
                            leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                            onClick = {
                                onStatusChange(ReadingStatus.TO_READ)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okunuyor") },
                            leadingIcon = { Icon(Icons.Default.AutoStories, contentDescription = null) },
                            onClick = {
                                onStatusChange(ReadingStatus.READING)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okundu") },
                            leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                            onClick = {
                                onStatusChange(ReadingStatus.READ)
                                showMenu = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Düzenle") },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            onClick = {
                                onEditClick(book)
                                showMenu = false
                            }
                        )
                    }
                }
                
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Sil")
                }
            }
        }
    }
}