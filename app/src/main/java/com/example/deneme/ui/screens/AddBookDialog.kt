package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.components.ReadingStatusDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    viewModel: BookViewModel
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var pageCount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ReadingStatus.TO_READ) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Yeni Kitap Ekle")
                
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
                
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth()
                )

                ReadingStatusDropdown(
                    currentStatus = status,
                    onStatusSelected = { status = it }
                )
                
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
                                    category = category.ifEmpty { "Genel" },
                                    status = status
                                )
                                viewModel.addBook(book)
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Ekle")
                    }
                }
            }
        }
    }
}