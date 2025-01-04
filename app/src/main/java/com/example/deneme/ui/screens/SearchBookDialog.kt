package com.example.deneme.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBookDialog(
    onDismiss: () -> Unit,
    viewModel: BookViewModel
) {
    var showManualAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    if (showManualAddDialog) {
        ManualAddBookDialog(
            onDismiss = { showManualAddDialog = false },
            viewModel = viewModel
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kitap Ara") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        viewModel.searchBooks(query)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Kitap Adı veya Yazar") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                Button(
                    onClick = { showManualAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manuel Kitap Ekle")
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.searchResults) { book ->
                        SearchResultItem(
                            book = book,
                            onClick = {
                                viewModel.addBook(book)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        }
    )
}

@Composable
fun SearchResultItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
            if (book.pageCount > 0) {
                Text(
                    text = "${book.pageCount} sayfa",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualAddBookDialog(
    onDismiss: () -> Unit,
    viewModel: BookViewModel
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(ReadingStatus.TO_READ) }
    var showStatusMenu by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manuel Kitap Ekle") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Kitap Adı") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Yazar") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = pageCount,
                    onValueChange = { pageCount = it },
                    label = { Text("Sayfa Sayısı") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = when(selectedStatus) {
                            ReadingStatus.TO_READ -> "Okunacak"
                            ReadingStatus.READING -> "Okunuyor"
                            ReadingStatus.READ -> "Okundu"
                        },
                        onValueChange = { },
                        label = { Text("Kategori") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStatusMenu = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Kategori Seç")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenuItem(
                            text = { Text("Okunacak") },
                            leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                            onClick = {
                                selectedStatus = ReadingStatus.TO_READ
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okunuyor") },
                            leadingIcon = { Icon(Icons.Default.AutoStories, contentDescription = null) },
                            onClick = {
                                selectedStatus = ReadingStatus.READING
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okundu") },
                            leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                            onClick = {
                                selectedStatus = ReadingStatus.READ
                                showStatusMenu = false
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        val book = Book(
                            title = title,
                            author = author,
                            pageCount = pageCount.toIntOrNull() ?: 0,
                            status = selectedStatus,
                            category = "Genel",
                            currentPage = 0
                        )
                        viewModel.addBook(book)
                        onDismiss()
                    }
                }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
