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
import com.farmlife.app.config.TreeConfigs
import com.farmlife.app.domain.engine.FarmEngine
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 果园屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrchardScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val trees by engine.trees.collectAsState()
    val player by engine.player.collectAsState()
    var showPlantDialog by remember { mutableStateOf(false) }
    var selectedPos by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    produceState(initialValue = 0L) {
        while (true) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("🍎 果园", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("果园需要等级 20 解锁", style = MaterialTheme.typography.bodyMedium)
                Text("果树种植后持续产出，无需重新种植", style = MaterialTheme.typography.bodySmall)
            }
        }

        if (player?.level ?: 0 < 20) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("需要等级 20 才能解锁果园", modifier = Modifier.padding(16.dp))
            }
        } else {
            // 果园网格 5x5
            LazyColumn {
                items(trees) { tree ->
                    val cfg = TreeConfigs.getById(tree.configId) ?: return@items
                    TreeCard(
                        tree = tree,
                        cfg = cfg,
                        onHarvest = {
                            coroutineScope.launch { engine.harvestTree(tree.treeId) }
                        }
                    )
                }
            }

            Button(
                onClick = {
                    selectedPos = Pair(0, 0)
                    showPlantDialog = true
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("种植果树")
            }
        }
    }

    if (showPlantDialog) {
        AlertDialog(
            onDismissRequest = { showPlantDialog = false },
            title = { Text("选择要种植的果树") },
            text = {
                LazyColumn {
                    items(TreeConfigs.ALL) { tree ->
                        val canBuy = (player?.level ?: 0) >= tree.unlockLevel &&
                                (player?.gold ?: 0) >= tree.sellPrice
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    if (canBuy) {
                                        coroutineScope.launch {
                                            engine.plantTree(0, 0, tree.treeId)
                                            showPlantDialog = false
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(tree.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tree.name, fontWeight = FontWeight.Bold)
                                Text("生长: ${tree.growTimeHours}小时 | 收获: ${tree.harvestTimeHours}小时",
                                    style = MaterialTheme.typography.bodySmall)
                            }
                            Text("💰${tree.sellPrice}", color = MaterialTheme.colorScheme.primary)
                        }
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlantDialog = false }) { Text("取消") }
            }
        )
    }
}

@Composable
fun TreeCard(
    tree: com.farmlife.app.data.entity.TreeInstanceEntity,
    cfg: com.farmlife.app.config.TreeConfig,
    onHarvest: () -> Unit
) {
    val now = System.currentTimeMillis()
    val canHarvest = now >= tree.nextHarvestTime
    val progress = if (!canHarvest) {
        val total = (tree.nextHarvestTime - tree.plantedTime).toFloat()
        ((now - tree.plantedTime) / total).coerceIn(0f, 1f)
    } else {
        1f
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (canHarvest) Color(0xFFFFFDE7) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(cfg.icon, fontSize = 36.sp, modifier = Modifier.padding(end = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(cfg.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Lv.${tree.level}", fontSize = 12.sp)
                }
                Text("售价: 💰${cfg.sellPrice} | ⭐${cfg.exp}",
                    style = MaterialTheme.typography.bodySmall)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(8.dp)
                        .background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                if (canHarvest) Color(0xFFFFC107) else Color(0xFF4CAF50),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            Button(onClick = onHarvest, enabled = canHarvest) {
                Text(if (canHarvest) "收获" else "等待中")
            }
        }
    }
}
