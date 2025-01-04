package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.deneme.ui.components.ReadingStatusDropdown
import com.example.deneme.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    onBookUpdated: (Book) -> Unit,
    viewModel: BookViewModel
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var pageCount by remember { mutableStateOf(book.pageCount.toString()) }
    var status by remember { mutableStateOf(book.status) }
    var category by remember { mutableStateOf(book.category) }
    var currentPage by remember { mutableStateOf(book.currentPage.toString()) }
    
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
                
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategori") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = currentPage,
                    onValueChange = { currentPage = it },
                    label = { Text("Mevcut Sayfa") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                ReadingStatusDropdown(
                    currentStatus = status,
                    onStatusSelected = { status = it }
                )
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
                            status = status,
                            category = category,
                            currentPage = currentPage.toIntOrNull() ?: 0
                        )
                        viewModel.updateBook(updatedBook)
                        onBookUpdated(updatedBook)
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