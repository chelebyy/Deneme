package com.example.deneme.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Book>>(emptyList()) }
    var showManualAddDialog by remember { mutableStateOf(false) }

    // Dialog her açıldığında arama sonuçlarını temizle
    LaunchedEffect(Unit) {
        searchQuery = ""
        searchResults = emptyList()
    }

    Dialog(
        onDismissRequest = {
            searchResults = emptyList() // Dialog kapanırken sonuçları temizle
            onDismiss()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Kitap Ara",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        if (query.isBlank()) {
                            searchResults = emptyList()
                        } else {
                            viewModel.viewModelScope.launch {
                                try {
                                    searchResults = viewModel.searchBooks(query)
                                } catch (e: Exception) {
                                    searchResults = emptyList()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Kitap Adı veya Yazar") },
                    singleLine = true,
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { 
                                searchQuery = ""
                                searchResults = emptyList()
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Temizle")
                            }
                        }
                    }
                )

                Button(
                    onClick = { showManualAddDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Manuel Kitap Ekle")
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { book ->
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
        }
    }

    if (showManualAddDialog) {
        ManualAddBookDialog(
            onDismiss = { showManualAddDialog = false },
            viewModel = viewModel
        )
    }
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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Manuel Kitap Ekle",
                    style = MaterialTheme.typography.titleLarge
                )

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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedTextField(
                        value = when (selectedStatus) {
                            ReadingStatus.TO_READ -> "Okunacak"
                            ReadingStatus.READING -> "Okunuyor"
                            ReadingStatus.READ -> "Okundu"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Durum") },
                        trailingIcon = {
                            IconButton(onClick = { showStatusMenu = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Durum seç")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Okunacak") },
                            onClick = {
                                selectedStatus = ReadingStatus.TO_READ
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okunuyor") },
                            onClick = {
                                selectedStatus = ReadingStatus.READING
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Okundu") },
                            onClick = {
                                selectedStatus = ReadingStatus.READ
                                showStatusMenu = false
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("İptal")
                    }
                    Button(
                        onClick = {
                            if (title.isNotBlank() && author.isNotBlank() && pageCount.isNotBlank()) {
                                val book = Book(
                                    title = title,
                                    author = author,
                                    pageCount = pageCount.toIntOrNull() ?: 0,
                                    status = selectedStatus
                                )
                                viewModel.addBook(book)
                                onDismiss()
                            }
                        },
                        enabled = title.isNotBlank() && author.isNotBlank() && pageCount.isNotBlank()
                    ) {
                        Text("Ekle")
                    }
                }
            }
        }
    }
}
