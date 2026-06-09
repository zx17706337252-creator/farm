package com.farmlife.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.TutorialSystem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 主应用屏幕 - 底部导航切换各个区域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }
    val player by engine.player.collectAsState()
    val toast by engine.toast.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()
    val tutorialProgress by engine.tutorialProgress.collectAsState()
    val unlockDialog by engine.pendingUnlockDialog.collectAsState()
    val context = LocalContext.current

    var showTutorial by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableStateOf<TutorialSystem.Step?>(null) }

    LaunchedEffect(Unit) {
        delay(500)
        val step = engine.getNextTutorialStep()
        if (step != null) {
            currentTutorialStep = step
            showTutorial = true
        }
    }

    fun handleTutorialComplete() {
        if (currentTutorialStep != null) {
            coroutineScope.launch {
                engine.markTutorialStepComplete(currentTutorialStep!!.id)
                engine.updateTutorialCurrentStep(currentTutorialStep!!.id)
            }
        }
        showTutorial = false
        currentTutorialStep = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(text = "🌾 呆柳的农场")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (season) {
                                    com.farmlife.app.data.model.Season.SPRING -> "🌸春"
                                    com.farmlife.app.data.model.Season.SUMMER -> "☀️夏"
                                    com.farmlife.app.data.model.Season.AUTUMN -> "🍂秋"
                                    com.farmlife.app.data.model.Season.WINTER -> "❄️冬"
                                },
                                fontSize = 14.sp
                            )
                        }
                        player?.let {
                            Text(
                                text = "Lv.${it.level}  💰${it.gold}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    Text(
                        text = when (weather) {
                            com.farmlife.app.data.model.Weather.SUNNY -> "☀️"
                            com.farmlife.app.data.model.Weather.CLOUDY -> "☁️"
                            com.farmlife.app.data.model.Weather.RAINY -> "🌧️"
                            com.farmlife.app.data.model.Weather.SNOWY -> "❄️"
                            com.farmlife.app.data.model.Weather.RAINBOW -> "🌈"
                            com.farmlife.app.data.model.Weather.METEOR -> "🌠"
                        },
                        modifier = Modifier.padding(end = 16.dp),
                        fontSize = 20.sp
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Yard, contentDescription = "农田") },
                    label = { Text("农田") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Pets, contentDescription = "牧场") },
                    label = { Text("牧场") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Factory, contentDescription = "工坊") },
                    label = { Text("工坊") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Store, contentDescription = "商店") },
                    label = { Text("商店") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "仓库") },
                    label = { Text("仓库") }
                )
                NavigationBarItem(
                    selected = selectedTab == 5,
                    onClick = { selectedTab = 5 },
                    icon = { Icon(Icons.Default.Book, contentDescription = "图鉴") },
                    label = { Text("图鉴") }
                )
                NavigationBarItem(
                    selected = selectedTab == 6,
                    onClick = { selectedTab = 6 },
                    icon = { Icon(Icons.Default.Park, contentDescription = "果园") },
                    label = { Text("果园") }
                )
                NavigationBarItem(
                    selected = selectedTab == 7,
                    onClick = { selectedTab = 7 },
                    icon = { Icon(Icons.Default.Water, contentDescription = "鱼塘") },
                    label = { Text("鱼塘") }
                )
            }
        },
        snackbarHost = {
            toast?.let { msg ->
                SnackbarHost(hostState = remember { SnackbarHostState() }) {
                    Snackbar { Text(msg) }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> FarmlandScreen(engine)
                1 -> LivestockScreen(engine)
                2 -> FactoryScreen(engine)
                3 -> ShopScreen(engine)
                4 -> InventoryScreen(engine)
                5 -> CollectionScreen(engine)
                6 -> OrchardScreen(engine)
                7 -> FishpondScreen(engine)
            }
        }
    }

    if (showTutorial && currentTutorialStep != null) {
        TutorialDialog(
            step = currentTutorialStep!!,
            onDismiss = { showTutorial = false },
            onComplete = ::handleTutorialComplete
        )
    }

    if (unlockDialog != null) {
        UnlockDialog(
            title = unlockDialog!!.first,
            description = unlockDialog!!.second,
            icon = unlockDialog!!.third,
            onDismiss = { engine.dismissUnlockDialog() }
        )
    }
}
