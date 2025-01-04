package com.example.deneme

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.deneme.ui.screens.MainScreen
import com.example.deneme.ui.theme.DenemeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            Log.e("MainActivity", "Kritik hata: ${e.message}", e)
            Toast.makeText(this, "Kritik Hata: ${e.message}", Toast.LENGTH_LONG).show()
        }

        setContent {
            DenemeTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        try {
                            // Hata ayıklama için log
                            Log.d("MainActivity", "MainScreen başlatılıyor")
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Başlatma hatası: ${e.message}", e)
                            errorMessage = e.message
                        }
                    }

                    if (errorMessage != null) {
                        Text("Hata: $errorMessage")
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}