package com.example.eserwis

import android.R.attr.thickness
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eserwis.ui.theme.EserwisTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    userRole : String,
    currentUserUID: String,
    department: String?,
    onLogout: () -> Unit,
    viewModel: MainDashboardViewModel = viewModel()
) {

    val faults = viewModel.allFaults.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Główny - $userRole") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Close, contentDescription = "Wyloguj")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if(userRole == UserRoles.MANAGER || userRole == UserRoles.OPERATOR) {
                Button(
                    onClick = { /*TODO: przejscie do zglaszania usterek*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Zgłoś usterkę")
                }
            }

            Text(
                text = "Lista usterek",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = filterFaults(
                        allFaults = viewModel.allFaults.value,
                        role = userRole,
                        currentUserUID = currentUserUID,
                        department = department
                    )
                ) { fault ->
                    FaultListItem(fault, userRole, currentUserUID)
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                }
            }


        }

    }

}

@Composable
fun FaultListItem(
    fault: Fault,
    userRole: String,
    currentUserUID: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { /*TODO: przejscie do szczegolow usterki*/ }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = fault.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Lokalizacja : ${fault.location}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Status : ${fault.status}", style = MaterialTheme.typography.bodySmall)

                if(fault.assignedTo != null && userRole == UserRoles.MANAGER) {
                    Text(text = "Przypisane do : ${fault.assignedTo}", style = MaterialTheme.typography.bodySmall)
                }
            }

            if(userRole == UserRoles.MANAGER && fault.status == "zgłoszone") {
                Button(onClick = { /*TODO: przypisanie do usterki*/ }) {
                    Text(text = "Przypisz")
                }
            } else if (fault.assignedTo == currentUserUID) {
                Button(onClick = { /*TODO: zmiana statusu usterki*/ }) {
                    Text(text = "Zmień status")
                }
            } else {
                Text(
                    text = fault.priority.uppercase(),
                    color = if(fault.priority == "krytyczny") Color.Red else Color.Yellow,
                    style = MaterialTheme.typography.labelSmall
                )
            }




        }
    }
}

fun filterFaults(
    allFaults : List<Fault>,
    role: String,
    currentUserUID: String,
    department: String? = null
): List<Fault> {

    return when (role){

        UserRoles.OPERATOR -> {
            allFaults.filter { it.status != "zakończone" && it.department == department }
        }

        UserRoles.MANAGER -> {
            allFaults.filter { it.department == department && it.status != "zakończone" }
        }

        UserRoles.TECHNICIAN -> {
            allFaults.filter { it.assignedTo == currentUserUID && it.status != "zakończone" }
        }

        else -> emptyList()

    }

}

@Preview(showBackground = true)
@Composable
fun MainDashboardScreenPreview() {
    EserwisTheme {
        MainDashboardScreen("technik", "uid_technik_1", "Hala A", onLogout = {})
    }
}