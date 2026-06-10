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
import com.farmlife.app.config.FactoryConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.launch

/**
 * 工坊界面 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopStatusBar(
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                collectionScore = player?.collectionScore ?: 0,
                season = seasonText,
                weather = weatherEmoji,
                onBack = onBack,
                title = "🏭 工坊"
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
                    Text("🏭", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "工坊",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmBrown
                        )
                        Text(
                            "${factories.size} 个工厂 · ${queues.size} 个生产任务",
                            fontSize = 12.sp,
                            color = FarmTextMuted
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FactoryConfigs.ALL.take(3).forEach { factory ->
                        AnimatedButton(
                            onClick = {
                                coroutineScope.launch { engine.buyFactory(factory.factoryId) }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(factory.icon, fontSize = 16.sp)
                                Text(
                                    "💰${factory.purchasePrice}",
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }

                if (factories.isEmpty() && queues.isEmpty()) {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text("🔧", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "还没有工厂",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FarmBrown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "购买工厂开始加工产品吧！",
                                fontSize = 13.sp,
                                color = FarmTextMuted
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(queues) { queue ->
                            val recipe = FactoryConfigs.RECIPES.firstOrNull { it.recipeId == queue.recipeId }
                            val now = System.currentTimeMillis()
                            val totalTime = queue.finishTime - queue.startTime
                            val progress = ((now - queue.startTime).toFloat() / totalTime).coerceIn(0f, 1f)
                            val isDone = progress >= 1f

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                color = if (isDone) Color(0xFFFFFDE7) else Color.White,
                                shape = RoundedCornerShape(14.dp),
                                shadowElevation = if (isDone) 3.dp else 1.dp,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isDone) 1.5.dp else 0.5.dp,
                                    color = if (isDone) FarmGold else Color(0x20000000)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(recipe?.icon ?: "📦", fontSize = 28.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            recipe?.name ?: "生产中",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(5.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Color(0xFFF0F0F0))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(progress)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(FarmGold)
                                            )
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            if (isDone) "✅ 已完成" else "${(progress * 100).toInt()}%",
                                            fontSize = 11.sp,
                                            color = if (isDone) FarmGold else FarmTextMuted,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    if (isDone) {
                                        AnimatedButton(
                                            onClick = {
                                                coroutineScope.launch { engine.collectProduction(queue.queueId) }
                                            }
                                        ) {
                                            Text("收获", fontSize = 11.sp)
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
}
