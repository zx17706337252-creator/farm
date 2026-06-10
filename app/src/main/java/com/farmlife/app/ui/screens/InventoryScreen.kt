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

private fun qualityText(quality: Int): String = when (quality) {
    0 -> "普通"
    1 -> "良好"
    2 -> "精良"
    3 -> "稀有"
    4 -> "史诗"
    else -> "传说"
}

@Composable
fun InventoryScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val inventory by engine.inventory.collectAsState()
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
                    Color(0xFFFFF3E0),
                    Color(0xFFFFE0B2),
                    Color(0xFFFFAB91),
                    Color(0xFFEF5350)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "📦 仓库",
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                GradientCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    gradient = GradientAutumn,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📦 仓库",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${inventory.size} 件物品",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
                Spacer(Modifier.width(6.dp))
                Surface(
                    onClick = { coroutineScope.launch { engine.sellAllInventory() } },
                    modifier = Modifier
                        .height(52.dp)
                        .width(84.dp),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(FarmRadiusMedium),
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GradientGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💰", fontSize = 12.sp)
                            Text(
                                "全部卖出",
                                fontSize = 9.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (inventory.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientAutumn,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("📦", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "仓库是空的",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "收获作物和动物产品，增加仓库！",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                inventory.forEach { item ->
                    val qColor = qualityColor(item.quality)
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
                                val icon = when (item.itemType) {
                                    "CROP" -> "🌱"
                                    "ANIMAL" -> "🥚"
                                    "PROCESSED" -> "📦"
                                    "TREE" -> "🍎"
                                    "FISH" -> "🐟"
                                    else -> "📦"
                                }
                                Text(icon, fontSize = 18.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.itemType} #${item.configId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FarmText
                                )
                                Text(
                                    text = "数量: ${item.quantity}",
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
                                        " ${qualityText(item.quality)} ",
                                        fontSize = 9.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Surface(
                                    onClick = {
                                        coroutineScope.launch { engine.sellInventoryItem(item.itemId, 1) }
                                    },
                                    modifier = Modifier
                                        .height(32.dp)
                                        .width(54.dp),
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(FarmRadiusSmall),
                                    shadowElevation = 1.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(GradientGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("卖出", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
