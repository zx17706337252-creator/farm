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
import com.farmlife.app.config.FishConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.TimeSystem
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

@Composable
fun FishpondScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val ponds by engine.ponds.collectAsState()
    val fishList by engine.fish.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()
    var showAddFishDialog by remember { mutableStateOf(false) }
    var selectedPondId by remember { mutableStateOf<Long>(0) }

    produceState(initialValue = 0L) {
        while (isActive) {
            delay(1000)
            value = TimeSystem.currentTimeMs()
        }
    }

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
                    Color(0xFFE3F2FD),
                    Color(0xFF90CAF9),
                    Color(0xFF42A5F5),
                    Color(0xFF1976D2)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🐟 鱼塘",
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
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                gradient = GradientDeepSea,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐟", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "鱼塘",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${ponds.size} 个池塘 · ${fishList.size} 条鱼",
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

            if ((player?.level ?: 0) < 40) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientDeepSea,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text("需要等级 40 才能解锁鱼塘",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                ponds.forEach { pond ->
                    PondCard(
                        pond = pond,
                        fishInPond = fishList.filter { it.pondId == pond.pondId },
                        onUnlock = {
                            coroutineScope.launch { engine.unlockPond(pond.pondId) }
                        },
                        onAddFish = {
                            selectedPondId = pond.pondId
                            showAddFishDialog = true
                        },
                        onCollectFish = { fishId ->
                            coroutineScope.launch { engine.collectFish(fishId) }
                        }
                    )
                }
            }
        }
    }

    if (showAddFishDialog) {
        AlertDialog(
            onDismissRequest = { showAddFishDialog = false },
            containerColor = Color(0xFFFFFBF0),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🐟 ", fontSize = 18.sp)
                    Text("选择放入的鱼", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FarmBrown)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    FishConfigs.ALL.forEach { fishConfig ->
                        val canBuy = (player?.level ?: 0) >= fishConfig.unlockLevel &&
                                (player?.gold ?: 0) >= fishConfig.sellPrice
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            color = if (canBuy) Color.White else Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(FarmRadiusSmall),
                            shadowElevation = 1.dp,
                            border = androidx.compose.foundation.BorderStroke(
                                0.5.dp,
                                if (canBuy) FarmBlue.copy(alpha = 0.3f) else Color(0x15000000)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = canBuy) {
                                        if (canBuy) {
                                            coroutineScope.launch {
                                                engine.addFish(selectedPondId, fishConfig.fishId)
                                                showAddFishDialog = false
                                            }
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(FarmRadiusSmall))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFFE3F2FD), Color(0xFF90CAF9))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(fishConfig.icon, fontSize = 18.sp)
                                }
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        fishConfig.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = FarmText
                                    )
                                    Text(
                                        "成长: ${fishConfig.growTimeMinutes}分钟",
                                        fontSize = 9.sp,
                                        color = FarmTextMuted
                                    )
                                }
                                Surface(
                                    color = if (canBuy) FarmBlue else Color(0xFFBDBDBD),
                                    shape = RoundedCornerShape(FarmRadiusSmall)
                                ) {
                                    Text(
                                        " 💰${fishConfig.sellPrice} ",
                                        fontSize = 10.sp,
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
                TextButton(onClick = { showAddFishDialog = false }) {
                    Text("取消", color = FarmBrown, fontSize = 12.sp)
                }
            }
        )
    }
}

@Composable
fun PondCard(
    pond: com.farmlife.app.data.entity.PondInstanceEntity,
    fishInPond: List<com.farmlife.app.data.entity.FishInstanceEntity>,
    onUnlock: () -> Unit,
    onAddFish: () -> Unit,
    onCollectFish: (Long) -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        gradient = GradientDeepSea,
        shape = RoundedCornerShape(FarmRadiusMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "🏊 池塘 ${pond.pondId + 1}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp
                )
                Spacer(Modifier.weight(1f))
                if (!pond.unlocked) {
                    Surface(
                        onClick = onUnlock,
                        modifier = Modifier
                            .height(36.dp)
                            .width(72.dp),
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
                            Text("💰10000", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Surface(
                        onClick = onAddFish,
                        modifier = Modifier
                            .height(36.dp)
                            .width(60.dp),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(FarmRadiusSmall),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GradientOcean),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("放鱼", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (pond.unlocked && fishInPond.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                fishInPond.forEach { fishItem ->
                    val cfg = FishConfigs.getById(fishItem.configId)
                    val now = TimeSystem.currentTimeMs()
                    val isReady = now >= fishItem.finishTime
                    val total = fishItem.finishTime - fishItem.placedTime
                    val progress = if (total > 0 && !isReady) {
                        ((now - fishItem.placedTime).toFloat() / total).coerceIn(0f, 1f)
                    } else {
                        1f
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(FarmRadiusSmall),
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(FarmRadiusSmall))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE3F2FD), Color(0xFF90CAF9))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(cfg?.icon ?: "🐟", fontSize = 14.sp)
                            }
                            Spacer(Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    cfg?.name ?: "鱼",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = FarmText
                                )
                                Spacer(Modifier.height(2.dp))
                                if (!isReady) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(Color(0xFFE0E0E0))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(progress)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(FarmBlue)
                                        )
                                    }
                                }
                                Text(
                                    if (isReady) "🐠 可收获" else "🐟 生长中 ${(progress * 100).toInt()}%",
                                    fontSize = 9.sp,
                                    color = if (isReady) FarmGold else FarmTextMuted,
                                    fontWeight = if (isReady) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (isReady) {
                                Surface(
                                    onClick = { onCollectFish(fishItem.fishId) },
                                    modifier = Modifier
                                        .height(32.dp)
                                        .width(48.dp),
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(FarmRadiusSmall),
                                    shadowElevation = 1.dp
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(GradientGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("收", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (pond.unlocked) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "池塘里还没有鱼，点击'放鱼'放入鱼苗",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
