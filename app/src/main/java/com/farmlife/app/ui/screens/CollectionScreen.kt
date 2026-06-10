package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*

private fun qualityColor(quality: Int): Color = when (quality) {
    0 -> Color(0xFF9E9E9E)
    1 -> Color(0xFF4CAF50)
    2 -> Color(0xFF2196F3)
    3 -> Color(0xFF9C27B0)
    4 -> Color(0xFFFF9800)
    else -> Color(0xFFE53935)
}

private fun qualityText(quality: Int): String = when (quality) {
    0 -> "普通"
    1 -> "良好"
    2 -> "精良"
    3 -> "稀有"
    4 -> "史诗"
    else -> "传说"
}

@Composable
fun CollectionScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val collections by engine.collections.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()

    val seasonText = when (season) {
        Season.SPRING -> "🌸 春天"
        Season.SUMMER -> "☀️ 夏天"
        Season.AUTUMN -> "🍂 秋天"
        Season.WINTER -> "❄️ 冬天"
    }
    val weatherEmoji = when (weather) {
        Weather.SUNNY -> "☀️"
        Weather.CLOUDY -> "☁️"
        Weather.RAINY -> "🌧️"
        Weather.SNOWY -> "❄️"
        Weather.RAINBOW -> "🌈"
        Weather.METEOR -> "☄️"
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF3E5F5),
                    Color(0xFFE1BEE7),
                    Color(0xFFCE93D8),
                    Color(0xFFAB47BC)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "📚 图鉴",
            gold = player?.gold ?: 0L,
            level = player?.level ?: 1,
            season = seasonText,
            weather = weatherEmoji,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                gradient = GradientPurple,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📚", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "图鉴",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${collections.size} 项收藏",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Text(
                        text = "Lv.${player?.level ?: 1}",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (collections.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientPurple,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("📖", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "图鉴是空的",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "收获作物、收集产品来完成图鉴！",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                collections.forEach { col ->
                    val qColor = qualityColor(col.highestQuality)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            qColor.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(qColor.copy(alpha = 0.15f), qColor.copy(alpha = 0.3f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when (col.collectionType) {
                                        "CROP" -> "🌱"
                                        "ANIMAL" -> "🐄"
                                        "PET" -> "🐕"
                                        else -> "📦"
                                    },
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${col.collectionType} #${col.configId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FarmText
                                )
                                Text(
                                    text = "最高品质: ${qualityText(col.highestQuality)}",
                                    fontSize = 10.sp,
                                    color = FarmTextMuted
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    color = qColor,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        " x${col.totalObtained} ",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
