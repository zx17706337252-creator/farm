package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.*

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
fun FarmlandScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val landList by engine.lands.collectAsState()
    val crops by engine.crops.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()
    var showPlantDialog by remember { mutableStateOf(false) }
    var selectedLandId by remember { mutableStateOf<Long?>(null) }

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

    val ticker = produceState(initialValue = 0L) {
        while (isActive) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }
    ticker.value

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE8F5E9),
                    Color(0xFFC8E6C9),
                    Color(0xFFA5D6A7),
                    Color(0xFF81C784)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🌾 农田",
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    onClick = { coroutineScope.launch { engine.harvestAllReady() } },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(FarmRadiusMedium),
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GradientForest),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌾 一键收获", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Surface(
                    onClick = { coroutineScope.launch { engine.expandFarmland() } },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(FarmRadiusMedium),
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF6D4C41), Color(0xFF5D4037))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏗️ 扩建", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Surface(
                    onClick = { coroutineScope.launch { engine.petAutoWork() } },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
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
                        Text("🐕 宠物", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            GlassCard(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                gradient = GradientForest,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💰 ", fontSize = 16.sp)
                            Text(
                                "${player?.gold ?: 0}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐ ", fontSize = 12.sp)
                            Text(
                                "Lv.${player?.level ?: 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.95f)
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("🌱 ${landList.size} 块地", fontSize = 11.sp, color = Color.White.copy(alpha = 0.95f))
                        Text(
                            "🌾 ${crops.size} 株作物",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                contentPadding = PaddingValues(2.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 3000.dp)
            ) {
                items(landList.size) { index ->
                    val tile = landList[index]
                    val crop = crops.firstOrNull { it.landId == tile.landId }
                    FarmTile(
                        landId = tile.landId,
                        crop = crop,
                        onClick = {
                            if (crop == null) {
                                selectedLandId = tile.landId
                                showPlantDialog = true
                            } else {
                                coroutineScope.launch { engine.harvestCrop(crop.instanceId) }
                            }
                        }
                    )
                }
            }
        }
    }

    val landIdForDialog = selectedLandId
    if (showPlantDialog && landIdForDialog != null) {
        PlantDialog(
            engine = engine,
            landId = landIdForDialog,
            onDismiss = { showPlantDialog = false },
            onPlant = { cropId ->
                coroutineScope.launch {
                    engine.plantCrop(landIdForDialog, cropId)
                    showPlantDialog = false
                }
            }
        )
    }
}

@Composable
fun FarmTile(
    landId: Long,
    crop: com.farmlife.app.data.entity.CropInstanceEntity?,
    onClick: () -> Unit
) {
    val cropConfig = crop?.let { CropConfigs.getById(it.cropId) }
    val now = System.currentTimeMillis()
    val isReady = crop != null && crop.finishTime <= now
    val growing = crop != null && crop.finishTime > now
    val progress = if (crop != null) {
        val total = (crop.finishTime - crop.plantTime).toFloat()
        if (total > 0 && !isReady) {
            ((now - crop.plantTime) / total).coerceIn(0f, 1f)
        } else if (isReady) {
            1f
        } else {
            0f
        }
    } else {
        0f
    }

    val qColor = crop?.let { qualityColor(it.quality) } ?: Color.White

    if (isReady && crop != null) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(36.dp),
            color = Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFF9C4), Color(0xFFFFEB3B))
                        )
                    )
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        cropConfig?.icon ?: "🌱",
                        fontSize = 16.sp
                    )
                }
            }
        }
    } else if (growing && crop != null) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(36.dp),
            color = Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 1.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        cropConfig?.icon ?: "🌱",
                        fontSize = 13.sp,
                        modifier = Modifier
                    )
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0x33000000))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width((progress * 24).dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(qColor)
                        )
                    }
                }
            }
        }
    } else {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(36.dp),
            color = Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 1.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFA1887F), Color(0xFF8D6E63))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("➕", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PlantDialog(
    engine: FarmEngine,
    landId: Long,
    onDismiss: () -> Unit,
    onPlant: (Int) -> Unit
) {
    val player by engine.player.collectAsState()
    val availableCrops = CropConfigs.ALL.take(20)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFFBF0),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌱 ", fontSize = 18.sp)
                Text("选择作物", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FarmBrown)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                availableCrops.forEach { crop ->
                    val canAfford = (player?.gold ?: 0) >= crop.sellPrice * 2
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = if (canAfford) Color.White else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(FarmRadiusSmall),
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            0.5.dp,
                            if (canAfford) FarmGreen.copy(alpha = 0.3f) else Color(0x15000000)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = canAfford) {
                                    if (canAfford) onPlant(crop.cropId)
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(crop.icon, fontSize = 18.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    crop.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FarmText
                                )
                                val sec = crop.growTimeSeconds
                                Text(
                                    "⏱ ${sec}秒 | 💰${crop.sellPrice}",
                                    fontSize = 10.sp,
                                    color = FarmTextMuted
                                )
                            }
                            Surface(
                                color = if (canAfford) FarmGreen else Color(0xFFBDBDBD),
                                shape = RoundedCornerShape(FarmRadiusSmall)
                            ) {
                                Text(
                                    " 💰${crop.sellPrice * 2} ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = FarmBrown, fontSize = 12.sp)
            }
        }
    )
}
