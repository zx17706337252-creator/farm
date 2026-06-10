package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 农田界面 - 精美动态版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    // 每秒刷新一次时间进度
    val ticker = produceState(initialValue = 0L) {
        while (isActive) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }
    ticker.value

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
                    title = "🌾 农田"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                // 顶部操作栏 - 精美按钮
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedButton(
                        onClick = { coroutineScope.launch { engine.harvestAllReady() } },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🌾 一键收获", fontSize = 12.sp)
                    }
                    AnimatedButton(
                        onClick = { coroutineScope.launch { engine.expandFarmland() } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FarmBrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("🏗️ 扩建", fontSize = 12.sp)
                    }
                    AnimatedButton(
                        onClick = { coroutineScope.launch { engine.petAutoWork() } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FarmGold,
                            contentColor = Color(0xFF3A2A00)
                        )
                    ) {
                        Text("🐕 宠物", fontSize = 12.sp)
                    }
                }

                // 玩家信息卡片 - 精美渐变
                PremiumCard(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        player?.let { p ->
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💰 ", fontSize = 18.sp)
                                    Text(
                                        "${p.gold}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = FarmGold
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐ ", fontSize = 14.sp)
                                    Text(
                                        "Lv.${p.level}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = FarmGrassGreen
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("🌱 ${landList.size} 块地", fontSize = 12.sp, color = FarmTextMuted)
                                Text(
                                    "🌾 ${crops.size} 株作物",
                                    fontSize = 12.sp,
                                    color = FarmTextMuted
                                )
                            }
                        }
                    }
                }

                // 土地网格 - 动态视觉
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    contentPadding = PaddingValues(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 2000.dp)
                ) {
                    items(landList) { tile ->
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
    }

    // 种植弹窗
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

/**
 * 动态农田格子 - 生长动画、成熟发光、品质颜色
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    // 成熟时的呼吸发光效果
    if (isReady && crop != null) {
        BreathAnimation(minScale = 0.95f, maxScale = 1.15f, durationMillis = 1800) { scale ->
            Surface(
                onClick = onClick,
                modifier = Modifier
                    .size(34.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    ),
                color = Color(0xFFFFF59D),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                border = BorderStroke(1.5.dp, FarmGold)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            cropConfig?.icon ?: "🌱",
                            fontSize = 16.sp
                        )
                        Text("✓", fontSize = 7.sp, color = FarmGrassGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else if (growing && crop != null) {
        // 生长中 - 渐变颜色 + 进度条
        Surface(
            onClick = onClick,
            modifier = Modifier.size(34.dp),
            color = Color(0xFFE8F5E9),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, qColor.copy(alpha = 0.4f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        cropConfig?.icon ?: "🌱",
                        fontSize = 14.sp,
                        modifier = Modifier.graphicsLayer(
                            scaleX = 0.6f + progress * 0.4f,
                            scaleY = 0.6f + progress * 0.4f
                        )
                    )
                    // 进度条
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
        // 空土地 - 点击种植
        Surface(
            onClick = onClick,
            modifier = Modifier.size(34.dp),
            color = Color(0xFFC9A87C),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF8B5E3C).copy(alpha = 0.6f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("➕", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}

/**
 * 种植选择弹窗 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
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
        title = {
            Text("🌱 选择要种植的作物", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.heightIn(max = 350.dp)
                ) {
                    items(availableCrops) { crop ->
                        val canAfford = (player?.gold ?: 0) >= crop.sellPrice * 2
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = if (canAfford) Color(0xFFF5F5F5) else Color(0xFFE8E8E8),
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            border = if (canAfford) BorderStroke(0.5.dp, Color(0x20000000)) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = canAfford) {
                                        if (canAfford) onPlant(crop.cropId)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 作物图标
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFE8F5E9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(crop.icon, fontSize = 24.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        crop.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    val sec = crop.growTimeSeconds
                                    val timeStr = if (sec < 60) "${sec}秒"
                                    else if (sec < 3600) "${sec / 60}分${sec % 60}秒"
                                    else "${sec / 3600}小时${(sec % 3600) / 60}分"
                                    Text(
                                        "⏱ $timeStr | 💰${crop.sellPrice} | ⭐${crop.exp}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FarmTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "💰${crop.sellPrice * 2}",
                                        fontWeight = FontWeight.Bold,
                                        color = FarmGold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            AnimatedButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
