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
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.launch

/**
 * 商店界面 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopStatusBar(
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                collectionScore = player?.collectionScore ?: 0,
                season = seasonText,
                weather = weatherEmoji,
                onBack = onBack,
                title = "🏪 商店"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("🏪", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "商店",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmBrown
                        )
                        Text(
                            "💰 ${player?.gold ?: 0} 金币",
                            fontSize = 12.sp,
                            color = FarmGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                var category by remember { mutableStateOf(0) }
                ScrollableTabRow(
                    selectedTabIndex = category,
                    containerColor = Color.Transparent,
                    contentColor = FarmBrown
                ) {
                    listOf("🌱 种子", "🐄 动物").forEachIndexed { index, name ->
                        Tab(
                            selected = category == index,
                            onClick = { category = index },
                            text = {
                                Text(
                                    name,
                                    fontSize = 13.sp,
                                    fontWeight = if (category == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    if (category == 0) {
                        items(CropConfigs.ALL.take(20)) { crop ->
                            ShopItemCard(
                                icon = crop.icon,
                                name = crop.name,
                                price = crop.sellPrice * 2,
                                subText = "⏱${crop.growTimeSeconds}秒",
                                canAfford = (player?.gold ?: 0) >= crop.sellPrice * 2
                            ) {
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
                    } else {
                        items(AnimalConfigs.ALL.take(10)) { animal ->
                            ShopItemCard(
                                icon = animal.icon,
                                name = animal.name,
                                price = animal.purchasePrice,
                                subText = "Lv.${animal.unlockLevel}解锁",
                                canAfford = (player?.gold ?: 0) >= animal.purchasePrice &&
                                        (player?.level ?: 0) >= animal.unlockLevel
                            ) {
                                coroutineScope.launch { engine.buyAnimal(animal.animalId) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShopItemCard(
    icon: String,
    name: String,
    price: Int,
    subText: String,
    canAfford: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0x15000000))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = canAfford) { if (canAfford) onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FarmText)
                Text(subText, fontSize = 11.sp, color = FarmTextMuted)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "💰$price",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canAfford) FarmGold else Color(0xFFCCCCCC)
                )
                if (canAfford) {
                    Text("点击购买", fontSize = 10.sp, color = FarmGrassGreen)
                } else {
                    Text("金币不足", fontSize = 10.sp, color = Color(0xFFCCCCCC))
                }
            }
        }
    }
}
