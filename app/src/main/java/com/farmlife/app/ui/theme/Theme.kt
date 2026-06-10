package com.farmlife.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import kotlin.math.sin
import kotlin.math.cos

// ============ 配色体系 ============
val FarmBackground = Color(0xFFFFF5E1)        // 奶油色背景
val FarmSurface = Color(0xFFFFFFFF)            // 卡片表面
val FarmGrassGreen = Color(0xFF5DAF54)         // 农场绿（主色）
val FarmGrassLight = Color(0xFF8BC78A)         // 浅绿
val FarmBrown = Color(0xFF8B5E3C)              // 土褐色
val FarmBrownLight = Color(0xFFB58868)          // 浅褐
val FarmGold = Color(0xFFF2B705)               // 金黄
val FarmGoldLight = Color(0xFFFFD95C)          // 浅金
val FarmSoil = Color(0xFF6B4226)               // 土壤色
val FarmSky = Color(0xFF87CEEB)                // 天空蓝
val FarmPink = Color(0xFFF06292)               // 粉色（稀有）
val FarmPurple = Color(0xFF7B1FA2)             // 紫色（史诗）
val FarmRed = Color(0xFFE53935)                // 红色（传说）
val FarmText = Color(0xFF2C2C2C)               // 主文字
val FarmTextMuted = Color(0xFF7A7A7A)          // 副文字

val SpringColor = Color(0xFFFFB7D5)
val SummerColor = Color(0xFF87CEEB)
val AutumnColor = Color(0xFFE8A87C)
val WinterColor = Color(0xFFC7E9F0)

val RainColor = Color(0xFF78909C)
val SnowColor = Color(0xFFB0BEC5)
val SunColor = Color(0xFFFFD54F)
val RainbowColor = Color(0xFFCE93D8)

val LightColorScheme = lightColorScheme(
    primary = FarmGrassGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = FarmGrassGreen,
    secondary = FarmBrown,
    onSecondary = Color.White,
    tertiary = FarmGold,
    onTertiary = Color(0xFF3A2A00),
    background = FarmBackground,
    onBackground = FarmText,
    surface = FarmSurface,
    onSurface = FarmText,
    surfaceVariant = Color(0xFFF5EFE0),
    onSurfaceVariant = FarmBrown,
    error = FarmRed,
    onError = Color.White,
    outline = Color(0xFFCFCFCF)
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    )
)

// ============ 渐变背景 ============
val FarmBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        FarmBackground,
        Color(0xFFFBEBC8),
        FarmBackground
    )
)

val CardGoldGradient = Brush.horizontalGradient(
    colors = listOf(FarmGoldLight, FarmGold, FarmGoldLight)
)

val CardGreenGradient = Brush.horizontalGradient(
    colors = listOf(FarmGrassLight, FarmGrassGreen)
)

// ============ 精美的卡片样式 ============
@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        color = FarmSurface,
        shape = shape,
        shadowElevation = 3.dp,
        tonalElevation = 2.dp,
        border = BorderStroke(0.5.dp, Color(0x20000000))
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// ============ 动画按钮 ============
@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = FarmGrassGreen,
        contentColor = Color.White,
        disabledContainerColor = Color(0xFFC0C0C0),
        disabledContentColor = Color.White
    ),
    content: @Composable RowScope.() -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "btn_scale"
    )

    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
    ) {
        content()
    }
}

// ============ 呼吸动画 ============
@Composable
fun BreathAnimation(
    minScale: Float = 0.94f,
    maxScale: Float = 1.06f,
    durationMillis: Int = 2000,
    content: @Composable (Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breath")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = androidx.compose.animation.core.EaseInOutSine),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "breath_progress"
    )
    val scale = minScale + (maxScale - minScale) * progress
    content(scale)
}

// ============ 发光闪烁动画 ============
@Composable
fun ShimmerAnimation(
    durationMillis: Int = 1500,
    content: @Composable (Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = androidx.compose.animation.core.EaseInOutSine),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    content(alpha)
}

// ============ 上下浮动动画 ============
@Composable
fun FloatAnimation(
    amplitude: Float = 8f,
    durationMillis: Int = 2500,
    content: @Composable (Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = androidx.compose.animation.core.EaseInOutSine),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "float_progress"
    )
    val offset = sin(progress * Math.PI * 2).toFloat() * amplitude
    content(offset)
}

// ============ 品质颜色 ============
fun qualityColor(quality: Int): Color = when (quality) {
    0 -> Color(0xFF7A7A7A)      // 普通 - 灰
    1 -> FarmGrassGreen          // 良好 - 绿
    2 -> Color(0xFF4FC3F7)        // 稀有 - 蓝
    3 -> FarmPink                 // 史诗 - 粉
    4 -> FarmGold                 // 传说 - 金
    5 -> Color(0xFFE040FB)        // 神话 - 紫彩
    else -> FarmRed               // 错误 - 红
}

