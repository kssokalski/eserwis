package com.example.eserwis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
@Composable
fun ChangeStatusDialog(
    fault : Fault,
    onDismiss : () -> Unit,
    onStatusChange : (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zmiena statusu zgłoszenia") },
        text = {
            Column {
                Text("Usterka : ${fault.title}")
                Text("Aktualny status : ${fault.status}")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Wybierz nowy status:")

                val avaiableStatuses = getAvailableStatuses(fault.status)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    avaiableStatuses.forEach { status ->
                        Button(
                            onClick = { onStatusChange(status) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(status)
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

fun getAvailableStatuses(currentStatus: String): List<String> {
    return when (currentStatus) {
        "zgłoszone" -> listOf("w trakcie pracy", "wstrzymane")
        "w trakcie pracy" -> listOf("wstrzymane", "zakończone")
        "wstrzymane" -> listOf("w trakcie pracy", "zakończone")
        else -> emptyList()
    }
}