package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    onBookUpdated: (Book) -> Unit
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var pageCount by remember { mutableStateOf(book.pageCount.toString()) }
    var status by remember { mutableStateOf(book.status) }
    var notes by remember { mutableStateOf(book.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Edit Book")
                IconButton(onClick = {
                    if (title.isNotEmpty() && author.isNotEmpty()) {
                        onBookUpdated(
                            book.copy(
                                title = title,
                                author = author,
                                pageCount = pageCount.toIntOrNull() ?: 0,
                                status = status,
                                notes = notes.ifEmpty { null }
                            )
                        )
                    }
                }) {
                    Icon(Icons.Outlined.Done, contentDescription = "Save")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = pageCount,
                    onValueChange = { pageCount = it },
                    label = { Text("Page Count") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )

                val items = listOf("To Read", "Reading", "Read")
                val selectedIndex = when(status) {
                    ReadingStatus.TO_READ -> 0
                    ReadingStatus.READING -> 1
                    ReadingStatus.READ -> 2
                }

                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    items.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                status = when(index) {
                                    0 -> ReadingStatus.TO_READ
                                    1 -> ReadingStatus.READING
                                    else -> ReadingStatus.READ
                                }
                            },
                            icon = { },
                            label = { 
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) 
                            }
                        )
                    }
                }
            }
        },
        confirmButton = { },
        dismissButton = { }
    )
}