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
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.AnimalLevelingSystem
import com.farmlife.app.domain.logic.QualitySystem
import kotlinx.coroutines.launch

/**
 * 牧场区屏幕 - 展示动物、收集产品
 */
@Composable
fun LivestockScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    val animals by engine.animals.collectAsState()
    val player by engine.player.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("🐄 牧场", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp))

        if (animals.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("还没有动物，去商店购买一些吧！", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn {
                items(animals) { animal ->
                    AnimalCard(
                        beast = animal,
                        engine = engine,
                        onCollect = {
                            coroutineScope.launch { engine.collectAnimalProduct(it) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimalCard(
    beast: com.farmlife.app.data.entity.AnimalInstanceEntity,
    engine: FarmEngine,
    onCollect: (Long) -> Unit
) {
    val cfg = AnimalConfigs.getById(beast.animalId) ?: return
    val now = System.currentTimeMillis()
    val productTime = (cfg.productTimeSeconds * 1000L /
            AnimalLevelingSystem.efficiencyMultiplier(beast.level)).toLong()
    val progress = ((now - beast.lastProduceTime).toFloat() / productTime).coerceIn(0f, 1f)
    val isReady = progress >= 1f

    val qualityColor = when (beast.quality) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFF9C27B0)
        4 -> Color(0xFFFFC107)
        5 -> Color(0xFFFF6EC7)
        else -> Color(0xFFE0E0E0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isReady) Color(0xFFFFFDE7)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isReady) 2.dp else 1.dp,
            color = if (isReady) Color(0xFFFFC107) else qualityColor
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
                    Text("Lv.${beast.level}", fontSize = 12.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("❤${beast.friendship}", fontSize = 12.sp, color = Color(0xFFE91E63))
                }
                Text("产出: ${cfg.productName} | 💰${cfg.productSellPrice}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp))

                // 进度条
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
                                if (isReady) Color(0xFFFFC107) else Color(0xFF4CAF50),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            Button(
                onClick = { onCollect(beast.instanceId) },
                enabled = isReady
            ) { Text(if (isReady) "收获" else "生产中") }
        }
    }
}
