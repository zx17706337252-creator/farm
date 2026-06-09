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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.FishConfigs
import com.farmlife.app.domain.engine.FarmEngine
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 鱼塘屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishpondScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val ponds by engine.ponds.collectAsState()
    val fish by engine.fish.collectAsState()
    val player by engine.player.collectAsState()
    var showAddFishDialog by remember { mutableStateOf(false) }
    var selectedPondId by remember { mutableStateOf<Int>(0) }

    produceState(initialValue = 0L) {
        while (true) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("🐟 鱼塘", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp))

        if (player?.level ?: 0 < 40) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("需要等级 40 才能解锁鱼塘", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn {
                items(ponds) { pond ->
                    PondCard(
                        pond = pond,
                        fishInPond = fish.filter { it.pondId == pond.pondId.toInt() },
                        engine = engine,
                        onUnlock = {
                            coroutineScope.launch { engine.unlockPond(pond.pondId) }
                        },
                        onAddFish = {
                            selectedPondId = pond.pondId.toInt()
                            showAddFishDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showAddFishDialog) {
        AlertDialog(
            onDismissRequest = { showAddFishDialog = false },
            title = { Text("选择要放入的鱼") },
            text = {
                LazyColumn {
                    items(FishConfigs.ALL) { fish ->
                        val canBuy = (player?.level ?: 0) >= fish.unlockLevel &&
                                (player?.gold ?: 0) >= fish.sellPrice
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    if (canBuy) {
                                        coroutineScope.launch {
                                            engine.addFish(selectedPondId, fish.fishId)
                                            showAddFishDialog = false
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(fish.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fish.name, fontWeight = FontWeight.Bold)
                                Text("成长: ${fish.growTimeMinutes}分钟 | 售价:💰${fish.sellPrice}",
                                    style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddFishDialog = false }) { Text("取消") }
            }
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
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pond.unlocked) Color(0xFFE3F2FD) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🏊 池塘 ${pond.pondId + 1}", fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                if (!pond.unlocked) {
                    Button(onClick = onUnlock) {
                        Text("解锁 💰10000")
                    }
                } else {
                    Button(onClick = onAddFish) {
                        Text("放鱼")
                    }
                }
            }

            if (pond.unlocked && fishInPond.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(fishInPond) { fish ->
                        val cfg = FishConfigs.getById(fish.configId)
                        FishItem(fish = fish, cfg = cfg, engine = engine) {
                            coroutineScope.launch { engine.collectFish(fish.fishId) }
                        }
                    }
                }
            } else if (pond.unlocked) {
                Text("池塘里还没有鱼，点击'放鱼'放入鱼苗", style = MaterialTheme.typography.bodySmall)
            }
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
    val now = System.currentTimeMillis()
    val isReady = now >= fish.finishTime

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(cfg?.icon ?: "🐟", fontSize = 20.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(cfg?.name ?: "鱼", fontWeight = FontWeight.Bold)
            Text(if (isReady) "🐠 可收获" else "🐟 生长中", style = MaterialTheme.typography.bodySmall)
        }
        if (isReady) {
            Button(onClick = onCollect, modifier = Modifier.size(36.dp), contentPadding = PaddingValues(0.dp)) {
                Text("收")
            }
        }
    }
}
