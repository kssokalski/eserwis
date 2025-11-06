package com.example.eserwis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFaultScreen(
    userRole : String,
    currentUserUid : String,
    department : String,
    onBack : () -> Unit,
    onFaultReported : () -> Unit,
    viewModel: ReportFaultViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()
    var showValidationError by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if(state.isSuccess){
            onFaultReported()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zgłoś usterkę") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Powrót")
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
                .verticalScroll(rememberScrollState())
        ) {
            // Tytul
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Tytuł usterki *") },
                isError = showValidationError && state.title.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Opis
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Opis usterki") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lokalizacja
            OutlinedTextField(
                value = state.location,
                onValueChange = { viewModel.onLocationChange(it) },
                label = { Text("Lokalizacja *") },
                isError = state.location.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dzial
            OutlinedTextField(
                value = department,
                onValueChange = { },
                label = { Text("Dział") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Priorytet
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
                        selected = state.priority == priority.name,
                        onClick = { viewModel.onPriorityChange(priority.name) },
                        label = { Text(priority.value) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = priority.color
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Przycisk zgloszenia
            Button(
                onClick = {
                    if (state.title.isBlank() || state.location.isBlank() || state.priority.isBlank()) {
                        showValidationError = true
                    } else {
                        showValidationError = false
                        viewModel.submitFault(
                            currentUserUid = currentUserUid,
                            department = department
                        )
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Zgłoś usterkę")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showValidationError) {
                Text(
                    text = "Wypełnij wszystkie wymagane pola",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Komunikat błędu z ViewModel
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}