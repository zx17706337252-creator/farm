package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.domain.logic.TutorialSystem
import com.farmlife.app.ui.theme.*

/**
 * 新手引导弹窗 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDialog(
    step: TutorialSystem.Step,
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    step.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = FarmBrown
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    step.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 15.sp,
                    color = FarmText
                )
                Spacer(modifier = Modifier.height(16.dp))
                // 小贴士卡片
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color(0xFFFFF8E1),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, FarmGoldLight),
                    tonalElevation = 0.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("💡 小贴士", fontWeight = FontWeight.Bold, color = FarmGold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            when (step.id) {
                                0 -> "点击底部导航切换不同区域"
                                1 -> "点击土地选择作物种植"
                                2 -> "作物成熟后点击收获获得金币和经验"
                                3 -> "在商店可以购买种子"
                                4 -> "动物会自动生产产品"
                                5 -> "宠物可以自动帮忙干活"
                                6 -> "将原料加工成更有价值的产品"
                                7 -> "果树会持续产出水果"
                                8 -> "养鱼可以获得稀有鱼类"
                                9 -> "收集不同品种增加收藏分数"
                                10 -> "升级宠物设施可以增强宠物能力"
                                else -> "继续探索你的农场吧！"
                            },
                            fontSize = 13.sp,
                            color = FarmTextMuted
                        )
                    }
                }
            }
        },
        confirmButton = {
            AnimatedButton(
                onClick = {
                    onComplete()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("我知道了！", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            if (step.id != 0) {
                TextButton(onClick = onDismiss) {
                    Text("稍后再说", color = FarmTextMuted)
                }
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

/**
 * 功能解锁弹窗 - 精美版本
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockDialog(
    title: String,
    description: String,
    icon: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FarmPink)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(description, style = MaterialTheme.typography.bodyLarge, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color(0xFFFFE0E0),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, FarmPink),
                    tonalElevation = 0.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🎁 新功能解锁！", fontWeight = FontWeight.Bold, color = FarmPink)
                    }
                }
            }
        },
        confirmButton = {
            AnimatedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FarmPink)
            ) {
                Text("太棒了！", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
