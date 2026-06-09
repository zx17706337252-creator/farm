package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GameSettings表 - 系统设置
 */
@Entity(tableName = "game_settings")
data class GameSettingsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val musicVolume: Float = 0.7f,
    val soundVolume: Float = 0.8f,
    val language: String = "zh-CN",
    val autoSaveInterval: Int = 60
)
