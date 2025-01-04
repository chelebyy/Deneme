package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.deneme.data.model.ReadingGoal
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingGoalDialog(
    onDismiss: () -> Unit,
    viewModel: ReadingGoalViewModel = hiltViewModel()
) {
    var yearlyGoal by remember { mutableStateOf("") }
    var monthlyGoal by remember { mutableStateOf("") }
    val currentGoal by viewModel.currentGoal.collectAsState()

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
                Text(
                    text = "Okuma Hedefleri",
                    style = MaterialTheme.typography.titleLarge
                )

                // Yıllık hedef
                OutlinedTextField(
                    value = yearlyGoal,
                    onValueChange = { yearlyGoal = it },
                    label = { Text("Yıllık Hedef (Kitap Sayısı)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Aylık hedef
                OutlinedTextField(
                    value = monthlyGoal,
                    onValueChange = { monthlyGoal = it },
                    label = { Text("Aylık Hedef (Kitap Sayısı)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Mevcut durum
                currentGoal?.let { goal ->
                    Text(
                        text = "Yıllık İlerleme: ${goal.completedBooks}/${goal.targetBooks} kitap",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LinearProgressIndicator(
                        progress = if (goal.targetBooks > 0) 
                            goal.completedBooks.toFloat() / goal.targetBooks 
                        else 0f,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                            val yearly = yearlyGoal.toIntOrNull() ?: 0
                            val monthly = monthlyGoal.toIntOrNull() ?: 0
                            
                            if (yearly > 0) {
                                viewModel.updateYearlyGoal(yearly)
                            }
                            if (monthly > 0) {
                                viewModel.updateMonthlyGoal(monthly)
                            }
                            
                            onDismiss()
                        }
                    ) {
                        Text("Kaydet")
                    }
                }
            }
        }
    }
}