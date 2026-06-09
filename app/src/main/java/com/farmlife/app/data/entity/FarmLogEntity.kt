package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FarmLog表 - 农场日志（纪念系统）
 */
@Entity(tableName = "farm_log")
data class FarmLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val eventType: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
