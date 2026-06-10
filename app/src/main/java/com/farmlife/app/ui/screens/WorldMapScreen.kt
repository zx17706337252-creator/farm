package com.farmlife.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 农场模块枚举
 */
enum class FarmModule {
    FARMLAND,    // 农田
    LIVESTOCK,   // 牧场
    ORCHARD,     // 果园
    FACTORY,     // 工坊
    FISHPOND,    // 鱼塘
    SHOP,        // 商店
    INVENTORY,   // 仓库
    COLLECTION,  // 图鉴
    PETS         // 宠物庄园
}

/**
 * 世界地图界面 - 横屏优化版
 * 精美的农场全景布局，各模块整齐排列
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldMapScreen(
    engine: FarmEngine,
    onEnterModule: (FarmModule) -> Unit
) {
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 季节背景色
    val seasonColors = when (season) {
        Season.SPRING -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        Season.SUMMER -> listOf(Color(0xFFFFF8E1), Color(0xFFFFE082))
        Season.AUTUMN -> listOf(Color(0xFFFFF3E0), Color(0xFFFFCC80))
        Season.WINTER -> listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
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

    // 点击动画状态
    var activeModule by remember { mutableStateOf<FarmModule?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(seasonColors))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部状态栏
            player?.let { p ->
                TopStatusBar(
                    gold = p.gold,
                    level = p.level,
                    collectionScore = p.collectionScore,
                    season = seasonText,
                    weather = weatherIcon
                )
            }

            Spacer(Modifier.height(8.dp))

            // 主内容区 - 横屏三列布局
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 左列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ModuleCard(
                        icon = "🌾",
                        label = "农田",
                        color = Color(0xFF8BC34A),
                        description = "种植作物，收获金币",
                        isUnlocked = true,
                        isActive = activeModule == FarmModule.FARMLAND,
                        onClick = {
                            activeModule = FarmModule.FARMLAND
                            coroutineScope.launch {
                                delay(300)
                                onEnterModule(FarmModule.FARMLAND)
                                activeModule = null
                            }
                        }
                    )
                    ModuleCard(
                        icon = "🐄",
                        label = "牧场",
                        color = Color(0xFFA1887F),
                        description = "养殖动物，收集产品",
                        isUnlocked = (player?.level ?: 0) >= 10,
                        unlockText = "Lv.10解锁",
                        isActive = activeModule == FarmModule.LIVESTOCK,
                        onClick = {
                            if ((player?.level ?: 0) >= 10) {
                                activeModule = FarmModule.LIVESTOCK
                                coroutineScope.launch {
                                    delay(300)
                                    onEnterModule(FarmModule.LIVESTOCK)
                                    activeModule = null
                                }
                            }
                        }
                    )
                    ModuleCard(
                        icon = "🍎",
                        label = "果园",
                        color = Color(0xFFFF8A65),
                        description = "种植果树，收获水果",
                        isUnlocked = (player?.level ?: 0) >= 20,
                        unlockText = "Lv.20解锁",
                        isActive = activeModule == FarmModule.ORCHARD,
                        onClick = {
                            if ((player?.level ?: 0) >= 20) {
                                activeModule = FarmModule.ORCHARD
                                coroutineScope.launch {
                                    delay(300)
                                    onEnterModule(FarmModule.ORCHARD)
                                    activeModule = null
                                }
                            }
                        }
                    )
                }

                // 中列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ModuleCard(
                        icon = "🏪",
                        label = "商店",
                        color = Color(0xFFFFB74D),
                        description = "购买种子、动物、道具",
                        isUnlocked = true,
                        isActive = activeModule == FarmModule.SHOP,
                        onClick = {
                            activeModule = FarmModule.SHOP
                            coroutineScope.launch {
                                delay(300)
                                onEnterModule(FarmModule.SHOP)
                                activeModule = null
                            }
                        }
                    )
                    ModuleCard(
                        icon = "📦",
                        label = "仓库",
                        color = Color(0xFFA5D6A7),
                        description = "管理物品，出售收获",
                        isUnlocked = true,
                        isActive = activeModule == FarmModule.INVENTORY,
                        onClick = {
                            activeModule = FarmModule.INVENTORY
                            coroutineScope.launch {
                                delay(300)
                                onEnterModule(FarmModule.INVENTORY)
                                activeModule = null
                            }
                        }
                    )
                    ModuleCard(
                        icon = "📚",
                        label = "图鉴",
                        color = Color(0xFFBA68C8),
                        description = "查看收集进度和成就",
                        isUnlocked = true,
                        isActive = activeModule == FarmModule.COLLECTION,
                        onClick = {
                            activeModule = FarmModule.COLLECTION
                            coroutineScope.launch {
                                delay(300)
                                onEnterModule(FarmModule.COLLECTION)
                                activeModule = null
                            }
                        }
                    )
                }

                // 右列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ModuleCard(
                        icon = "🏭",
                        label = "工坊",
                        color = Color(0xFF78909C),
                        description = "加工原料，制作成品",
                        isUnlocked = (player?.level ?: 0) >= 30,
                        unlockText = "Lv.30解锁",
                        isActive = activeModule == FarmModule.FACTORY,
                        onClick = {
                            if ((player?.level ?: 0) >= 30) {
                                activeModule = FarmModule.FACTORY
                                coroutineScope.launch {
                                    delay(300)
                                    onEnterModule(FarmModule.FACTORY)
                                    activeModule = null
                                }
                            }
                        }
                    )
                    ModuleCard(
                        icon = "🐟",
                        label = "鱼塘",
                        color = Color(0xFF4FC3F7),
                        description = "养殖鱼类，收集渔获",
                        isUnlocked = (player?.level ?: 0) >= 40,
                        unlockText = "Lv.40解锁",
                        isActive = activeModule == FarmModule.FISHPOND,
                        onClick = {
                            if ((player?.level ?: 0) >= 40) {
                                activeModule = FarmModule.FISHPOND
                                coroutineScope.launch {
                                    delay(300)
                                    onEnterModule(FarmModule.FISHPOND)
                                    activeModule = null
                                }
                            }
                        }
                    )
                    ModuleCard(
                        icon = "🐕",
                        label = "宠物庄园",
                        color = Color(0xFFFFCDD2),
                        description = "宠物管理，自动工作",
                        isUnlocked = (player?.level ?: 0) >= 12,
                        unlockText = "Lv.12解锁",
                        isActive = activeModule == FarmModule.PETS,
                        onClick = {
                            if ((player?.level ?: 0) >= 12) {
                                activeModule = FarmModule.PETS
                                coroutineScope.launch {
                                    delay(300)
                                    onEnterModule(FarmModule.PETS)
                                    activeModule = null
                                }
                            }
                        }
                    )
                }
            }

            // 底部提示
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        "🌟 点击卡片进入各区域 🌟",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmBrown,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * 模块卡片组件
 */
@Composable
fun ModuleCard(
    icon: String,
    label: String,
    color: Color,
    description: String,
    isUnlocked: Boolean,
    unlockText: String = "",
    isActive: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 0.95f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(enabled = isUnlocked) { onClick() }
            .clip(RoundedCornerShape(16.dp)),
        color = if (isUnlocked) Color.White else Color(0xFFE0E0E0),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isActive) 8.dp else 3.dp,
        border = BorderStroke(2.dp, if (isUnlocked) color else Color(0xFFBDBDBD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标区域
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isUnlocked) color.copy(alpha = 0.15f)
                        else Color(0xFFBDBDBD).copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    icon,
                    fontSize = 32.sp,
                    color = if (isUnlocked) Color.Unspecified else Color(0xFF9E9E9E)
                )
            }

            Spacer(Modifier.width(12.dp))

            // 文字区域
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) FarmText else Color(0xFF9E9E9E)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isUnlocked) description else unlockText,
                    fontSize = 12.sp,
                    color = if (isUnlocked) FarmTextMuted else Color(0xFFBDBDBD)
                )
            }

            // 状态指示
            if (isUnlocked) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text("→", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFCDD2))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔒", fontSize = 12.sp)
                }
            }
        }
    }
}