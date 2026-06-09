package com.farmlife.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.domain.logic.TutorialSystem

/**
 * 新手引导弹窗
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
                Text(step.title, style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(step.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("💡 小贴士", fontWeight = FontWeight.Bold)
                        Text(
                            when (step.id) {
                                0 -> "点击底部导航可以切换不同区域"
                                1 -> "小麦生长最快，只需30秒"
                                2 -> "高品质作物售价更高"
                                3 -> "商店物品需要达到指定等级才能购买"
                                4 -> "动物需要喂养才能保持高效率"
                                5 -> "宠物会自动工作，记得给它们升级"
                                6 -> "加工品价值是原料的数倍"
                                7 -> "果树种植后会持续产出"
                                8 -> "鱼塘需要先解锁才能使用"
                                9 -> "收藏完成可以获得奖励"
                                10 -> "宠物设施可以提升宠物能力"
                                else -> "继续探索你的农场吧！"
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onComplete()
                onDismiss()
            }) {
                Text("知道了")
            }
        },
        dismissButton = {
            if (step.id != 0) {
                TextButton(onClick = onDismiss) {
                    Text("稍后再说")
                }
            }
        }
    )
}

/**
 * 功能解锁弹窗
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
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("🎁 新功能解锁！", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("太棒了！")
            }
        }
    )
}
