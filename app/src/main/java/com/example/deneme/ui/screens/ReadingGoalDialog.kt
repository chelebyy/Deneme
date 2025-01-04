package com.example.deneme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deneme.ui.viewmodel.GoalProgress
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingGoalDialog(
    viewModel: ReadingGoalViewModel,
    onDismiss: () -> Unit
) {
    var showYearlyGoalInput by remember { mutableStateOf(false) }
    var showMonthlyGoalInput by remember { mutableStateOf(false) }
    
    val yearProgress by viewModel.yearProgress.collectAsState()
    val monthProgress by viewModel.monthProgress.collectAsState()
    val currentYearGoal by viewModel.currentYearGoal.collectAsState(initial = null)
    val currentMonthGoal by viewModel.currentMonthGoal.collectAsState(initial = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Okuma Hedefleri",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.Close, contentDescription = "Kapat")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Yıllık Hedef
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Yıllık Hedef",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            FilledTonalIconButton(
                                onClick = { showYearlyGoalInput = true }
                            ) {
                                Icon(
                                    if (currentYearGoal == null) Icons.Outlined.Add else Icons.Outlined.Edit,
                                    contentDescription = if (currentYearGoal == null) "Hedef Ekle" else "Hedef Düzenle"
                                )
                            }
                        }
                        
                        if (yearProgress != null) {
                            GoalProgressIndicator(progress = yearProgress!!)
                        } else {
                            Text(
                                "Henüz yıllık hedef belirlenmemiş",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Aylık Hedef
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Aylık Hedef",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            FilledTonalIconButton(
                                onClick = { showMonthlyGoalInput = true }
                            ) {
                                Icon(
                                    if (currentMonthGoal == null) Icons.Outlined.Add else Icons.Outlined.Edit,
                                    contentDescription = if (currentMonthGoal == null) "Hedef Ekle" else "Hedef Düzenle"
                                )
                            }
                        }
                        
                        if (monthProgress != null) {
                            GoalProgressIndicator(progress = monthProgress!!)
                        } else {
                            Text(
                                "Henüz aylık hedef belirlenmemiş",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        },
        confirmButton = { }
    )

    if (showYearlyGoalInput) {
        GoalInputDialog(
            title = "Yıllık Hedef",
            currentGoal = currentYearGoal?.targetBooks,
            currentPageGoal = currentYearGoal?.targetPages,
            onDismiss = { showYearlyGoalInput = false },
            onGoalSet = { books, pages ->
                viewModel.setYearlyGoal(books, pages)
                showYearlyGoalInput = false
            }
        )
    }

    if (showMonthlyGoalInput) {
        GoalInputDialog(
            title = "Aylık Hedef",
            currentGoal = currentMonthGoal?.targetBooks,
            currentPageGoal = currentMonthGoal?.targetPages,
            onDismiss = { showMonthlyGoalInput = false },
            onGoalSet = { books, pages ->
                viewModel.setMonthlyGoal(books, pages)
                showMonthlyGoalInput = false
            }
        )
    }
}

@Composable
private fun GoalProgressIndicator(progress: GoalProgress) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Kitap hedefi ilerleme çubuğu
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Kitap: ${progress.completedBooks}/${progress.targetBooks}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "%${progress.bookProgressPercentage.toInt()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            LinearProgressIndicator(
                progress = progress.bookProgressPercentage / 100,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Sayfa hedefi ilerleme çubuğu (varsa)
        progress.pageProgressPercentage?.let { pageProgress ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Sayfa: ${progress.completedPages}/${progress.targetPages}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "%${pageProgress.toInt()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                LinearProgressIndicator(
                    progress = pageProgress / 100,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalInputDialog(
    title: String,
    currentGoal: Int?,
    currentPageGoal: Int?,
    onDismiss: () -> Unit,
    onGoalSet: (books: Int, pages: Int?) -> Unit
) {
    var bookGoal by remember { mutableStateOf(currentGoal?.toString() ?: "") }
    var pageGoal by remember { mutableStateOf(currentPageGoal?.toString() ?: "") }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = bookGoal,
                    onValueChange = { 
                        bookGoal = it.filter { char -> char.isDigit() }
                        showError = false
                    },
                    label = { Text("Kitap Hedefi") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    isError = showError && bookGoal.isEmpty(),
                    supportingText = if (showError && bookGoal.isEmpty()) {
                        { Text("Kitap hedefi gerekli") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pageGoal,
                    onValueChange = { pageGoal = it.filter { char -> char.isDigit() } },
                    label = { Text("Sayfa Hedefi (Opsiyonel)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (bookGoal.isEmpty()) {
                        showError = true
                        return@TextButton
                    }
                    onGoalSet(
                        bookGoal.toInt(),
                        pageGoal.takeIf { it.isNotEmpty() }?.toInt()
                    )
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