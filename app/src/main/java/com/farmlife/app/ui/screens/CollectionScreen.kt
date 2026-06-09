package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.config.PetConfigs
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.CollectionScoreSystem

/**
 * 图鉴 / 收藏馆屏幕
 */
@Composable
fun CollectionScreen(engine: FarmEngine) {
    val collections by engine.collections.collectAsState()
    val player by engine.player.collectAsState()
    var currentTab by remember { mutableStateOf(0) }

    val cropRecords = collections.filter { it.collectionType == "CROP" }
    val animalRecords = collections.filter { it.collectionType == "ANIMAL" }
    val petRecords = collections.filter { it.collectionType == "PET" }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("📚 收藏馆", style = MaterialTheme.typography.titleLarge)
                player?.let {
                    val level = CollectionScoreSystem.collectionLevel(it.collectionScore)
                    val title = CollectionScoreSystem.collectionTitle(level)
                    Text("收藏分数: ${it.collectionScore} | 等级: Lv.$level | $title",
                        style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    "作物: ${cropRecords.size}/${CropConfigs.ALL.size}  " +
                            "动物: ${animalRecords.size}/${AnimalConfigs.ALL.size}  " +
                            "宠物: ${petRecords.size}/${PetConfigs.ALL.size}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        TabRow(selectedTabIndex = currentTab) {
            Tab(selected = currentTab == 0, onClick = { currentTab = 0 }, text = { Text("作物图鉴") })
            Tab(selected = currentTab == 1, onClick = { currentTab = 1 }, text = { Text("动物图鉴") })
            Tab(selected = currentTab == 2, onClick = { currentTab = 2 }, text = { Text("宠物图鉴") })
        }

        when (currentTab) {
            0 -> CollectionList(
                items = CropConfigs.ALL.map { crop ->
                    Triple(crop.icon, crop.name, cropRecords.firstOrNull { it.configId == crop.cropId })
                }
            )
            1 -> CollectionList(
                items = AnimalConfigs.ALL.map { animal ->
                    Triple(animal.icon, animal.name, animalRecords.firstOrNull { it.configId == animal.animalId })
                }
            )
            2 -> CollectionList(
                items = PetConfigs.ALL.map { pet ->
                    Triple(pet.icon, pet.name, petRecords.firstOrNull { it.configId == pet.petId })
                }
            )
        }
    }
}

@Composable
fun CollectionList(
    items: List<Triple<String, String, com.farmlife.app.data.entity.CollectionRecordEntity?>>
) {
    LazyColumn {
        items(items) { (icon, name, record) ->
            val unlocked = record != null
            val qualityColor = when (record?.highestQuality) {
                1 -> Color(0xFF4CAF50)
                2 -> Color(0xFF2196F3)
                3 -> Color(0xFF9C27B0)
                4 -> Color(0xFFFFC107)
                5 -> Color(0xFFFF6EC7)
                else -> Color(0xFF9E9E9E)
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = if (unlocked) icon else "❓",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (!unlocked) Color(0xFFBDBDBD) else Color.Unspecified
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (unlocked) name else "???",
                        fontWeight = FontWeight.Bold,
                        color = if (!unlocked) Color(0xFFBDBDBD) else Color.Unspecified
                    )
                    if (record != null) {
                        Text(
                            "获得 ${record.totalObtained} 次",
                            style = MaterialTheme.typography.bodySmall,
                            color = qualityColor
                        )
                    } else {
                        Text("未解锁",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFBDBDBD))
                    }
                }
            }
            Divider()
        }
    }
}
