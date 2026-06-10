package com.farmlife.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MapTransformState(
    var scale: Float = 1f,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
)

/**
 * 可缩放平移的世界地图
 * 点击区域标记以动画形式进入对应模块
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

    val transformState = remember { MapTransformState() }

    // 当前活跃的区域（点击时有爆炸动画）
    var activeRegion by remember { mutableStateOf<FarmModule?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // 计算季节对应的背景颜色
    val seasonColors = when (season) {
        com.farmlife.app.data.model.Season.SPRING -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7))
        com.farmlife.app.data.model.Season.SUMMER -> listOf(Color(0xFFFFF8E1), Color(0xFFFFE082), Color(0xFFFFCA28))
        com.farmlife.app.data.model.Season.AUTUMN -> listOf(Color(0xFFFFF3E0), Color(0xFFFFCC80), Color(0xFFFF8A65))
        com.farmlife.app.data.model.Season.WINTER -> listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB), Color(0xFF90CAF9))
    }

    val seasonText = when (season) {
        com.farmlife.app.data.model.Season.SPRING -> "🌸 春天"
        com.farmlife.app.data.model.Season.SUMMER -> "☀️ 夏天"
        com.farmlife.app.data.model.Season.AUTUMN -> "🍂 秋天"
        com.farmlife.app.data.model.Season.WINTER -> "❄️ 冬天"
    }

    val weatherIcon = when (weather) {
        com.farmlife.app.data.model.Weather.SUNNY -> "☀️"
        com.farmlife.app.data.model.Weather.CLOUDY -> "☁️"
        com.farmlife.app.data.model.Weather.RAINY -> "🌧️"
        com.farmlife.app.data.model.Weather.SNOWY -> "❄️"
        com.farmlife.app.data.model.Weather.RAINBOW -> "🌈"
        com.farmlife.app.data.model.Weather.METEOR -> "✨"
        else -> "🌤️"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 地图背景 - 可缩放平移的区域
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = transformState.scale
                    scaleY = transformState.scale
                    translationX = transformState.offsetX
                    translationY = transformState.offsetY
                }
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, zoom, _ ->
                            transformState.scale = (transformState.scale * zoom).coerceIn(0.5f, 3f)
                            transformState.offsetX += pan.x
                            transformState.offsetY += pan.y
                        }
                    )
                }
        ) {
            // 动态渐变背景 - 随季节变化
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = seasonColors,
                            radius = 1500f
                        )
                    )
            )

            // 装饰性图案 - 草地纹理
            for (i in 0..20) {
                for (j in 0..20) {
                    val x = (i * 150 - 500).toFloat()
                    val y = (j * 150 - 500).toFloat()
                    Box(
                        modifier = Modifier
                            .absoluteOffset { IntOffset(x.toInt(), y.toInt()) }
                            .size(100.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Color(0x102E7D32)
                            )
                    )
                }
            }

            // 河流装饰
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(100, 400) }
                    .size(width = 1200.dp, height = 80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF81D4FA), Color(0xFF4FC3F7), Color(0xFF81D4FA))
                        )
                    )
                    .graphicsLayer(rotationZ = -5f)
            )

            // 森林区域装饰
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(1400, 200) }
                    .size(300.dp)
                    .clip(RoundedCornerShape(150.dp))
                    .background(Color(0x40388E3C))
            )

            // 山丘装饰
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(-100, -50) }
                    .size(500.dp, 250.dp)
                    .clip(RoundedCornerShape(250.dp, 250.dp, 0.dp, 0.dp))
                    .background(Color(0x308D6E63))
            )

            // 云朵动画装饰
            FloatingCloud(offsetX = 100f, offsetY = 50f, size = 60, speed = 20000)
            FloatingCloud(offsetX = 300f, offsetY = 100f, size = 40, speed = 25000)
            FloatingCloud(offsetX = 600f, offsetY = 80f, size = 50, speed = 22000)
            FloatingCloud(offsetX = 900f, offsetY = 120f, size = 35, speed = 28000)

            // 太阳动画
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(1200, 60) }
            ) {
                SunAnimation(visible = true)
            }

            // ==== 农场区域按钮 - 分布在地图不同位置 ====

            // 农田（中心区域）
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(450, 350) }
            ) {
                WorldMapRegionButton(
                    icon = "🌾",
                    label = "农田",
                    color = Color(0xFF8BC34A),
                    onClick = {
                        activeRegion = FarmModule.FARMLAND
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.FARMLAND)
                            activeRegion = null
                        }
                    },
                    size = 90
                )
                if (activeRegion == FarmModule.FARMLAND) {
                    ParticleExplosion(visible = true, color = Color(0xFF8BC34A))
                }
            }

            // 牧场
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(700, 200) }
            ) {
                WorldMapRegionButton(
                    icon = "🐄",
                    label = "牧场",
                    color = Color(0xFFA1887F),
                    onClick = {
                        activeRegion = FarmModule.LIVESTOCK
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.LIVESTOCK)
                            activeRegion = null
                        }
                    },
                    isUnlocked = (player?.level ?: 0) >= 10,
                    size = 85
                )
                if (activeRegion == FarmModule.LIVESTOCK) {
                    ParticleExplosion(visible = true, color = Color(0xFFA1887F))
                }
            }

            // 果园
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(250, 200) }
            ) {
                WorldMapRegionButton(
                    icon = "🍎",
                    label = "果园",
                    color = Color(0xFFFF8A65),
                    onClick = {
                        activeRegion = FarmModule.ORCHARD
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.ORCHARD)
                            activeRegion = null
                        }
                    },
                    isUnlocked = (player?.level ?: 0) >= 20,
                    size = 80
                )
                if (activeRegion == FarmModule.ORCHARD) {
                    ParticleExplosion(visible = true, color = Color(0xFFFF8A65))
                }
            }

            // 工坊
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(600, 550) }
            ) {
                WorldMapRegionButton(
                    icon = "🏭",
                    label = "工坊",
                    color = Color(0xFF78909C),
                    onClick = {
                        activeRegion = FarmModule.FACTORY
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.FACTORY)
                            activeRegion = null
                        }
                    },
                    isUnlocked = (player?.level ?: 0) >= 30,
                    size = 85
                )
                if (activeRegion == FarmModule.FACTORY) {
                    ParticleExplosion(visible = true, color = Color(0xFF78909C))
                }
            }

            // 鱼塘
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(900, 500) }
            ) {
                WorldMapRegionButton(
                    icon = "🐟",
                    label = "鱼塘",
                    color = Color(0xFF4FC3F7),
                    onClick = {
                        activeRegion = FarmModule.FISHPOND
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.FISHPOND)
                            activeRegion = null
                        }
                    },
                    isUnlocked = (player?.level ?: 0) >= 40,
                    size = 80
                )
                if (activeRegion == FarmModule.FISHPOND) {
                    ParticleExplosion(visible = true, color = Color(0xFF4FC3F7))
                }
            }

            // 商店
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(150, 500) }
            ) {
                WorldMapRegionButton(
                    icon = "🏪",
                    label = "商店",
                    color = Color(0xFFFFB74D),
                    onClick = {
                        activeRegion = FarmModule.SHOP
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.SHOP)
                            activeRegion = null
                        }
                    },
                    size = 75
                )
                if (activeRegion == FarmModule.SHOP) {
                    ParticleExplosion(visible = true, color = Color(0xFFFFB74D))
                }
            }

            // 仓库
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(350, 650) }
            ) {
                WorldMapRegionButton(
                    icon = "📦",
                    label = "仓库",
                    color = Color(0xFFA5D6A7),
                    onClick = {
                        activeRegion = FarmModule.INVENTORY
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.INVENTORY)
                            activeRegion = null
                        }
                    },
                    size = 75
                )
                if (activeRegion == FarmModule.INVENTORY) {
                    ParticleExplosion(visible = true, color = Color(0xFFA5D6A7))
                }
            }

            // 图鉴
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(1000, 300) }
            ) {
                WorldMapRegionButton(
                    icon = "📚",
                    label = "图鉴",
                    color = Color(0xFFBA68C8),
                    onClick = {
                        activeRegion = FarmModule.COLLECTION
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.COLLECTION)
                            activeRegion = null
                        }
                    },
                    size = 80
                )
                if (activeRegion == FarmModule.COLLECTION) {
                    ParticleExplosion(visible = true, color = Color(0xFFBA68C8))
                }
            }

            // 宠物庄园
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(1100, 650) }
            ) {
                WorldMapRegionButton(
                    icon = "🐕",
                    label = "宠物庄园",
                    color = Color(0xFFFFCDD2),
                    onClick = {
                        activeRegion = FarmModule.PETS
                        coroutineScope.launch {
                            delay(400)
                            onEnterModule(FarmModule.PETS)
                            activeRegion = null
                        }
                    },
                    isUnlocked = (player?.level ?: 0) >= 12,
                    size = 80
                )
                if (activeRegion == FarmModule.PETS) {
                    ParticleExplosion(visible = true, color = Color(0xFFFFCDD2))
                }
            }
        }

        // 顶部状态条（不随地图缩放）
        Column(modifier = Modifier.fillMaxWidth()) {
            player?.let { p ->
                TopStatusBar(
                    gold = p.gold,
                    level = p.level,
                    collectionScore = p.collectionScore,
                    season = seasonText,
                    weather = weatherIcon
                )
            }

            // 缩放控制提示
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("👆 双指缩放 · 拖动平移", fontSize = 10.sp, color = FarmTextMuted)

                        Spacer(Modifier.width(8.dp))

                        // 缩小按钮
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFECEFF1))
                                .clickable {
                                    transformState.scale = (transformState.scale - 0.2f).coerceIn(0.5f, 3f)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("−", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmBrown)
                        }

                        // 缩放指示
                        Text(
                            "${(transformState.scale * 100).toInt()}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmBrown
                        )

                        // 放大按钮
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .clickable {
                                    transformState.scale = (transformState.scale + 0.2f).coerceIn(0.5f, 3f)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FarmBrown)
                        }

                        // 重置按钮
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFF8E1))
                                .clickable {
                                    transformState.scale = 1f
                                    transformState.offsetX = 0f
                                    transformState.offsetY = 0f
                                }
                                .padding(horizontal = 6.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("重置", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = FarmBrown)
                        }
                    }
                }
            }
        }

        // 底部欢迎文字
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    "🌟 点击地图上的建筑进入各区域 🌟",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmBrown,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

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
