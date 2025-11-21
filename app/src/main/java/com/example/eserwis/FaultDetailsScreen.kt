package com.example.eserwis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eserwis.ui.theme.EserwisTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaultDetailsScreen(
    faultId : String,
    userRole: String,
    currentUserUid: String,
    onBack: () -> Unit,
    viewModel: FaultDetailsViewModel = viewModel()
) {

    LaunchedEffect(faultId) {
        viewModel.loadFaultDetails(faultId)
    }

    val fault by viewModel.faultDetails.collectAsState()
    var username by remember { mutableStateOf<String?>(null) }
    var reportedUsername by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }


    LaunchedEffect(fault?.assignedToUid) {
        if (fault?.assignedToUid != null) {
            username = viewModel.getUsername(fault!!.assignedToUid!!)
        }
    }

    LaunchedEffect(fault?.reportedByUid) {
        if (fault?.reportedByUid != null) {
            reportedUsername = viewModel.getUsername(fault!!.reportedByUid)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły usterki") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, contentDescription = "Powrót")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if(fault == null){
                Text("Ładowanie...")
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (userRole == UserRoles.MANAGER || userRole == UserRoles.OPERATOR) {
                            showEditDialog = true
                        }
                    }
                ){
                    Column(modifier = Modifier.padding(16.dp)){
                        Text(
                            text = fault!!.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val priorityEnum = Priority.fromString(fault!!.priority)
                        Text(
                            text = "Priorytet: ${priorityEnum.value}",
                            color = priorityEnum.color,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //szczegoly
                Card(modifier = Modifier.fillMaxWidth()){
                    Column(modifier = Modifier.padding(16.dp)){
                        FaultDetail("Opis", fault!!.description)
                        FaultDetail("Lokalizacja", fault!!.location)
                        FaultDetail("Wydział", fault!!.department)
                        FaultDetail("Status", fault!!.status)
                        FaultDetail("Zgłoszono przez", /*fault!!.reportedByUid*/reportedUsername ?: "NIEZNANY")
                        FaultDetail("Przypisano do", username ?: "NIEPRZYPISANO")
                    }
                }
            }
        }
        if(showEditDialog && fault != null){
            EditFaultDialog(
                fault = fault!!,
                userRole = userRole,
                onDismiss = { showEditDialog = false },
                onFaultUpdated = { updatedFault ->
                    println("DEBUG: FaultDetailsScreen updated fault priority: ${updatedFault.priority}")
                    viewModel.updateFault(faultId, updatedFault)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun FaultDetail(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$title:",
            modifier = Modifier.weight(0.3f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FaultDetailsScreenPreview() {
    EserwisTheme {
        FaultDetailsScreen(
            faultId = "123",
            userRole = "admin",
            currentUserUid = "user123",
            onBack = {}
        )
    }
}