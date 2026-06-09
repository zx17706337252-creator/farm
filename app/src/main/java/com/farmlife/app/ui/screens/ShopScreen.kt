package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.AnimalConfigs
import com.farmlife.app.config.CropConfigs
import com.farmlife.app.config.FactoryConfigs
import com.farmlife.app.config.PetConfigs
import com.farmlife.app.domain.engine.FarmEngine
import kotlinx.coroutines.launch

/**
 * 商店屏幕 - 购买种子、动物、宠物、建筑
 */
@Composable
fun ShopScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("🏪 商店", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp))

        // Tab切换
        TabRow(selectedTabIndex = currentTab) {
            Tab(selected = currentTab == 0, onClick = { currentTab = 0 }, text = { Text("种子") })
            Tab(selected = currentTab == 1, onClick = { currentTab = 1 }, text = { Text("动物") })
            Tab(selected = currentTab == 2, onClick = { currentTab = 2 }, text = { Text("宠物") })
            Tab(selected = currentTab == 3, onClick = { currentTab = 3 }, text = { Text("建筑") })
        }

        when (currentTab) {
            0 -> SeedShop(engine)
            1 -> AnimalShop(engine, onBuy = {
                coroutineScope.launch { engine.buyAnimal(it) }
            })
            2 -> PetShop(engine, onBuy = {
                coroutineScope.launch { engine.buyPet(it) }
            })
            3 -> FactoryShop(engine, onBuy = {
                coroutineScope.launch { engine.buyFactory(it) }
            })
        }
    }
}

@Composable
fun SeedShop(engine: FarmEngine) {
    val player by engine.player.collectAsState()
    Text("点击农田空地即可种植，种子将从金币中扣除",
        modifier = Modifier.padding(12.dp),
        style = MaterialTheme.typography.bodyMedium)

    LazyColumn {
        items(CropConfigs.ALL.take(30)) { crop ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(crop.icon, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(crop.name, fontWeight = FontWeight.Bold)
                    Text("成熟: ${crop.growTimeSeconds}秒 | 售价:💰${crop.sellPrice} | ⭐${crop.exp}",
                        style = MaterialTheme.typography.bodySmall)
                }
                val seedPrice = crop.sellPrice * 2
                Text("种子💰$seedPrice", style = MaterialTheme.typography.labelLarge)
            }
            Divider()
        }
    }
}

@Composable
fun AnimalShop(engine: FarmEngine, onBuy: (Int) -> Unit) {
    val player by engine.player.collectAsState()
    LazyColumn {
        items(AnimalConfigs.ALL) { animal ->
            val canBuy = (player?.level ?: 0) >= animal.unlockLevel &&
                    (player?.gold ?: 0) >= animal.purchasePrice
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(animal.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(animal.name, fontWeight = FontWeight.Bold)
                    Text("产出: ${animal.productName} | 售价:💰${animal.productSellPrice}",
                        style = MaterialTheme.typography.bodySmall)
                    Text("需要等级: ${animal.unlockLevel}",
                        style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { onBuy(animal.animalId) }, enabled = canBuy) {
                    Text("💰${animal.purchasePrice}")
                }
            }
            Divider()
        }
    }
}

@Composable
fun PetShop(engine: FarmEngine, onBuy: (Int) -> Unit) {
    val player by engine.player.collectAsState()
    LazyColumn {
        items(PetConfigs.ALL) { pet ->
            val canBuy = (player?.level ?: 0) >= pet.unlockLevel &&
                    (player?.gold ?: 0) >= pet.purchasePrice
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(pet.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(pet.name, fontWeight = FontWeight.Bold)
                    Text("职责: ${pet.primaryWorkType} | ${pet.description}",
                        style = MaterialTheme.typography.bodySmall)
                    Text("需要等级: ${pet.unlockLevel}",
                        style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { onBuy(pet.petId) }, enabled = canBuy) {
                    Text("💰${pet.purchasePrice}")
                }
            }
            Divider()
        }
    }
}

@Composable
fun FactoryShop(engine: FarmEngine, onBuy: (Int) -> Unit) {
    val player by engine.player.collectAsState()
    LazyColumn {
        items(FactoryConfigs.ALL) { factory ->
            val canBuy = (player?.level ?: 0) >= factory.unlockLevel &&
                    (player?.gold ?: 0) >= factory.purchasePrice
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(factory.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(factory.name, fontWeight = FontWeight.Bold)
                    Text(factory.description, style = MaterialTheme.typography.bodySmall)
                    Text("需要等级: ${factory.unlockLevel}",
                        style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { onBuy(factory.factoryId) }, enabled = canBuy) {
                    Text("💰${factory.purchasePrice}")
                }
            }
            Divider()
        }
    }
}
