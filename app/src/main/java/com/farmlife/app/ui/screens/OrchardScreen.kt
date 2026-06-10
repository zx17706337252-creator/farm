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
import kotlinx.coroutines.launch

private fun qualityColor(quality: Int): Color = when (quality) {
    0 -> Color(0xFF9E9E9E)
    1 -> Color(0xFF4CAF50)
    2 -> Color(0xFF2196F3)
    3 -> Color(0xFF9C27B0)
    4 -> Color(0xFFFF9800)
    else -> Color(0xFFE53935)
}

@Composable
fun OrchardScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val trees by engine.trees.collectAsState()
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
                    Color(0xFFFFE0B2),
                    Color(0xFFFFCC80),
                    Color(0xFFFFA726),
                    Color(0xFFFB8C00)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🍎 果园",
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
                gradient = GradientSunset,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🍎", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "果园",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${trees.size} 棵果树",
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

            if (trees.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientSunset,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("🌳", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "还没有果树",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "种植果树开始收获果实吧！",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                trees.forEach { tree ->
                    val now = System.currentTimeMillis()
                    val total = tree.nextHarvestTime - tree.plantedTime
                    val progress = if (total > 0 && now < tree.nextHarvestTime) {
                        ((now - tree.plantedTime).toFloat() / total).coerceIn(0f, 1f)
                    } else {
                        1f
                    }
                    val isReady = progress >= 1f
                    val qColor = qualityColor(tree.quality)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = if (isReady) 2.dp else 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isReady) FarmGold.copy(alpha = 0.5f) else qColor.copy(alpha = 0.3f)
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
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFFFE0B2), Color(0xFFFFA726))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🍎", fontSize = 22.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "果树 #${tree.treeId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FarmText
                                )
                                Spacer(Modifier.height(3.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFFE0E0E0))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(progress)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (isReady) FarmGold else qColor)
                                    )
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    if (isReady) "✅ 已成熟" else "${(progress * 100).toInt()}%",
                                    fontSize = 9.sp,
                                    color = if (isReady) FarmGold else FarmTextMuted,
                                    fontWeight = if (isReady) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (isReady) {
                                Surface(
                                    onClick = {
                                        coroutineScope.launch { engine.harvestTree(tree.treeId) }
                                    },
                                    modifier = Modifier
                                        .height(44.dp)
                                        .width(54.dp),
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(FarmRadiusSmall),
                                    shadowElevation = 2.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(GradientGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("收获", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
