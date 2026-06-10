package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.AnimalLevelingSystem
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.launch

/**
 * 牧场区屏幕 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivestockScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val animals by engine.animals.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()

    val seasonText = when (season) {
        com.farmlife.app.data.model.Season.SPRING -> "🌸 春天"
        com.farmlife.app.data.model.Season.SUMMER -> "☀️ 夏天"
        com.farmlife.app.data.model.Season.AUTUMN -> "🍂 秋天"
        com.farmlife.app.data.model.Season.WINTER -> "❄️ 冬天"
    }

    val weatherIcon = when (weather) {
        com.farmlife.app.data.model.Weather.SUNNY -> "☀️"
        com.farmlife.app.data.model.Weather.CLOUDY -> "☁️"
        com.farmlife.app.data.model.Weather.RAINY -> "🌧️"
        com.farmlife.app.data.model.Weather.SNOWY -> "❄️"
        com.farmlife.app.data.model.Weather.RAINBOW -> "🌈"
        com.farmlife.app.data.model.Weather.METEOR -> "✨"
        else -> "🌤️"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmBackgroundGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            player?.let { p ->
                TopStatusBar(
                    gold = p.gold,
                    level = p.level,
                    collectionScore = p.collectionScore,
                    season = seasonText,
                    weather = weatherIcon,
                    onBack = onBack,
                    title = "🐄 牧场"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                // 标题区
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("🐄", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "牧场",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmBrown
                        )
                        Text(
                            "${animals.size} 只动物",
                            fontSize = 12.sp,
                            color = FarmTextMuted
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (animals.isEmpty()) {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text("🐾", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "还没有动物",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FarmBrown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "去商店购买一些可爱的动物吧！",
                                fontSize = 13.sp,
                                color = FarmTextMuted
                            )
                        }
                    }
                } else {
                    animals.forEach { animal ->
                        AnimalCard(
                            beast = animal,
                            engine = engine,
                            onCollect = {
                                coroutineScope.launch { engine.collectAnimalProduct(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalCard(
    beast: com.farmlife.app.data.entity.AnimalInstanceEntity,
    engine: FarmEngine,
    onCollect: (Long) -> Unit
) {
    val cfg = AnimalConfigs.getById(beast.animalId)
    if (cfg == null) {
        Text("未知动物", color = Color.Red)
        return
    }
    val now = System.currentTimeMillis()
    val productTime = (cfg.productTimeSeconds * 1000L /
            AnimalLevelingSystem.efficiencyMultiplier(beast.level)).toLong()
    val progress = ((now - beast.lastProduceTime).toFloat() / productTime).coerceIn(0f, 1f)
    val isReady = progress >= 1f
    val qColor = qualityColor(beast.quality)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = if (isReady) Color(0xFFFFFDE7) else Color.White,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = if (isReady) 3.dp else 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isReady) 1.5.dp else 0.5.dp,
            color = if (isReady) FarmGold else qColor.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 动物图标
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFAF0E6)),
                contentAlignment = Alignment.Center
            ) {
                Text(cfg.icon, fontSize = 32.sp)
            }
            Spacer(Modifier.width(12.dp))

            // 信息
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(cfg.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = FarmText)
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        color = qColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            " Lv.${beast.level} ",
                            fontSize = 11.sp,
                            color = qColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "产出: ${cfg.productName}",
                    fontSize = 12.sp,
                    color = FarmTextMuted
                )
                Spacer(Modifier.height(8.dp))
                // 进度条
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFF0F0F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(3.dp))
                            .background(qColor)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isReady) "✅ 可收获!" else "生长中... ${(progress * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = if (isReady) FarmGold else FarmTextMuted,
                    fontWeight = if (isReady) FontWeight.Bold else FontWeight.Normal
                )
            }
            Spacer(Modifier.width(8.dp))
            // 收获按钮
            AnimatedButton(
                onClick = { onCollect(beast.instanceId) },
                enabled = isReady
            ) {
                Text("收获", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
