package com.farmlife.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.domain.logic.TutorialSystem
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 主应用屏幕 - 交互式世界地图导航
 * 点击地图上的区域以动画形式进入对应模块
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(engine: FarmEngine) {
    val coroutineScope = rememberCoroutineScope()
    var currentModule by remember { mutableStateOf<FarmModule?>(null) }
    var showTutorial by remember { mutableStateOf(false) }
    var currentTutorialStep by remember { mutableStateOf<TutorialSystem.Step?>(null) }

    // 启动时显示引导
    LaunchedEffect(Unit) {
        delay(500)
        val step = engine.getNextTutorialStep()
        if (step != null) {
            currentTutorialStep = step
            showTutorial = true
        }
    }

    fun handleTutorialComplete() {
        val step = currentTutorialStep
        if (step != null) {
            coroutineScope.launch {
                try {
                    engine.markTutorialStepComplete(step.id)
                    engine.updateTutorialCurrentStep(step.id)
                } catch (e: Exception) {
                    // 忽略引导错误
                }
            }
        }
        showTutorial = false
        currentTutorialStep = null
    }

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        // 世界地图（默认显示）
        BackgroundRevealAnimation(
            visible = currentModule == null
        ) {
            WorldMapScreen(engine) { module ->
                currentModule = module
            }
        }

        // 各模块界面（动画进入/退出）
        ModuleEnterAnimation(
            visible = currentModule == FarmModule.FARMLAND
        ) {
            FarmlandScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.LIVESTOCK
        ) {
            LivestockScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.ORCHARD
        ) {
            OrchardScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.FACTORY
        ) {
            FactoryScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.FISHPOND
        ) {
            FishpondScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.SHOP
        ) {
            ShopScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.INVENTORY
        ) {
            InventoryScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.COLLECTION
        ) {
            CollectionScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }

        ModuleEnterAnimation(
            visible = currentModule == FarmModule.PETS
        ) {
            PetScreen(
                engine = engine,
                onBack = { currentModule = null }
            )
        }
    }

    // 新手引导弹窗
    val step = currentTutorialStep
    if (showTutorial && step != null) {
        TutorialDialog(
            step = step,
            onDismiss = { showTutorial = false },
            onComplete = ::handleTutorialComplete
        )
    }
}
