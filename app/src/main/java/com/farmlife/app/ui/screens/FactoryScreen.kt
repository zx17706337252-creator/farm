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
import com.farmlife.app.config.FactoryConfigs
import com.farmlife.app.domain.engine.FarmEngine
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 加工区屏幕 - 展示工厂、生产队列
 */
@Composable
fun FactoryScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val factories by engine.factories.collectAsState()
    val queues by engine.queues.collectAsState()
    val player by engine.player.collectAsState()

    // 进度刷新
    produceState(initialValue = 0L) {
        while (true) {
            delay(1000)
            value = System.currentTimeMillis()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("🏭 加工工坊", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp))

        if (factories.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("还没有建筑，去商店购买一个加工建筑吧！", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn {
                items(factories) { factory ->
                    FactoryCard(factory, engine, queues.filter { it.factoryId == factory.factoryId },
                        onStartProduction = { queue ->
                            coroutineScope.launch { engine.startProduction(factory.factoryId, queue) }
                        },
                        onCollect = { qid ->
                            coroutineScope.launch { engine.collectProduction(qid) }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactoryCard(
    factory: com.farmlife.app.data.entity.FactoryInstanceEntity,
    engine: FarmEngine,
    queues: List<com.farmlife.app.data.entity.ProductionQueueEntity>,
    onStartProduction: (Int) -> Unit,
    onCollect: (Long) -> Unit
) {
    val factoryTypeId = factory.factoryType.toIntOrNull() ?: 1
    val cfg = FactoryConfigs.getFactoryById(factoryTypeId) ?: return
    val recipes = FactoryConfigs.getRecipesByFactory(factoryTypeId)
    val now = System.currentTimeMillis()
    var showRecipeDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(cfg.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(cfg.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Lv.${factory.level} | 队列: ${factory.queueSize}",
                        style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { showRecipeDialog = true }) {
                    Text("生产")
                }
            }

            // 生产队列
            queues.forEach { q ->
                val recipe = FactoryConfigs.getRecipeById(q.recipeId)
                if (recipe != null && !q.isCollected) {
                    val progress = ((now - q.startTime).toFloat() /
                            (q.finishTime - q.startTime).toFloat()).coerceIn(0f, 1f)
                    val isDone = progress >= 1f
                    Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(recipe.name, modifier = Modifier.weight(1f))
                        Box(modifier = Modifier
                            .width(80.dp)
                            .height(8.dp)
                            .background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp))
                        ) {
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(
                                    if (isDone) Color(0xFFFFC107) else Color(0xFF4CAF50),
                                    RoundedCornerShape(4.dp)
                                )
                            )
                        }
                        TextButton(onClick = { onCollect(q.queueId) }, enabled = isDone) {
                            Text(if (isDone) "收取" else "生产中")
                        }
                    }
                }
            }

            // 配方列表
            if (recipes.isNotEmpty()) {
                Text("可用配方:", style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp))
                recipes.take(3).forEach { recipe ->
                    Text("  📜 ${recipe.name} → 💰${recipe.outputSellPrice}",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

    if (showRecipeDialog) {
        AlertDialog(
            onDismissRequest = { showRecipeDialog = false },
            title = { Text("选择配方") },
            text = {
                LazyColumn {
                    items(recipes) { recipe ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onStartProduction(recipe.recipeId)
                                showRecipeDialog = false
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("📜", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(recipe.name, fontWeight = FontWeight.Bold)
                                Text("产出💰${recipe.outputSellPrice} | ⭐${recipe.outputExp}",
                                    style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Divider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRecipeDialog = false }) { Text("关闭") }
            }
        )
    }
}
