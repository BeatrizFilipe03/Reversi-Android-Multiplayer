package com.example.cher_g42.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.cher_g42.R

private val boldFont = FontFamily(
    Font(R.font.fredoka_bold)
)
private val semiBoldFont = FontFamily(
    Font(R.font.fredoka_semibold)
)

private val regularFont = FontFamily(
    Font(R.font.fredoka_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = boldFont,
        fontSize = 45.sp
    ),
    titleMedium = TextStyle(
        fontFamily = boldFont,
        fontSize = 40.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = semiBoldFont,
        fontSize = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = semiBoldFont,
        fontSize = 25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = regularFont,
        fontSize = 20.sp
    ),
    labelSmall = TextStyle(
        fontFamily = regularFont,
        fontSize = 16.sp
    )
)