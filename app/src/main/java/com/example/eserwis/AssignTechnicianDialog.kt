package com.example.eserwis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignTechnicianDialog(
    fault: Fault,
    department: String,
    onDismiss : () -> Unit,
    onTechnicianAssigned : (String) -> Unit,
    viewModel: MainDashboardViewModel = viewModel()
) {
    val technicians by viewModel.technicians.collectAsState()

    LaunchedEffect(department) {
        viewModel.loadTechnicians(department)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Przypisz serwisanta") },
        text =  {
            Column {
                Text("Usterka : ${fault.title}")
                Text("Wybierz serwisanta:")
                Spacer(modifier = Modifier.height(8.dp))

                if(technicians.isEmpty()) {
                    Text("Brak dostępnych serwisantów", style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn(
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(technicians) { technician ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = {
                                    onTechnicianAssigned(technician.uid)
                                    onDismiss()
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        style = MaterialTheme.typography.bodyLarge,
                                        text = technician.username
                                    )
                                    Text(
                                        style = MaterialTheme.typography.bodySmall,
                                        text = technician.department?: "Brak działu",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                        }

                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }

    )
}



