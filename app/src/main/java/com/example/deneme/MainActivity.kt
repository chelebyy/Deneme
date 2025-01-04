package com.example.deneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.deneme.ui.screens.MainScreen
import com.example.deneme.ui.theme.DenemeTheme
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BookViewModel by viewModels()
    private val readingGoalViewModel: ReadingGoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DenemeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        readingGoalViewModel = readingGoalViewModel
                    )
                }
            }
        }
    }
}