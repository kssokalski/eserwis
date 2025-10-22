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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eserwis.ui.theme.EserwisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EserwisTheme {
                LoginScreen(
                    onLoginSuccess = {username ->
                        //TODO: przejscie do glownego ekranu aplikacji
                        Log.d("Login", "Zalogowano jako $username")
                    }
                )
            }
        }
    }
}
