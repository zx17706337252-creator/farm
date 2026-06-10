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
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.AnimalLevelingSystem
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
    1 -> "优秀"
    2 -> "精良"
    3 -> "稀有"
    4 -> "史诗"
    else -> "传说"
}

@Composable
fun LivestockScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val animals by engine.animals.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()

    val seasonText = when (season) {
        Season.SPRING -> "🌸 春天"
        Season.SUMMER -> "☀️ 夏天"
        Season.AUTUMN -> "🍂 秋天"
        Season.WINTER -> "❄️ 冬天"
    }

    val weatherIcon = when (weather) {
        Weather.SUNNY -> "☀️"
        Weather.CLOUDY -> "☁️"
        Weather.RAINY -> "🌧️"
        Weather.SNOWY -> "❄️"
        Weather.RAINBOW -> "🌈"
        Weather.METEOR -> "✨"
        else -> "🌤️"
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFF3E0),
                    Color(0xFFFFE0B2),
                    Color(0xFFFFCC80),
                    Color(0xFFFFB74D)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🐄 牧场",
            gold = player?.gold ?: 0L,
            level = player?.level ?: 1,
            season = seasonText,
            weather = weatherIcon,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                gradient = GradientSunrise,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐄", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "牧场",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${animals.size} 只动物",
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

            if (animals.isEmpty()) {
                GradientCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientSunrise,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("🐾", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "还没有动物",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "去商店购买可爱的动物吧！",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            } else {
                animals.forEach { animal ->
                    AnimalCard(
                        beast = animal,
                        onCollect = {
                            coroutineScope.launch { engine.collectAnimalProduct(it) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimalCard(
    beast: com.farmlife.app.data.entity.AnimalInstanceEntity,
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
            .padding(vertical = 3.dp),
        color = Color.White.copy(alpha = 0.95f),
        shape = RoundedCornerShape(FarmRadiusMedium),
        shadowElevation = if (isReady) 2.dp else 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isReady) FarmGold.copy(alpha = 0.5f) else qColor.copy(alpha = 0.3f)
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
                    .size(44.dp)
                    .clip(RoundedCornerShape(FarmRadiusSmall))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFF3E0), Color(0xFFFFCC80))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(cfg.icon, fontSize = 24.sp)
            }
            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        cfg.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FarmText
                    )
                    Spacer(Modifier.width(6.dp))
                    Surface(
                        color = qColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            " Lv.${beast.level} ",
                            fontSize = 9.sp,
                            color = qColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    "产出: ${cfg.productName}",
                    fontSize = 10.sp,
                    color = FarmTextMuted
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
                            .background(qColor)
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    if (isReady) "✅ 可收获" else "生长中 ${(progress * 100).toInt()}%",
                    fontSize = 9.sp,
                    color = if (isReady) FarmGold else FarmTextMuted,
                    fontWeight = if (isReady) FontWeight.Bold else FontWeight.Normal
                )
            }
            Spacer(Modifier.width(6.dp))
            Surface(
                onClick = if (isReady) { { onCollect(beast.instanceId) } } else { {} },
                modifier = Modifier
                    .height(44.dp)
                    .width(54.dp),
                color = Color.Transparent,
                shape = RoundedCornerShape(FarmRadiusSmall),
                shadowElevation = if (isReady) 2.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isReady) GradientGold else Brush.horizontalGradient(listOf(Color(0xFFBDBDBD), Color(0xFF9E9E9E))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("收获", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
