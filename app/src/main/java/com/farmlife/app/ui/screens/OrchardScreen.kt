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
import kotlinx.coroutines.launch

/**
 * 果园界面 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopStatusBar(
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                collectionScore = player?.collectionScore ?: 0,
                season = seasonText,
                weather = weatherEmoji,
                onBack = onBack,
                title = "🍎 果园"
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
                    Text("🍎", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "果园",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmPink
                        )
                        Text(
                            "${trees.size} 棵果树",
                            fontSize = 12.sp,
                            color = FarmTextMuted
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (trees.isEmpty()) {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text("🌳", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "还没有果树",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FarmBrown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "种植果树开始收获果实吧！",
                                fontSize = 13.sp,
                                color = FarmTextMuted
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(trees) { tree ->
                            val now = System.currentTimeMillis()
                            val total = tree.nextHarvestTime - tree.plantedTime
                            val progress = if (total > 0 && now < tree.nextHarvestTime) {
                                ((now - tree.plantedTime).toFloat() / total).coerceIn(0f, 1f)
                            } else {
                                1f
                            }
                            val isReady = progress >= 1f

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                color = if (isReady) Color(0xFFFFFDE7) else Color.White,
                                shape = RoundedCornerShape(12.dp),
                                shadowElevation = if (isReady) 3.dp else 1.dp,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isReady) 1.5.dp else 0.5.dp,
                                    if (isReady) FarmGold else Color(0x15000000)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🍎", fontSize = 24.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "果树 #${tree.treeId}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
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
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            if (isReady) "✅ 已成熟" else "${(progress * 100).toInt()}%",
                                            fontSize = 10.sp,
                                            color = if (isReady) FarmGold else FarmTextMuted
                                        )
                                    }
                                    if (isReady) {
                                        AnimatedButton(
                                            onClick = {
                                                coroutineScope.launch { engine.harvestTree(tree.treeId) }
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
