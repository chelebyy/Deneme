package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    viewModel: BookViewModel,
    onBookUpdated: () -> Unit
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var pageCount by remember { mutableStateOf(book.pageCount.toString()) }
    var selectedStatus by remember { mutableStateOf(book.status) }
    var showStatusMenu by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kitabı Düzenle") },
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank() && pageCount.isNotBlank()) {
                        val updatedBook = book.copy(
                            title = title,
                            author = author,
                            pageCount = pageCount.toIntOrNull() ?: 0,
                            status = selectedStatus
                        )
                        viewModel.updateBook(updatedBook)
                        onBookUpdated()
                        onDismiss()
                    }
                }
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}