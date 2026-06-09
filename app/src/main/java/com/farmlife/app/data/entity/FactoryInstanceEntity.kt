package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * FactoryInstance表 - 加工建筑实例
 */
@Entity(tableName = "factory_instance")
data class FactoryInstanceEntity(
    @PrimaryKey(autoGenerate = true) val factoryId: Long = 0,
    val factoryType: String,
    val level: Int = 1,
    val exp: Long = 0,
    val queueSize: Int = 1,
    val region: String = "PROCESSING",
    val x: Int = 0,
    val y: Int = 0
)
