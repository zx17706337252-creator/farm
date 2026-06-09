package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * AnimalInstance表 - 动物实例
 * friendship: 亲密度 0-100
 */
@Entity(tableName = "animal_instance")
data class AnimalInstanceEntity(
    @PrimaryKey(autoGenerate = true) val instanceId: Long = 0,
    val animalId: Int,
    val level: Int = 1,
    val exp: Long = 0,
    val friendship: Int = 0,
    val quality: Int = 0,
    val lastProduceTime: Long = System.currentTimeMillis(),
    val isTired: Boolean = false,
    val region: String = "LIVESTOCK"
)
