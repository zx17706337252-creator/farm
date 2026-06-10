package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*

/**
 * 图鉴界面 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopStatusBar(
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                collectionScore = player?.collectionScore ?: 0,
                season = seasonText,
                weather = weatherEmoji,
                onBack = onBack,
                title = "📚 图鉴"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("📚", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "图鉴",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmBrown
                        )
                        Text(
                            "${collections.size} 项收藏",
                            fontSize = 12.sp,
                            color = FarmTextMuted
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (collections.isEmpty()) {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text("📖", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "图鉴是空的",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FarmBrown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "收获作物、收集产品来完成图鉴！",
                                fontSize = 13.sp,
                                color = FarmTextMuted
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
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 1.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                0.5.dp,
                                qColor.copy(alpha = 0.2f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(qColor.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        when (col.collectionType) {
                                            "CROP" -> "🌱"
                                            "ANIMAL" -> "🐄"
                                            "PET" -> "🐕"
                                            else -> "📦"
                                        }, fontSize = 20.sp
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${col.collectionType} #${col.configId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        "最高品质: ${col.highestQuality}",
                                        fontSize = 11.sp,
                                        color = FarmTextMuted
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Surface(
                                        color = qColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            " x${col.totalObtained} ",
                                            fontSize = 12.sp,
                                            color = qColor,
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
}
