package com.example.eserwis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eserwis.ui.theme.EserwisTheme

@Composable
fun LoginScreen(
   onLoginSuccess: (String) -> Unit,
   viewModel: LoginViewModel = viewModel()
) {
   val state = viewModel.state.value

   LaunchedEffect(key1 = state.loginSuccess) {
      if (state.loginSuccess) {
         onLoginSuccess(state.username)
      }
   }

   Scaffold { paddingValues ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Top
      ) {

         Spacer(modifier = Modifier.height(64.dp))

         Text(
            text = "E-Serwis",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
         )

         // nazwa uzytkownika
         OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Nazwa użytkownika") },
            isError = state.loginError != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
         )

         Spacer(modifier = Modifier.height(16.dp))

         // haslo
         OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Hasło") },
            isError = state.loginError != null,
            visualTransformation = PasswordVisualTransformation(), // Ukrywa wpisywane znaki
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
         )

         Spacer(modifier = Modifier.height(24.dp))

         // przycisk logowania
         Button(
            onClick = viewModel::login,
            enabled = !state.isLoading, // Wyłączony, gdy trwa ładowanie
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
               Text("Zaloguj")
            }
         }

         Spacer(modifier = Modifier.height(16.dp))

         // komunikat o bledzie
         state.loginError?.let { error ->
            Text(
               text = error,
               color = MaterialTheme.colorScheme.error,
               modifier = Modifier.align(Alignment.Start)
            )
         }
      }
   }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
   EserwisTheme {
      LoginScreen(
         onLoginSuccess = { },
      )
   }
}