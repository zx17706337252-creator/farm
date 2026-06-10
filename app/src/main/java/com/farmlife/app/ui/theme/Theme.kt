package com.farmlife.app.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===================== 精致配色系统 =====================

val FarmLightGold = Color(0xFFFFD54F)
val FarmGold = Color(0xFFFFB300)
val FarmDeepGold = Color(0xFFFF8F00)

val FarmLightGreen = Color(0xFF81C784)
val FarmGreen = Color(0xFF66BB6A)
val FarmDeepGreen = Color(0xFF388E3C)

val FarmLightBlue = Color(0xFF64B5F6)
val FarmBlue = Color(0xFF42A5F5)
val FarmDeepBlue = Color(0xFF1976D2)

val FarmLightPink = Color(0xFFF48FB1)
val FarmPink = Color(0xFFEC407A)

val FarmLightPurple = Color(0xFFB39DDB)
val FarmPurple = Color(0xFF7E57C2)

val FarmLightCyan = Color(0xFF80DEEA)
val FarmCyan = Color(0xFF26C6DA)

val FarmLightOrange = Color(0xFFFFCC80)
val FarmOrange = Color(0xFFFF9800)

val FarmLightRed = Color(0xFFEF9A9A)
val FarmRed = Color(0xFFE53935)

val FarmText = Color(0xFF2C1810)
val FarmTextMuted = Color(0xFF78909C)
val FarmTextLight = Color(0xFFECEFF1)

val FarmBrown = Color(0xFF5D4037)
val FarmBrownDark = Color(0xFF3E2723)
val FarmBrownLight = Color(0xFF8D6E63)

// ===================== 渐变配色方案 =====================

val GradientSunset = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF9966), Color(0xFFFF5E62))
)

val GradientOcean = Brush.horizontalGradient(
    colors = listOf(Color(0xFF2193B0), Color(0xFF6DD5ED))
)

val GradientForest = Brush.horizontalGradient(
    colors = listOf(Color(0xFF134E5E), Color(0xFF71B280))
)

val GradientGold = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF2994A), Color(0xFFF2C94C))
)

val GradientPurple = Brush.horizontalGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

val GradientMint = Brush.horizontalGradient(
    colors = listOf(Color(0xFFA8EDEA), Color(0xFFFED6E3))
)

val GradientAurora = Brush.linearGradient(
    colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE), Color(0xFF43E97B))
)

val GradientSunrise = Brush.linearGradient(
    colors = listOf(Color(0xFFFF512F), Color(0xFFF09819))
)

val GradientDeepSea = Brush.linearGradient(
    colors = listOf(Color(0xFF2E3192), Color(0xFF1BFFFF))
)

val GradientSpring = Brush.horizontalGradient(
    colors = listOf(Color(0xFFA8E063), Color(0xFF56AB2F))
)

val GradientAutumn = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF2994A), Color(0xFFEB5757))
)

val GradientWinter = Brush.horizontalGradient(
    colors = listOf(Color(0xFFE0EAFC), Color(0xFFCFDEF3))
)

val FarmBackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFF8E7), Color(0xFFE8F5E9), Color(0xFFFFECB3))
)

// ===================== 精致阴影阴影 =====================

val FarmSmallShadow = 2.dp
val FarmMediumShadow = 6.dp
val FarmLargeShadow = 12.dp
val FarmHugeShadow = 20.dp

// ===================== 圆角配置 =====================

val FarmRadiusSmall = 8.dp
val FarmRadiusMedium = 12.dp
val FarmRadiusLarge = 16.dp
val FarmRadiusHuge = 24.dp
val FarmRadiusCircle = 9999.dp

// ===================== 主题组件 =====================

@Composable
fun FarmLifeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    primary = FarmGold,
    onPrimary = Color.White,
    secondary = FarmGreen,
    onSecondary = Color.White,
    tertiary = FarmOrange,
    background = Color(0xFFFFFBF0),
    surface = Color.White,
    onBackground = FarmText,
    onSurface = FarmText,
    outline = Color(0xFFD7CCC8)
)

private val Typography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = FarmText
    )
)

// ===================== 渐变卡片容器 =====================

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    gradient: Brush = GradientGold,
    shape: Shape = RoundedCornerShape(FarmRadiusLarge),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .shadow(FarmMediumShadow, shape),
        color = Color.White.copy(alpha = 0.92f),
        shape = shape,
        border = BorderStroke(1.5.dp, Color(0x33FFFFFF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.White.copy(alpha = 0.85f)
                        )
                    )
                ),
            content = content
        )
    }
}

// ===================== 渐变按钮 =====================

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = GradientGold,
    icon: String? = null,
    enabled: Boolean = true
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(150),
        label = "button_scale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(FarmRadiusMedium))
            .clickable(enabled = enabled) {
                pressed = true
                onClick()
                pressed = false
            },
        color = Color.Transparent,
        contentColor = Color.Transparent,
        shadowElevation = if (enabled) FarmMediumShadow else 0.dp,
        shape = RoundedCornerShape(FarmRadiusMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled) gradient else Brush.horizontalGradient(listOf(Color(0xFFBDBDBD), Color(0xFF9E9E9E)))),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                icon?.let { Text(it, fontSize = 14.sp) }
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color(0x40000000),
                            offset = androidx.compose.ui.geometry.Offset(1f, 1f)
                        )
                    )
                )
            }
        }
    }
}

