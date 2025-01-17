package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookList(
    viewModel: BookViewModel,
    onAddBookClick: () -> Unit,
    onEditClick: (Book) -> Unit,
    currentTab: Int,
    showSearchBar: Boolean = false,
    selectedFilter: String? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    val books by viewModel.allBooks.collectAsState()

    println("BookList yeniden compose ediliyor. Kitap sayısı: ${books.size}")

    // Sayfa her açıldığında ve arama çubuğu kapandığında sorguyu sıfırla
    LaunchedEffect(Unit, showSearchBar) {
        searchQuery = ""
    }

    // Kitapları duruma göre filtrele
    var filteredBooks = when (currentTab) {
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

    // Filtreleme uygula
    filteredBooks = when (selectedFilter) {
        "Okunacak" -> books.filter { it.status == ReadingStatus.TO_READ }
        "Okunuyor" -> books.filter { it.status == ReadingStatus.READING }
        "Okundu" -> books.filter { it.status == ReadingStatus.READ }
        "Yazar" -> filteredBooks.sortedBy { it.author }
        "Sayfa" -> filteredBooks.sortedByDescending { it.pageCount }
        else -> filteredBooks
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Kitaplığında ara...") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Temizle")
                        }
                    }
                }
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
                    onDeleteClick = { viewModel.deleteBook(book) },
                    onStatusChange = { newStatus ->
                        viewModel.updateBook(book.copy(status = newStatus))
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
    val isDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) MaterialTheme.colorScheme.surface else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDarkTheme) Color.White else Color(0xFF000000)
                    )
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) Color.White else Color(0xFF000000)
                    )
                    Text(
                        text = "${book.pageCount} sayfa",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.White else Color(0xFF000000)
                    )
                    Text(
                        text = when(book.status) {
                            ReadingStatus.TO_READ -> "Okunacak"
                            ReadingStatus.READING -> "Okunuyor"
                            ReadingStatus.READ -> "Okundu"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.White else Color(0xFF000000)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Default.Edit, 
                                contentDescription = "Düzenle",
                                tint = if (isDarkTheme) Color.White else Color(0xFF000000)
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Okunacak", color = if (isDarkTheme) Color.White else Color.Black) },
                                leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                                onClick = {
                                    onStatusChange(ReadingStatus.TO_READ)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Okunuyor", color = if (isDarkTheme) Color.White else Color.Black) },
                                leadingIcon = { Icon(Icons.Default.AutoStories, contentDescription = null) },
                                onClick = {
                                    onStatusChange(ReadingStatus.READING)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Okundu", color = if (isDarkTheme) Color.White else Color.Black) },
                                leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                                onClick = {
                                    onStatusChange(ReadingStatus.READ)
                                    showMenu = false
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Düzenle", color = if (isDarkTheme) Color.White else Color.Black) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                                onClick = {
                                    onEditClick(book)
                                    showMenu = false
                                }
                            )
                        }
                    }
                    
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = "Sil",
                            tint = if (isDarkTheme) Color.White else Color(0xFF000000)
                        )
                    }
                }
            }
        }
    }
}