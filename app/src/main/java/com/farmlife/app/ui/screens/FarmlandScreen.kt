package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.QualitySystem
import com.farmlife.app.domain.logic.TimeSystem
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 农田区屏幕 - 展示土地网格、种植、收获
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmlandScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val landList by engine.lands.collectAsState()
    val crops by engine.crops.collectAsState()
    val player by engine.player.collectAsState()
    var showPlantDialog by remember { mutableStateOf(false) }
    var selectedLandId by remember { mutableStateOf<Long?>(null) }

    // 每秒刷新一次时间进度
    val ticker = produceState(initialValue = 0L) {
        while (true) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }
    ticker.value // 触发重组

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // 顶部操作栏
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch { engine.harvestAllReady() }
                },
                modifier = Modifier.weight(1f)
            ) { Text("一键收获") }
            Button(
                onClick = {
                    coroutineScope.launch { engine.expandFarmland() }
                },
                modifier = Modifier.weight(1f)
            ) { Text("扩建农场") }
            Button(
                onClick = {
                    coroutineScope.launch { engine.petAutoWork() }
                },
                modifier = Modifier.weight(1f)
            ) { Text("宠物工作") }
        }

        // 状态信息
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                player?.let {
                    Text("💰 ${it.gold}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("⭐ Lv.${it.level}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("📦 收藏分: ${it.collectionScore}", fontSize = 14.sp)
                }
            }
        }

        // 土地网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            gridItems(landList) { tile ->
                val crop = crops.firstOrNull { it.landId == tile.landId }
                LandTile(
                    landId = tile.landId,
                    landLevel = tile.level,
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

    // 种植弹窗
    if (showPlantDialog && selectedLandId != null) {
        PlantDialog(
            engine = engine,
            landId = selectedLandId!!,
            onDismiss = { showPlantDialog = false },
            onPlant = { cropId ->
                coroutineScope.launch {
                    engine.plantCrop(selectedLandId!!, cropId)
                    showPlantDialog = false
                }
            }
        )
    }
}

/**
 * 单个土地格子
 */
@Composable
fun LandTile(
    landId: Long,
    landLevel: Int,
    crop: com.farmlife.app.data.entity.CropInstanceEntity?,
    onClick: () -> Unit
) {
    val cropConfig = crop?.let { CropConfigs.getById(it.cropId) }
    val now = System.currentTimeMillis()
    val isReady = crop != null && crop.finishTime <= now
    val progress = if (crop != null && !isReady) {
        val total = (crop.finishTime - crop.plantTime).toFloat()
        if (total > 0) ((now - crop.plantTime) / total).coerceIn(0f, 1f) else 1f
    } else if (isReady) {
        1f
    } else {
        0f
    }

    val bgColor = when {
        isReady -> Color(0xFFFFF59D)
        crop != null -> Color(0xFFA5D6A7)
        else -> Color(0xFF8D6E63)
    }

    val qualityColor = crop?.let {
        when (it.quality) {
            1 -> Color(0xFF4CAF50)
            2 -> Color(0xFF2196F3)
            3 -> Color(0xFF9C27B0)
            4 -> Color(0xFFFFC107)
            5 -> Color(0xFFFF6EC7)
            else -> Color.White
        }
    } ?: Color.White

    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .border(
                width = if (isReady) 2.dp else 1.dp,
                color = if (isReady) Color(0xFFFFEB3B) else Color(0x4D000000),
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (crop != null && cropConfig != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(cropConfig.icon, fontSize = 16.sp)
                if (isReady) {
                    Text("✓", fontSize = 8.sp, color = Color(0xFF2E7D32))
                } else {
                    // 进度条
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(3.dp)
                            .background(Color(0x4DFFFFFF), RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width((progress * 28).dp)
                                .background(qualityColor, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        } else {
            Text("Lv$landLevel", fontSize = 9.sp, color = Color.White)
        }
    }
}

/**
 * 种植选择弹窗
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
    val availableCrops = CropConfigs.ALL.take(20) // 取前20种作展示

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择要种植的作物") },
        text = {
            LazyColumn {
                items(availableCrops) { crop ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                if (player != null && player!!.gold >= crop.sellPrice * 2) {
                                    onPlant(crop.cropId)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(crop.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(crop.name, fontWeight = FontWeight.Bold)
                            val sec = crop.growTimeSeconds
                            val timeStr = if (sec < 60) "${sec}秒"
                            else if (sec < 3600) "${sec / 60}分${sec % 60}秒"
                            else "${sec / 3600}小时${(sec % 3600) / 60}分"
                            Text(
                                "成熟时间: $timeStr | 💰${crop.sellPrice} | ⭐${crop.exp}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text("种子💰${crop.sellPrice * 2}", color = MaterialTheme.colorScheme.primary)
                    }
                    Divider()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}
