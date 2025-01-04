package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.deneme.R
import com.example.deneme.data.model.Book
import com.example.deneme.data.model.ReadingStatus

@Composable
fun BookList(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    onBookStatusChange: (Book, ReadingStatus) -> Unit,
    onBookDelete: (Book) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                onClick = { onBookClick(book) },
                onStatusChange = { status -> onBookStatusChange(book, status) },
                onDelete = { onBookDelete(book) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    onStatusChange: (ReadingStatus) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete book")
                }
            }
            
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${book.pageCount} pages",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Row {
                    IconButton(
                        onClick = { onStatusChange(ReadingStatus.TO_READ) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_book),
                            contentDescription = "To Read",
                            tint = if (book.status == ReadingStatus.TO_READ) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    IconButton(
                        onClick = { onStatusChange(ReadingStatus.READING) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = "Reading",
                            tint = if (book.status == ReadingStatus.READING) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    IconButton(
                        onClick = { onStatusChange(ReadingStatus.READ) }
                    ) {
                        Icon(Icons.Default.Done, 
                            contentDescription = "Read",
                            tint = if (book.status == ReadingStatus.READ) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}