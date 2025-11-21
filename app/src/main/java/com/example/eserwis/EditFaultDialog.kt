package com.example.eserwis


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFaultDialog(
    fault: Fault,
    userRole : String,
    onDismiss: () -> Unit,
    onFaultUpdated: (Fault) -> Unit,
    viewModel: MainDashboardViewModel = viewModel()
) {

    var editedFault by remember { mutableStateOf(fault.copy()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(editedFault.priority) {
        println("DEBUG: Priority set to ${editedFault.priority}")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj usterkę") },
        text = {
            Column(
                modifier = Modifier
                    //.verticalScroll(rememberScrollState())
                    .heightIn(max = 500.dp)
            ) {
                OutlinedTextField(
                    value = editedFault.title,
                    onValueChange = { editedFault = editedFault.copy(title = it) },
                    label = {Text("Tytuł")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedFault.description,
                    onValueChange = { editedFault = editedFault.copy(description = it) },
                    label = {Text("Opis")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedFault.location,
                    onValueChange = { editedFault = editedFault.copy(location = it) },
                    label = {Text("Lokalizacja")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if(userRole == UserRoles.OPERATOR || userRole == UserRoles.MANAGER) {
                    Text(
                        text = "Priorytet",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Priority.entries.forEach { priority ->
                            FilterChip(
                                selected = editedFault.priority == priority.name,
                                onClick = {
                                    println("DEBUG: Clicked ${priority.name}")
                                    editedFault = editedFault.copy(priority = priority.name)
                                    println("DEBUG: New priority set to ${editedFault.priority}")
                                },
                                label = { Text(priority.value) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = priority.color
                                )
                            )
                        }
                    }
                }

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

            }
        },
        confirmButton = {
            Row{
                TextButton(onClick = onDismiss){
                    Text(text = "Anuluj")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        println("DEBUG: Save button clicked with priority: ${editedFault.priority}")
                        if (editedFault.title.isBlank() || editedFault.location.isBlank()) {
                            errorMessage = "Wypełnij wszystkie wymagane pola"
                        } else {
                            isLoading = true
                            println("DEBUG: calling onFaultUpdated with priority ${editedFault.priority}")
                            onFaultUpdated(editedFault)
                        }
                    },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(text = "Zapisz")
                    }
                }
            }
        }
    )

}