package com.example.deneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deneme.ui.screens.MainScreen
import com.example.deneme.ui.theme.DenemeTheme
import com.example.deneme.ui.viewmodel.BookViewModel
import com.example.deneme.ui.viewmodel.ReadingGoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DenemeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bookViewModel = hiltViewModel<BookViewModel>()
                    val readingGoalViewModel = hiltViewModel<ReadingGoalViewModel>()
                    MainScreen(
                        bookViewModel = bookViewModel,
                        readingGoalViewModel = readingGoalViewModel
                    )
                }
            }
        }
    }
}