package com.farmlife.app.ui.screens

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmlife.app.config.PetConfigs
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather
import com.farmlife.app.domain.engine.FarmEngine
import com.farmlife.app.ui.theme.*
import kotlinx.coroutines.launch

private fun qualityColor(quality: Int): Color = when (quality) {
    0 -> Color(0xFF9E9E9E)
    1 -> Color(0xFF4CAF50)
    2 -> Color(0xFF2196F3)
    3 -> Color(0xFF9C27B0)
    4 -> Color(0xFFFF9800)
    else -> Color(0xFFE53935)
}

private fun qualityText(quality: Int): String = when (quality) {
    0 -> "普通"
    1 -> "良好"
    2 -> "精良"
    3 -> "稀有"
    4 -> "史诗"
    else -> "传说"
}

@Composable
fun PetScreen(engine: FarmEngine, onBack: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pets by engine.pets.collectAsState()
    val player by engine.player.collectAsState()
    val season by engine.season.collectAsState()
    val weather by engine.weather.collectAsState()

    val seasonText = when (season) {
        Season.SPRING -> "🌸 春天"
        Season.SUMMER -> "☀️ 夏天"
        Season.AUTUMN -> "🍂 秋天"
        Season.WINTER -> "❄️ 冬天"
    }
    val weatherEmoji = when (weather) {
        Weather.SUNNY -> "☀️"
        Weather.CLOUDY -> "☁️"
        Weather.RAINY -> "🌧️"
        Weather.SNOWY -> "❄️"
        Weather.RAINBOW -> "🌈"
        Weather.METEOR -> "☄️"
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFCE4EC),
                    Color(0xFFF8BBD0),
                    Color(0xFFE1F5FE),
                    Color(0xFFB2DFDB)
                )
            )
        )
    ) {
        CompactTopBar(
            title = "🐕 宠物庄园",
            gold = player?.gold ?: 0L,
            level = player?.level ?: 1,
            season = seasonText,
            weather = weatherEmoji,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            GradientCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                gradient = GradientMint,
                shape = RoundedCornerShape(FarmRadiusMedium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐕", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "宠物庄园",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = FarmBrown
                        )
                        Text(
                            text = "${pets.size} 只宠物",
                            fontSize = 10.sp,
                            color = FarmBrown.copy(alpha = 0.8f)
                        )
                    }
                    Text(
                        text = "Lv.${player?.level ?: 1}",
                        fontSize = 11.sp,
                        color = FarmBrown,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (pets.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradient = GradientMint,
                    shape = RoundedCornerShape(FarmRadiusMedium)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("🐾", fontSize = 36.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "还没有宠物",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = FarmBrown
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "去商店领养你的第一只宠物吧！",
                            fontSize = 11.sp,
                            color = FarmBrown.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                pets.forEach { pet ->
                    val cfg = PetConfigs.getById(pet.petId)
                    val qColor = qualityColor(pet.quality)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(FarmRadiusMedium),
                        shadowElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            qColor.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(FarmRadiusSmall))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    qColor.copy(alpha = 0.15f),
                                                    qColor.copy(alpha = 0.3f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(cfg?.icon ?: "🐕", fontSize = 22.sp)
                                }
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        cfg?.name ?: "未知宠物",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = FarmText
                                    )
                                    Text(
                                        "等级 ${pet.level} · 友好度 ${pet.friendship}",
                                        fontSize = 10.sp,
                                        color = FarmTextMuted
                                    )
                                    Text(
                                        "状态: ${if (pet.isActive) "工作中" else "休息中"} · ${cfg?.category ?: "普通"}",
                                        fontSize = 10.sp,
                                        color = FarmTextMuted
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Surface(
                                        color = qColor,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            " ${qualityText(pet.quality)} ",
                                            fontSize = 9.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Surface(
                                        onClick = {
                                            coroutineScope.launch {
                                                engine.dispatchPetExplore(context, pet.instanceId, 15)
                                            }
                                        },
                                        modifier = Modifier
                                            .height(32.dp)
                                            .width(54.dp),
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(FarmRadiusSmall),
                                        shadowElevation = 1.dp
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(GradientForest),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("探险", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                            if (cfg?.description?.isNotEmpty() == true) {
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    cfg.description,
                                    fontSize = 10.sp,
                                    color = FarmTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
