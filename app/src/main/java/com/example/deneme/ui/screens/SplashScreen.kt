package com.example.deneme.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

@Composable
fun SplashScreen() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = with(density) { configuration.screenWidthDp.toDp().toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.toDp().toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val bookWidth = screenWidth * 0.8f
            val bookHeight = screenHeight * 0.1f
            val startX = (screenWidth - bookWidth) / 2
            val startY = (screenHeight - (bookHeight * 6)) / 2

            // En alttaki kitap - Turuncu
            drawRect(
                color = Color(0xFFFF9800),
                topLeft = Offset(startX, startY + (bookHeight * 5)),
                size = Size(bookWidth, bookHeight)
            )

            // 5. kitap - Kırmızı
            drawRect(
                color = Color(0xFFF44336),
                topLeft = Offset(startX, startY + (bookHeight * 4)),
                size = Size(bookWidth, bookHeight)
            )

            // 4. kitap - Mavi
            drawRect(
                color = Color(0xFF2196F3),
                topLeft = Offset(startX, startY + (bookHeight * 3)),
                size = Size(bookWidth, bookHeight)
            )

            // 3. kitap - Sarı
            drawRect(
                color = Color(0xFFFFC107),
                topLeft = Offset(startX, startY + (bookHeight * 2)),
                size = Size(bookWidth, bookHeight)
            )

            // 2. kitap - Yeşil
            drawRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(startX, startY + bookHeight),
                size = Size(bookWidth, bookHeight)
            )

            // En üstteki kitap - Mor
            drawRect(
                color = Color(0xFF9C27B0),
                topLeft = Offset(startX, startY),
                size = Size(bookWidth, bookHeight)
            )
        }
    }
} 