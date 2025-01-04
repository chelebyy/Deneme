package com.example.deneme.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.deneme.data.model.ReadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingStatusDropdown(
    currentStatus: ReadingStatus,
    onStatusSelected: (ReadingStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = currentStatus.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Okuma Durumu") }
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Okunacak") },
                onClick = {
                    onStatusSelected(ReadingStatus.TO_READ)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Okunuyor") },
                onClick = {
                    onStatusSelected(ReadingStatus.READING)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Okundu") },
                onClick = {
                    onStatusSelected(ReadingStatus.READ)
                    expanded = false
                }
            )
        }
    }
}
