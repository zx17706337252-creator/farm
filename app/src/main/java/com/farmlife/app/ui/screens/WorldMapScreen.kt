package com.farmlife.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
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
 * 模块配置
 */
data class ModuleConfig(
    val module: FarmModule,
    val icon: String,
    val label: String,
    val description: String,
    val color: Color,
    val gradient: Brush,
    val unlockLevel: Int = 1
)

val moduleConfigs = listOf(
    ModuleConfig(FarmModule.FARMLAND, "🌾", "农田", "种植作物", Color(0xFF8BC34A), GradientForest),
    ModuleConfig(FarmModule.SHOP, "🏪", "商店", "购买种子动物", Color(0xFFFFB300), GradientGold),
    ModuleConfig(FarmModule.INVENTORY, "📦", "仓库", "管理物品", Color(0xFF8D6E63), GradientAutumn),
    ModuleConfig(FarmModule.COLLECTION, "📚", "图鉴", "收集进度", Color(0xFF7E57C2), GradientPurple),
    ModuleConfig(FarmModule.LIVESTOCK, "🐄", "牧场", "养殖动物", Color(0xFFA1887F), GradientSunrise, 10),
    ModuleConfig(FarmModule.ORCHARD, "🍎", "果园", "种植果树", Color(0xFFFF8A65), GradientSunset, 20),
    ModuleConfig(FarmModule.FACTORY, "🏭", "工坊", "加工原料", Color(0xFF78909C), GradientOcean, 30),
    ModuleConfig(FarmModule.FISHPOND, "🐟", "鱼塘", "养殖鱼类", Color(0xFF4FC3F7), GradientDeepSea, 40),
    ModuleConfig(FarmModule.PETS, "🐕", "宠物庄园", "宠物系统", Color(0xFFF48FB1), GradientMint, 12)
)

/**
 * 世界地图主界面 - 精致横屏版
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

    // 季节背景
    val seasonGradient = when (season) {
        Season.SPRING -> GradientSpring
        Season.SUMMER -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFFF59D), Color(0xFFFFE082), Color(0xFFAED581))
        )
        Season.AUTUMN -> GradientAutumn
        Season.WINTER -> GradientWinter
        else -> GradientGold
    }

    val seasonText = when (season) {
        Season.SPRING -> "🌸春"
        Season.SUMMER -> "☀️夏"
        Season.AUTUMN -> "🍂秋"
        Season.WINTER -> "❄️冬"
        else -> "🌸"
    }

    val weatherIcon = when (weather) {
        Weather.SUNNY -> "☀️晴"
        Weather.CLOUDY -> "☁️阴"
        Weather.RAINY -> "🌧️雨"
        Weather.SNOWY -> "❄️雪"
        Weather.RAINBOW -> "🌈虹"
        Weather.METEOR -> "✨流星"
        else -> "🌤️"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(seasonGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 紧凑顶部栏
            CompactTopBar(
                title = "🏡 呆柳农场",
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                season = seasonText,
                weather = weatherIcon
            )

            // 主内容区域 - 横向3列布局
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 左列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModuleCardLarge(
                        icon = "🌾",
                        label = "农田",
                        description = "种植作物，收获金币",
                        color = Color(0xFF8BC34A),
                        gradient = GradientForest,
                        onClick = {
                            coroutineScope.launch {
                                onEnterModule(FarmModule.FARMLAND)
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "🐄",
                        label = "牧场",
                        description = "养殖动物",
                        color = Color(0xFFA1887F),
                        gradient = GradientSunrise,
                        isUnlocked = (player?.level ?: 0) >= 10,
                        unlockText = "Lv.10",
                        onClick = {
                            if ((player?.level ?: 0) >= 10) {
                                coroutineScope.launch {
                                    onEnterModule(FarmModule.LIVESTOCK)
                                }
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "🍎",
                        label = "果园",
                        description = "种植果树",
                        color = Color(0xFFFF8A65),
                        gradient = GradientSunset,
                        isUnlocked = (player?.level ?: 0) >= 20,
                        unlockText = "Lv.20",
                        onClick = {
                            if ((player?.level ?: 0) >= 20) {
                                coroutineScope.launch {
                                    onEnterModule(FarmModule.ORCHARD)
                                }
                            }
                        }
                    )
                }

                // 中列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModuleCardSmall(
                        icon = "🏪",
                        label = "商店",
                        description = "购买种子动物",
                        color = Color(0xFFFFB300),
                        gradient = GradientGold,
                        onClick = {
                            coroutineScope.launch {
                                onEnterModule(FarmModule.SHOP)
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "📦",
                        label = "仓库",
                        description = "管理物品出售",
                        color = Color(0xFF8D6E63),
                        gradient = GradientAutumn,
                        onClick = {
                            coroutineScope.launch {
                                onEnterModule(FarmModule.INVENTORY)
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "📚",
                        label = "图鉴",
                        description = "查看收集进度",
                        color = Color(0xFF7E57C2),
                        gradient = GradientPurple,
                        onClick = {
                            coroutineScope.launch {
                                onEnterModule(FarmModule.COLLECTION)
                            }
                        }
                    )
                }

                // 右列
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModuleCardSmall(
                        icon = "🏭",
                        label = "工坊",
                        description = "加工原料",
                        color = Color(0xFF78909C),
                        gradient = GradientOcean,
                        isUnlocked = (player?.level ?: 0) >= 30,
                        unlockText = "Lv.30",
                        onClick = {
                            if ((player?.level ?: 0) >= 30) {
                                coroutineScope.launch {
                                    onEnterModule(FarmModule.FACTORY)
                                }
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "🐟",
                        label = "鱼塘",
                        description = "养殖鱼类",
                        color = Color(0xFF4FC3F7),
                        gradient = GradientDeepSea,
                        isUnlocked = (player?.level ?: 0) >= 40,
                        unlockText = "Lv.40",
                        onClick = {
                            if ((player?.level ?: 0) >= 40) {
                                coroutineScope.launch {
                                    onEnterModule(FarmModule.FISHPOND)
                                }
                            }
                        }
                    )
                    ModuleCardSmall(
                        icon = "🐕",
                        label = "宠物庄园",
                        description = "宠物管理",
                        color = Color(0xFFF48FB1),
                        gradient = GradientMint,
                        isUnlocked = (player?.level ?: 0) >= 12,
                        unlockText = "Lv.12",
                        onClick = {
                            if ((player?.level ?: 0) >= 12) {
                                coroutineScope.launch {
                                    onEnterModule(FarmModule.PETS)
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
                    .padding(bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(FarmRadiusCircle),
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, Color(0x20000000))
                ) {
                    Text(
                        "✨ 点击卡片进入各区域 ✨",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmBrown,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

/**
 * 大模块卡片 - 主视觉
 */
