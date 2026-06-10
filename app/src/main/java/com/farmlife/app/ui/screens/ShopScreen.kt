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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.config.CropConfigs
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
    1 -> "优秀"
    2 -> "精良"
    3 -> "稀有"
    4 -> "史诗"
    else -> "传说"
}

@Composable
fun ShopScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
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
                    Color(0xFFFFF8E1),
                    Color(0xFFFFECB3),
                    Color(0xFFFFE0B2),
                    Color(0xFFFFCC80)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🏪 商店",
            gold = player?.gold ?: 0L,
            level = player?.level ?: 1,
            season = seasonText,
            weather = weatherEmoji,
            onBack = onBack
        )

        var category by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            // Tab selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("🌱 种子", "🐄 动物").forEachIndexed { index, name ->
                    Surface(
                        onClick = { category = index },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        color = if (category == index) Color.Transparent else Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = if (category == index) 2.dp else 0.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (category == index) FarmGold.copy(alpha = 0.4f) else Color(0x20000000)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (category == index) Modifier.background(GradientGold) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (category == index) Color.White else FarmBrown
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Stats header
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                gradient = GradientGold,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💰", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${player?.gold ?: 0} 金币",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Lv.${player?.level ?: 1}",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Text(
                        text = if (category == 0) "购买种子" else "购买动物",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (category == 0) {
                CropConfigs.ALL.take(20).forEach { crop ->
                    val canAfford = (player?.gold ?: 0) >= crop.seedPrice
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (canAfford) FarmGold.copy(alpha = 0.3f) else Color(0x20000000)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = canAfford) {
                                    if (canAfford) {
                                        coroutineScope.launch {
                                            val lands = engine.lands.value
                                            val crops = engine.crops.value
                                            val freeLand = lands.firstOrNull { land ->
                                                crops.none { it.landId == land.landId }
                                            }
                                            if (freeLand != null) {
                                                engine.plantCrop(freeLand.landId, crop.cropId)
                                            }
                                        }
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFFFFF3E0),
                                                Color(0xFFFFE0B2)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(crop.icon, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = crop.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FarmText
                                )
                                Text(
                                    text = "售价:${crop.sellPrice} · ${crop.growTimeSeconds}秒",
                                    fontSize = 10.sp,
                                    color = FarmTextMuted
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    color = if (canAfford) FarmGold else Color(0xFFBDBDBD),
                                    shape = RoundedCornerShape(FarmRadiusSmall)
                                ) {
                                    Text(
                                        text = " 💰${crop.seedPrice} ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = if (canAfford) "点击购买" else "金币不足",
                                    fontSize = 9.sp,
                                    color = if (canAfford) FarmBrown else Color(0xFFBDBDBD)
                                )
                            }
                        }
                    }
                }
            } else {
                AnimalConfigs.ALL.take(10).forEach { animal ->
                    val canAfford = (player?.gold ?: 0) >= animal.purchasePrice &&
                            (player?.level ?: 0) >= animal.unlockLevel
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (canAfford) FarmOrange.copy(alpha = 0.3f) else Color(0x20000000)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = canAfford) {
                                    if (canAfford) {
                                        coroutineScope.launch { engine.buyAnimal(animal.animalId) }
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFFFFF8E1),
                                                Color(0xFFFFCC80)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(animal.icon, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = animal.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FarmText
                                )
                                Text(
                                    text = "产出:${animal.productName} · Lv.${animal.unlockLevel}",
                                    fontSize = 10.sp,
                                    color = FarmTextMuted
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    color = if (canAfford) FarmOrange else Color(0xFFBDBDBD),
                                    shape = RoundedCornerShape(FarmRadiusSmall)
                                ) {
                                    Text(
                                        text = " 💰${animal.purchasePrice} ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = if (canAfford) "点击购买" else "金币不足",
                                    fontSize = 9.sp,
                                    color = if (canAfford) FarmBrown else Color(0xFFBDBDBD)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
