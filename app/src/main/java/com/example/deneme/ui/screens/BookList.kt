package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookList(
    viewModel: BookViewModel,
    onAddBookClick: () -> Unit
) {
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val books by viewModel.books.collectAsState()

    Column {
        TopAppBar(
            title = { Text("Kitaplarım") },
            actions = {
                IconButton(onClick = { showSearchBar = !showSearchBar }) {
                    Icon(Icons.Default.Search, contentDescription = "Ara")
                }
                IconButton(onClick = onAddBookClick) {
                    Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
                }
            }
        )

        if (showSearchBar) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.searchBooks(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Kitap veya yazar ara...") },
                singleLine = true
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(books) { book ->
                BookItem(
                    book = book,
                    onEditClick = { /* TODO: Edit book */ },
                    onDeleteClick = { viewModel.deleteBook(book) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: Book,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                TextButton(onClick = onEditClick) {
                    Text("Düzenle")
                }
                TextButton(onClick = onDeleteClick) {
                    Text("Sil")
                }
            }
        }
    }
}