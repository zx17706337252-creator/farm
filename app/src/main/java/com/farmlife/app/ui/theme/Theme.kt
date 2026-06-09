package com.farmlife.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FarmGreen = Color(0xFF4CAF50)
val FarmBrown = Color(0xFF8D6E63)
val FarmGold = Color(0xFFFFC107)
val FarmSoil = Color(0xFF6D4C41)
val FarmGrass = Color(0xFF7CB342)
val FarmSky = Color(0xFF81D4FA)

private val LightColors = lightColorScheme(
    primary = FarmGreen,
    secondary = FarmBrown,
    tertiary = FarmGold,
    background = Color(0xFFFFF8E1),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121)
)

private val DarkColors = darkColorScheme(
    primary = FarmGreen,
    secondary = FarmBrown,
    tertiary = FarmGold,
    background = Color(0xFF1B1B1B),
    surface = Color(0xFF2D2D2D)
)

private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)

@Composable
fun FarmLifeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
