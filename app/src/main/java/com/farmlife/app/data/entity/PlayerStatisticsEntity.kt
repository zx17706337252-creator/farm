package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * PlayerStatistics表 - 玩家统计（用于成就/收藏馆）
 */
@Entity(tableName = "player_statistics")
data class PlayerStatisticsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val totalHarvest: Long = 0,
    val totalPlant: Long = 0,
    val totalAnimalProduct: Long = 0,
    val totalGoldEarned: Long = 0,
    val totalPlayTimeMinutes: Long = 0,
    val totalLegendaryFound: Int = 0,
    val totalPetObtained: Int = 0,
    val totalAnimalObtained: Int = 0
)
