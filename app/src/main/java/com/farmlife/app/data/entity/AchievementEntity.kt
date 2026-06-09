package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Achievement表 - 成就/奖杯
 */
@Entity(tableName = "achievement")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val achievementId: String,
    val displayName: String,
    val progress: Long = 0,
    val target: Long = 1,
    val isCompleted: Boolean = false,
    val completedTime: Long? = null
)
