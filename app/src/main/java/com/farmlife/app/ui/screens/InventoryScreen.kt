package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.config.FactoryConfigs
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.QualitySystem
import kotlinx.coroutines.launch

/**
 * 仓库屏幕 - 查看库存、出售
 */
@Composable
fun InventoryScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val inventory by engine.inventory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📦 仓库 (${inventory.sumOf { it.quantity }}件)",
                style = MaterialTheme.typography.titleLarge)
            Button(onClick = { coroutineScope.launch { engine.sellAllInventory() } }) {
                Text("全部出售")
            }
        }

        if (inventory.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("仓库空空如也，快去种点什么吧！", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn {
                items(inventory) { item ->
                    val configName = when (item.itemType) {
                        "CROP" -> CropConfigs.getById(item.configId)?.name ?: "作物"
                        "ANIMAL" -> AnimalConfigs.getById(item.configId)?.productName ?: "动物产品"
                        "PROCESSED" -> FactoryConfigs.RECIPES.firstOrNull { it.recipeId == item.configId }?.name ?: "加工品"
                        else -> "物品"
                    }
                    val configIcon = when (item.itemType) {
                        "CROP" -> CropConfigs.getById(item.configId)?.icon ?: "🌾"
                        "ANIMAL" -> AnimalConfigs.getById(item.configId)?.icon ?: "🥚"
                        else -> "📦"
                    }
                    val sellPrice = when (item.itemType) {
                        "CROP" -> CropConfigs.getById(item.configId)?.sellPrice ?: 0
                        "ANIMAL" -> AnimalConfigs.getById(item.configId)?.productSellPrice ?: 0
                        "PROCESSED" -> FactoryConfigs.RECIPES.firstOrNull { it.recipeId == item.configId }?.outputSellPrice ?: 0
                        else -> 0
                    }
                    val finalPrice = (sellPrice * QualitySystem.qualityMultiplier(item.quality)).toLong()

                    val qualityColor = when (item.quality) {
                        1 -> Color(0xFF4CAF50)
                        2 -> Color(0xFF2196F3)
                        3 -> Color(0xFF9C27B0)
                        4 -> Color(0xFFFFC107)
                        5 -> Color(0xFFFF6EC7)
                        else -> Color(0xFF9E9E9E)
                    }
                    val qualityName = when (item.quality) {
                        1 -> "优良"
                        2 -> "稀有"
                        3 -> "史诗"
                        4 -> "传说"
                        5 -> "神话"
                        else -> "普通"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(configIcon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(configName, fontWeight = FontWeight.Bold)
                            Text("品质: $qualityName | 数量: ${item.quantity}",
                                style = MaterialTheme.typography.bodySmall,
                                color = qualityColor)
                        }
                        Text("💰$finalPrice/件", modifier = Modifier.padding(horizontal = 8.dp))
                        Button(onClick = {
                            coroutineScope.launch { engine.sellInventoryItem(item.itemId, item.quantity) }
                        }) {
                            Text("出售")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}
