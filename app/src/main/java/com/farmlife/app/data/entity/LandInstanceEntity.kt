package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * LandInstance表 - 玩家拥有的土地实例
 * 每块土地独立等级、坐标
 */
@Entity(tableName = "land_instance")
data class LandInstanceEntity(
    @PrimaryKey(autoGenerate = true) val landId: Long = 0,
    val level: Int = 1,
    val exp: Long = 0,
    val x: Int,
    val y: Int,
    val region: String = "FARMLAND",
    val unlocked: Boolean = true
)