fun qualityText(quality: Int): String = when (quality) {
    0 -> "普通"
    1 -> "良好"
    2 -> "稀有"
    3 -> "史诗"
    4 -> "传说"
    5 -> "神话"
    else -> "未知"
}

// ============ 高级过渡动画 ============

/**
 * 模块进入动画 - 从世界地图点击后展开
 */
@Composable
fun ModuleEnterAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 300)
        ) + scaleIn(
            initialScale = 0.3f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + expandIn(
            expandFrom = Alignment.Center,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 200)
        ) + shrinkOut(
            shrinkTowards = Alignment.Center,
            animationSpec = tween(durationMillis = 250)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * 模块背景渐显动画
 */
@Composable
fun BackgroundRevealAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 600)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 300)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * 粒子爆炸动画 - 点击时的火花效果
 */
@Composable
fun ParticleExplosion(
    visible: Boolean,
    color: Color = FarmGold,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    val particles = remember { (1..8).toList() }

    particles.forEach { index ->
        val angle = (index * 45f * Math.PI / 180f).toFloat()
        val infiniteTransition = rememberInfiniteTransition(label = "particle_$index")
        val progress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Restart
            ),
            label = "particle_prog_$index"
        )

        val distance = 30.dp * progress
        val offsetX = cos(angle) * distance.value
        val offsetY = sin(angle) * distance.value

        Box(
            modifier = modifier
                .graphicsLayer {
                    translationX = offsetX
                    translationY = offsetY
                    alpha = 1f - progress
                    scaleX = 1f - progress * 0.5f
                    scaleY = 1f - progress * 0.5f
                }
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
    }
}

/**
 * 世界地图区域标记的呼吸动画
 */
@Composable
fun MapMarkerPulse(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "marker_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = pulseScale
                scaleY = pulseScale
            }
    ) {
        content()
    }
}

/**
 * 云朵飘动动画（世界地图背景装饰）
 */
@Composable
fun FloatingCloud(
    offsetX: Float,
    offsetY: Float,
    size: Int,
    speed: Int = 15000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cloud")
    val floatX by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speed, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud_x"
    )

    Box(
        modifier = Modifier
            .absoluteOffset {
                IntOffset(
                    x = (floatX + offsetX).toInt(),
                    y = offsetY.toInt()
                )
            }
    ) {
        Text("☁️", fontSize = size.sp, color = Color.White.copy(alpha = 0.6f))
    }
}

/**
 * 太阳/月亮动画
 */
@Composable
fun SunAnimation(
    visible: Boolean
) {
    if (!visible) return

    val infiniteTransition = rememberInfiniteTransition(label = "sun")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing)
        ),
        label = "sun_rot"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sun_pulse"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = rotation
                scaleX = pulse
                scaleY = pulse
            }
    ) {
        Text("☀️", fontSize = 48.sp)
    }
}

/**
 * 世界地图区域按钮
 */
@Composable
fun WorldMapRegionButton(
    icon: String,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUnlocked: Boolean = true,
    size: Int = 80
) {
    var pressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "press"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        MapMarkerPulse {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .graphicsLayer {
                        scaleX = pressScale
                        scaleY = pressScale
                        transformOrigin = TransformOrigin.Center
                    }
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(enabled = isUnlocked) { onClick() }
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                pressed = event.changes.any { it.pressed }
                            }
                        }
                    }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.95f),
                                color.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.8f),
                                color,
                                Color.White.copy(alpha = 0.6f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = (size * 0.5).sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isUnlocked) label else "🔒 $label",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isUnlocked) FarmBrown else FarmTextMuted
        )
    }
}

/**
 * 顶部状态条（金币/等级等）
 */
@Composable
fun TopStatusBar(
    gold: Long,
    level: Int,
    collectionScore: Int,
    season: String,
    weather: String,
    onBack: (() -> Unit)? = null,
    title: String = "呆柳的农场"
) {
    Surface(
        color = Color(0xFFFAF1DB),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onBack != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onBack() }
                            .background(Color.White.copy(alpha = 0.8f))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("← 返回", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FarmBrown)
                    }
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    "🌾",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = FarmBrown
                    )
                    Text(
                        season,
                        fontSize = 10.sp,
                        color = FarmTextMuted
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFF9E0))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(weather, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusChip("💰", "$gold", FarmGold, Color(0xFFFFF8E1), Modifier.weight(1f))
                StatusChip("⭐", "Lv.$level", FarmGrassGreen, Color(0xFFE8F5E9), Modifier.weight(1f))
                StatusChip("📚", "$collectionScore", FarmPink, Color(0xFFF3E5F5), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatusChip(
    icon: String,
    text: String,
    textColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, textColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 11.sp)
            Spacer(Modifier.width(2.dp))
            Text(
                text,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun FarmLifeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
