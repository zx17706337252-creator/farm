package com.farmlife.app.ui.screens

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

/**
 * 宠物庄园界面
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Box(modifier = Modifier.fillMaxSize().background(FarmBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopStatusBar(
                gold = player?.gold ?: 0L,
                level = player?.level ?: 1,
                collectionScore = player?.collectionScore ?: 0,
                season = seasonText,
                weather = weatherEmoji,
                onBack = onBack,
                title = "🐕 宠物庄园"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("🐕", fontSize = 28.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "宠物庄园",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = FarmBrown
                        )
                        Text(
                            "${pets.size} 只宠物",
                            fontSize = 12.sp,
                            color = FarmTextMuted
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (pets.isEmpty()) {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text("🐾", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "还没有宠物",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = FarmBrown
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "去商店领养你的第一只宠物吧！",
                                fontSize = 13.sp,
                                color = FarmTextMuted
                            )
                        }
                    }
                } else {
                    pets.forEach { pet ->
                        val cfg = PetConfigs.getById(pet.petId)
                        val qColor = qualityColor(pet.quality)
                        PremiumCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(qColor.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(cfg?.icon ?: "🐕", fontSize = 28.sp)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            cfg?.name ?: "未知宠物",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            "等级 ${pet.level} · 友好度 ${pet.friendship}",
                                            fontSize = 11.sp,
                                            color = FarmTextMuted
                                        )
                                        Text(
                                            "状态: ${if (pet.isActive) "工作中" else "休息中"} · ${cfg?.category ?: "普通"}",
                                            fontSize = 11.sp,
                                            color = FarmTextMuted
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Surface(
                                            color = qColor.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                " ${qualityText(pet.quality)} ",
                                                fontSize = 10.sp,
                                                color = qColor,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(Modifier.height(6.dp))
                                        AnimatedButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    engine.dispatchPetExplore(context, pet.instanceId, 15)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = FarmGrassGreen,
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text("探险", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                if (cfg?.description?.isNotEmpty() == true) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        cfg.description,
                                        fontSize = 11.sp,
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
}
