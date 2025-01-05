package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deneme.data.model.ReadingGoal
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingGoalDialog(
    onDismiss: () -> Unit,
    viewModel: ReadingGoalViewModel
) {
    var targetBooks by remember { mutableStateOf("") }
    val currentGoal by viewModel.currentGoal.collectAsState(initial = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Okuma Hedefi") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentGoal?.let { goal ->
                    Text("Mevcut Hedef: ${goal.targetBooks} kitap")
                    Text("Tamamlanan: ${goal.completedBooks} kitap")
                    LinearProgressIndicator(
                        progress = goal.completedBooks.toFloat() / goal.targetBooks,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = targetBooks,
                    onValueChange = { targetBooks = it },
                    label = { Text("Yıllık Hedef (Kitap Sayısı)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    targetBooks.toIntOrNull()?.let { target ->
                        viewModel.setGoal(target)
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