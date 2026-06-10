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
import com.farmlife.app.config.FactoryConfigs
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
fun FactoryScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val factories by engine.factories.collectAsState()
    val queues by engine.queues.collectAsState()
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
                    Color(0xFFE1F5FE),
                    Color(0xFF81D4FA),
                    Color(0xFF4FC3F7),
                    Color(0xFF29B6F6)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🏭 工坊",
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
                gradient = GradientOcean,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🏭", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "工坊",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${factories.size} 个工厂 · ${queues.size} 个任务",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Text(
                        text = "💰${player?.gold ?: 0}",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FactoryConfigs.ALL.take(3).forEach { factory ->
                    Surface(
                        onClick = {
                            coroutineScope.launch { engine.buyFactory(factory.factoryId) }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GradientOcean),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(factory.icon, fontSize = 14.sp)
                                Text(
                                    "💰${factory.purchasePrice}",
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (factories.isEmpty() && queues.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientOcean,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("🔧", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "还没有工厂",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "购买工厂开始加工产品吧！",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                queues.forEach { queue ->
                    val recipe = FactoryConfigs.RECIPES.firstOrNull { it.recipeId == queue.recipeId }
                    val now = System.currentTimeMillis()
                    val totalTime = queue.finishTime - queue.startTime
                    val progress = ((now - queue.startTime).toFloat() / totalTime).coerceIn(0f, 1f)
                    val isDone = progress >= 1f

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = if (isDone) 2.dp else 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDone) FarmGold.copy(alpha = 0.5f) else Color(0x20000000)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE1F5FE), Color(0xFF81D4FA))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(recipe?.icon ?: "📦", fontSize = 22.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    recipe?.name ?: "生产中",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FarmText
                                )
                                Spacer(Modifier.height(4.dp))
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
                                            .background(if (isDone) FarmGold else FarmBlue)
                                    )
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    if (isDone) "✅ 已完成" else "${(progress * 100).toInt()}%",
                                    fontSize = 9.sp,
                                    color = if (isDone) FarmGold else FarmTextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (isDone) {
                                Surface(
                                    onClick = {
                                        coroutineScope.launch { engine.collectProduction(queue.queueId) }
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
