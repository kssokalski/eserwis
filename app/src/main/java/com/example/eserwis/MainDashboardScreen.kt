package com.example.eserwis

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    department: String,
    onLogout: () -> Unit,
    viewModel: MainDashboardViewModel = viewModel()
) {

    LaunchedEffect(key1 = currentUserUID, key2 = userRole) {
        viewModel.loadFaults(currentUserUID, userRole, department)
    }

    val allFaults by viewModel.faults.collectAsState()

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
                    items = allFaults
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
    currentUserUID: String,
    viewModel: MainDashboardViewModel = viewModel()
){

    var username by remember(fault.assignedToUid) { mutableStateOf<String?>(null) }

    //pobranie username po pojawieniu sie komponentu
    LaunchedEffect(fault.assignedToUid) {
        if (fault.assignedToUid != null){
            username = viewModel.getUsername(fault.assignedToUid)
        }
    }

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
                /* TODO: FIX $USERNAME */
                if(fault.assignedToUid != null && userRole == UserRoles.MANAGER) {
                    Text(text = "Przypisane do : ${username ?: "Nie przypisano"}", style = MaterialTheme.typography.bodySmall)
                }
            }

            if(userRole == UserRoles.MANAGER && fault.status == "zgłoszone") {
                Button(onClick = { /*TODO: przypisanie do usterki*/ }) {
                    Text(text = "Przypisz")
                }
            } else if (fault.assignedToUid == currentUserUID) {
                Button(onClick = { /*TODO: zmiana statusu usterki*/ }) {
                    Text(text = "Zmień status")
                }
            } else {
                val priorityEnum = Priority.fromString(fault.priority)
                Text(
                    text = priorityEnum.value.uppercase(),
                    color = priorityEnum.color,
                    style = MaterialTheme.typography.labelSmall
                )
            }




        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainDashboardScreenPreview() {
    EserwisTheme {
        MainDashboardScreen("technik", "uid_technik_1", "Hala A", onLogout = {})
    }
}