@Composable
fun ModuleCardLarge(
    icon: String,
    label: String,
    description: String,
    color: Color,
    gradient: Brush,
    isUnlocked: Boolean = true,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(150),
        label = "module_large_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .scale(scale)
            .clip(RoundedCornerShape(FarmRadiusLarge))
            .clickable(enabled = isUnlocked) {
                pressed = true
                onClick()
                pressed = false
            },
        color = Color.Transparent,
        shape = RoundedCornerShape(FarmRadiusLarge),
        shadowElevation = if (isUnlocked) 8.dp else 2.dp,
        border = BorderStroke(2.dp, if (isUnlocked) color.copy(alpha = 0.5f) else Color(0xFFBDBDBD))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isUnlocked) gradient else Brush.horizontalGradient(listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD)))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(icon, fontSize = 42.sp)
                Text(
                    label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color(0x60000000),
                            offset = androidx.compose.ui.geometry.Offset(2f, 2f)
                        )
                    )
                )
                Text(
                    description,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

/**
 * 小模块卡片 - 标准
 */
@Composable
fun ModuleCardSmall(
    icon: String,
    label: String,
    description: String,
    color: Color,
    gradient: Brush,
    isUnlocked: Boolean = true,
    unlockText: String = "",
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = "module_small_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .scale(scale)
            .clip(RoundedCornerShape(FarmRadiusLarge))
            .clickable(enabled = isUnlocked) {
                pressed = true
                onClick()
                pressed = false
            },
        color = Color.Transparent,
        shape = RoundedCornerShape(FarmRadiusLarge),
        shadowElevation = if (isUnlocked) 5.dp else 1.dp,
        border = BorderStroke(1.5.dp, if (isUnlocked) color.copy(alpha = 0.4f) else Color(0xFFBDBDBD))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isUnlocked) gradient else Brush.horizontalGradient(listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD)))),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 图标
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(FarmRadiusMedium))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 24.sp)
                }

                // 文字
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) Color.White else Color(0xFF757575),
                        style = if (isUnlocked) androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color(0x40000000),
                                offset = androidx.compose.ui.geometry.Offset(1f, 1f)
                            )
                        ) else androidx.compose.ui.text.TextStyle()
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        description,
                        fontSize = 9.sp,
                        color = if (isUnlocked) Color.White.copy(alpha = 0.85f) else Color(0xFF9E9E9E),
                        maxLines = 1
                    )
                }

                // 箭头
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(FarmRadiusSmall))
                        .background(
                            if (isUnlocked) Color.White.copy(alpha = 0.3f)
                            else Color(0xFFBDBDBD)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (isUnlocked) "→" else "🔒",
                        fontSize = if (isUnlocked) 12.sp else 9.sp,
                        color = if (isUnlocked) Color.White else Color(0xFF616161),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 未解锁等级提示
            if (!isUnlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        unlockText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(FarmRadiusSmall))
                            .background(Color(0x9E000000))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
