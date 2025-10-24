package com.example.eserwis

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eserwis.ui.theme.EserwisTheme

object UserRoles{
    const val MANAGER = "kierownik"
    const val TECHNICIAN = "serwisant"
    const val OPERATOR = "operator"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EserwisTheme {
                var currentUser by remember { mutableStateOf<AuthenticatedUser?>(null) }

                if(currentUser == null){
                    LoginScreen(
                        onLoginSuccess = { user ->
                            currentUser = user
                        }
                    )
                } else {
                    MainDashboardScreen(
                        userRole = currentUser!!.role,
                        currentUserUID = currentUser!!.uid,
                        department = currentUser!!.department,
                        onLogout = {
                            currentUser = null
                        }
                    )
                }
            }
        }
    }
}
