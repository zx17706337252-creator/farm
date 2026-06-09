package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Player表 - 玩家主体数据
 */
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val name: String = "农场主",
    val level: Int = 1,
    val exp: Long = 0,
    val gold: Long = 500,
    val diamond: Int = 0,
    val farmLevel: Int = 1,
    val createTime: Long = System.currentTimeMillis(),
    val lastLoginTime: Long = System.currentTimeMillis(),
    val collectionScore: Int = 0,
    val automationRate: Int = 0
)
