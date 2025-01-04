package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onBookAdded: (Book) -> Unit,
    viewModel: com.example.deneme.ui.viewmodel.BookViewModel
) {
    val scope = rememberCoroutineScope()
    var isManualAdd by remember { mutableStateOf(false) }
    var showSearchResults by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ReadingStatus.TO_READ) }

    val searchResults by viewModel.searchResults.collectAsState(initial = emptyList())
    val existingBooks by viewModel.books.collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isManualAdd) {
                    IconButton(onClick = { isManualAdd = false }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
                Text(if (isManualAdd) "Yeni Kitap Ekle" else "Kitap Ara")
                if (!isManualAdd) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Outlined.Close, contentDescription = "Kapat")
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                if (!isManualAdd) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            if (it.length >= 3) {
                                scope.launch {
                                    viewModel.searchBooks(it)
                                }
                                showSearchResults = true
                            } else {
                                showSearchResults = false
                            }
                        },
                        label = { Text("Kitap Adı veya Yazar") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Outlined.Close, "Temizle")
                                }
                            }
                        }
                    )

                    if (showSearchResults) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            items(searchResults) { volumeInfo ->
                                val isBookExists = existingBooks.any { existingBook -> 
                                    existingBook.title == volumeInfo.volumeInfo.title && 
                                    existingBook.author == volumeInfo.volumeInfo.authors?.firstOrNull().orEmpty()
                                }
                                
                                ListItem(
                                    headlineText = { Text(volumeInfo.volumeInfo.title) },
                                    supportingText = { Text(volumeInfo.volumeInfo.authors?.firstOrNull().orEmpty()) },
                                    trailingContent = {
                                        if (isBookExists) {
                                            Text(
                                                "Ekli",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            TextButton(
                                                onClick = { 
                                                    onBookAdded(Book(
                                                        title = volumeInfo.volumeInfo.title,
                                                        author = volumeInfo.volumeInfo.authors?.firstOrNull().orEmpty(),
                                                        pageCount = volumeInfo.volumeInfo.pageCount,
                                                        status = ReadingStatus.TO_READ
                                                    ))
                                                }
                                            ) {
                                                Text("Ekle")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { isManualAdd = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Manuel Ekle")
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Kitap Adı") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = author,
                            onValueChange = { author = it },
                            label = { Text("Yazar") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = pageCount,
                            onValueChange = { pageCount = it },
                            label = { Text("Sayfa Sayısı") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            RadioButton(
                                selected = status == ReadingStatus.TO_READ,
                                onClick = { status = ReadingStatus.TO_READ }
                            )
                            Text("Okunacak")
                            
                            RadioButton(
                                selected = status == ReadingStatus.READING,
                                onClick = { status = ReadingStatus.READING }
                            )
                            Text("Okunuyor")
                            
                            RadioButton(
                                selected = status == ReadingStatus.READ,
                                onClick = { status = ReadingStatus.READ }
                            )
                            Text("Okundu")
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (isManualAdd) {
                TextButton(
                    onClick = {
                        if (title.isNotEmpty() && author.isNotEmpty()) {
                            onBookAdded(
                                Book(
                                    title = title,
                                    author = author,
                                    pageCount = pageCount.toIntOrNull() ?: 0,
                                    status = status
                                )
                            )
                        }
                    }
                ) {
                    Text("Ekle")
                }
            }
        },
        dismissButton = {
            if (isManualAdd) {
                TextButton(onClick = { isManualAdd = false }) {
                    Text("Geri")
                }
            }
        }
    )
} 