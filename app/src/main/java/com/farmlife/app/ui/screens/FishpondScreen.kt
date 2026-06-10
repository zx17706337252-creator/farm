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
import com.farmlife.app.config.FishConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.TimeSystem
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.*

/**
 * 鱼塘屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部状态条
            player?.let { p ->
                TopStatusBar(
                    gold = p.gold,
                    level = p.level,
                    collectionScore = p.collectionScore,
                    season = seasonText,
                    weather = weatherIcon,
                    onBack = onBack,
                    title = "🐟 鱼塘"
                )
            }

            // 内容区
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                if ((player?.level ?: 0) < 40) {
                    PremiumCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("需要等级 40 才能解锁鱼塘", fontSize = 14.sp)
                    }
                } else {
                    ponds.forEach { pond ->
                        PondCard(
                            pond = pond,
                            fishInPond = fishList.filter { it.pondId == pond.pondId },
                            engine = engine,
                            onUnlock = {
                                coroutineScope.launch { engine.unlockPond(pond.pondId) }
                            },
                            onAddFish = {
                                selectedPondId = pond.pondId
                                showAddFishDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddFishDialog) {
        AlertDialog(
            onDismissRequest = { showAddFishDialog = false },
            title = { Text("选择要放入的鱼", fontWeight = FontWeight.Bold, color = FarmBrown) },
            text = {
                LazyColumn {
                    items(FishConfigs.ALL) { fishConfig ->
                        val canBuy = (player?.level ?: 0) >= fishConfig.unlockLevel &&
                                (player?.gold ?: 0) >= fishConfig.sellPrice
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (canBuy) Color(0xFFE8F5E9) else Color(0xFFEEEEEE))
                                .clickable {
                                    if (canBuy) {
                                        coroutineScope.launch {
                                            engine.addFish(selectedPondId, fishConfig.fishId)
                                            showAddFishDialog = false
                                        }
                                    }
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(fishConfig.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fishConfig.name, fontWeight = FontWeight.Bold, color = if (canBuy) FarmText else Color.Gray)
                                Text("成长: ${fishConfig.growTimeMinutes}分钟 | 售价:💰${fishConfig.sellPrice}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FarmTextMuted)
                            }
                        }
                        Divider(color = Color(0x20000000))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddFishDialog = false }) { Text("取消", color = FarmBrown) }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun PondCard(
    pond: com.farmlife.app.data.entity.PondInstanceEntity,
    fishInPond: List<com.farmlife.app.data.entity.FishInstanceEntity>,
    engine: FarmEngine,
    onUnlock: () -> Unit,
    onAddFish: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    PremiumCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🏊 池塘 ${pond.pondId + 1}", fontWeight = FontWeight.Bold, color = FarmBrown)
            Spacer(Modifier.weight(1f))
            if (!pond.unlocked) {
                AnimatedButton(onClick = onUnlock) {
                    Text("解锁 💰10000", fontSize = 12.sp)
                }
            } else {
                AnimatedButton(
                    onClick = onAddFish,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1),
                        contentColor = Color.White
                    )
                ) {
                    Text("放鱼", fontSize = 12.sp)
                }
            }
        }

        if (pond.unlocked && fishInPond.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            fishInPond.forEach { fishItem ->
                val cfg = FishConfigs.getById(fishItem.configId)
                FishItem(fish = fishItem, cfg = cfg, engine = engine) {
                    coroutineScope.launch { engine.collectFish(fishItem.fishId) }
                }
            }
        } else if (pond.unlocked) {
            Spacer(Modifier.height(8.dp))
            Text("池塘里还没有鱼，点击'放鱼'放入鱼苗",
                style = MaterialTheme.typography.bodySmall,
                color = FarmTextMuted)
        }
    }
}

@Composable
fun FishItem(
    fish: com.farmlife.app.data.entity.FishInstanceEntity,
    cfg: com.farmlife.app.config.FishConfig?,
    engine: FarmEngine,
    onCollect: () -> Unit
) {
    val now = TimeSystem.currentTimeMs()
    val isReady = now >= fish.finishTime

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isReady) Color(0xFFFFF8E1) else Color(0xFFE3F2FD))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(cfg?.icon ?: "🐟", fontSize = 20.sp)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(cfg?.name ?: "鱼", fontWeight = FontWeight.Bold, color = FarmText)
            Text(if (isReady) "🐠 可收获" else "🐟 生长中",
                style = MaterialTheme.typography.bodySmall,
                color = FarmTextMuted)
        }
        if (isReady) {
            AnimatedButton(
                onClick = onCollect,
                modifier = Modifier.size(40.dp, 32.dp)
            ) {
                Text("收", fontSize = 12.sp)
            }
        }
    }
}