// ===================== 发光按钮 =====================

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = FarmGold,
    icon: String? = null,
    enabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .shadow(if (enabled) 8.dp else 0.dp, ambientColor = glowColor.copy(alpha = glowAlpha))
    ) {
        GradientButton(
            text = text,
            onClick = onClick,
            gradient = Brush.horizontalGradient(
                colors = listOf(glowColor.copy(alpha = 0.9f), glowColor)
            ),
            icon = icon,
            enabled = enabled
        )
    }
}

// ===================== 紧凑状态栏 =====================

@Composable
fun CompactTopBar(
    title: String,
    gold: Long,
    level: Int,
    season: String,
    weather: String,
    onBack: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 返回按钮
            if (onBack != null) {
                Surface(
                    onClick = onBack,
                    color = Color.White.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(FarmRadiusSmall),
                    shadowElevation = 2.dp,
                    border = BorderStroke(1.dp, Color(0x20000000))
                ) {
                    Box(
                        modifier = Modifier.size(44.dp, 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("←", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmBrown)
                    }
                }
            }

            // 标题
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = FarmBrown,
                modifier = Modifier.padding(end = 4.dp)
            )

            // 金币
            Surface(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(FarmRadiusCircle),
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, FarmGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("💰", fontSize = 12.sp)
                    Text(
                        text = formatNumber(gold),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmBrown
                    )
                }
            }

            // 等级
            Surface(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(FarmRadiusCircle),
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, FarmGreen.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("⭐", fontSize = 12.sp)
                    Text(
                        text = "Lv.$level",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FarmBrown
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // 季节
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(season, fontSize = 12.sp)
                Text(weather, fontSize = 12.sp)
            }
        }
    }
}

private fun formatNumber(num: Long): String {
    return when {
        num >= 100000000 -> "${num / 100000000}亿"
        num >= 10000 -> "${num / 10000}万"
        num >= 1000 -> "${num / 1000}k"
        else -> num.toString()
    }
}

// ===================== 数字格式化 =====================

fun formatGold(gold: Long): String = formatNumber(gold)
fun formatLevel(level: Int): String = "Lv.$level"

// ===================== 动画容器 =====================

@Composable
fun ModuleAnimation(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
            scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ),
        exit = fadeOut(animationSpec = tween(200)) +
            scaleOut(
                targetScale = 0.92f,
                animationSpec = tween(200)
            )
    ) {
        content()
    }
}

// ===================== 呼吸光晕组件 =====================

@Composable
fun BreathingGlow(
    color: Color = FarmGold,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = alpha),
                            Color.Transparent
                        )
                    )
                )
        )
        content()
    }
}

// ===================== 悬浮动画组件 =====================

@Composable
fun FloatingAnimation(
    offsetRange: Int = 8,
    duration: Int = 3000,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val float by infiniteTransition.animateFloat(
        initialValue = -offsetRange.toFloat(),
        targetValue = offsetRange.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset"
    )

    Box(modifier = Modifier.offset(y = float.dp)) {
        content()
    }
}

// ===================== 脉冲效果 =====================

@Composable
fun PulseAnimation(
    pulseRange: Float = 0.06f,
    duration: Int = 1800,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f - pulseRange,
        targetValue = 1f + pulseRange,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    Box(modifier = Modifier.scale(scale)) {
        content()
    }
}

// ===================== 渐变卡片 =====================

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush = GradientGold,
    shape: Shape = RoundedCornerShape(FarmRadiusLarge),
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .shadow(FarmMediumShadow, shape),
        color = Color.Transparent,
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            content = content
        )
    }
}

// ===================== 模块图标卡片 =====================

@Composable
fun ModuleIconCard(
    icon: String,
    label: String,
    color: Color,
    description: String? = null,
    isUnlocked: Boolean = true,
    unlockText: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = tween(120),
        label = "module_press"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clickable(enabled = isUnlocked) {
                pressed = true
                onClick()
                pressed = false
            },
        color = Color.White.copy(alpha = 0.95f),
        shape = RoundedCornerShape(FarmRadiusLarge),
        shadowElevation = if (isUnlocked) FarmMediumShadow else 1.dp,
        border = BorderStroke(
            width = 2.dp,
            color = if (isUnlocked) color else Color(0xFFBDBDBD)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(FarmRadiusSmall))
                    .background(
                        if (isUnlocked) color.copy(alpha = 0.18f)
                        else Color(0xFFBDBDBD).copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 22.sp)
            }

            // 文本
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) FarmText else Color(0xFF9E9E9E)
                )
                description?.let {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = if (isUnlocked) FarmTextMuted else Color(0xFFBDBDBD),
                        maxLines = 1
                    )
                }
            }

            // 状态指示
            if (isUnlocked) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(FarmRadiusSmall))
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text("→", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(FarmRadiusSmall))
                        .background(Color(0xFFBDBDBD))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(unlockText, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ===================== 装饰性星星粒子 =====================

@Composable
fun SparkleParticle(
    color: Color = FarmGold,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_alpha"
    )

    Box(
        modifier = modifier.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✦",
            fontSize = 8.sp,
            color = color.copy(alpha = alpha),
            fontWeight = FontWeight.Bold
        )
    }
}

// ===================== 精美渐变文本 =====================

@Composable
fun GradientText(
    text: String,
    gradient: Brush = GradientGold,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        color = Color.White
    )
}

// ===================== 精美进度条 =====================

@Composable
fun DecorativeProgressBar(
    progress: Float,
    color: Color = FarmGold,
    modifier: Modifier = Modifier,
    height: Int = 6
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(FarmRadiusCircle))
            .background(Color(0x33000000))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(FarmRadiusCircle))
                .background(
                    Brush.horizontalGradient(
                        listOf(color.copy(alpha = 0.7f), color, color.copy(alpha = 0.7f))
                    )
                )
        )
    }
}
