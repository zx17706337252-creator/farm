package com.farmlife.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 农场图标 - 用Canvas绘制的精美图标
 * 展现农场主题的创意设计
 */
@Composable
fun FarmAppIcon(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB),      // 天空蓝顶
                        Color(0xFFB5E8D0)       // 亮绿底
                    )
                ),
                shape = CircleShape
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            val width = this.size.width
            val height = this.size.height

            // 绘制土壤（棕色圆底）
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8B5E3C),
                        Color(0xFF6B4226)
                    ),
                    center = center,
                    radius = width * 0.55f
                ),
                radius = width * 0.45f
            )

            // 绘制太阳（右上角金色圆）
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD95C),
                        Color(0xFFF2B705)
                    ),
                    center = center.copy(x = center.x + width * 0.25f, y = center.y - width * 0.25f),
                    radius = width * 0.2f
                ),
                radius = width * 0.12f,
                center = center.copy(x = center.x + width * 0.28f, y = center.y - width * 0.28f)
            )

            // 绘制小树苗 - 树干
            drawRect(
                color = Color(0xFF8B5E3C),
                topLeft = center.copy(x = center.x - width * 0.04f, y = center.y - width * 0.05f),
                size = androidx.compose.ui.geometry.Size(width * 0.08f, width * 0.25f),
                style = Fill
            )

            // 绘制小树苗 - 树叶1
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8BC78A), Color(0xFF5DAF54)),
                    center = center.copy(x = center.x - width * 0.15f, y = center.y - width * 0.1f),
                    radius = width * 0.18f
                ),
                radius = width * 0.15f,
                center = center.copy(x = center.x - width * 0.15f, y = center.y - width * 0.05f)
            )

            // 绘制小树苗 - 树叶2
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8BC78A), Color(0xFF5DAF54)),
                    center = center.copy(x = center.x + width * 0.15f, y = center.y - width * 0.1f),
                    radius = width * 0.18f
                ),
                radius = width * 0.15f,
                center = center.copy(x = center.x + width * 0.15f, y = center.y - width * 0.05f)
            )

            // 绘制小树苗 - 树叶3（顶部）
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFA5D6A7), Color(0xFF5DAF54)),
                    center = center.copy(x = center.x, y = center.y - width * 0.2f),
                    radius = width * 0.2f
                ),
                radius = width * 0.15f,
                center = center.copy(x = center.x, y = center.y - width * 0.18f)
            )

            // 绘制金色星星
            val starColor = Color(0xFFFFD95C)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(starColor, Color(0xFFF2B705)),
                    center = center.copy(x = center.x + width * 0.25f, y = center.y - width * 0.25f),
                    radius = width * 0.1f
                ),
                radius = width * 0.06f,
                center = center.copy(x = center.x + width * 0.28f, y = center.y - width * 0.28f)
            )
        }
    }
}

/**
 * 用户头像装饰 - 可爱的小图标
 * 用于菜单和玩家卡片
 */
@Composable
fun PlayerAvatar(
    size: androidx.compose.ui.unit.Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFE5D3),
                        Color(0xFFF4C99A)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "农",
            fontSize = (size.value * 0.45).sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF8B5E3C)
        )
    }
}
