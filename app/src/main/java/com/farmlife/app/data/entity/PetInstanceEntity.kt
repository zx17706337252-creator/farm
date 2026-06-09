package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * PetInstance表 - 宠物实例
 */
@Entity(tableName = "pet_instance")
data class PetInstanceEntity(
    @PrimaryKey(autoGenerate = true) val instanceId: Long = 0,
    val petId: Int,
    val level: Int = 1,
    val exp: Long = 0,
    val friendship: Int = 0,
    val quality: Int = 0,
    val currentMission: String? = null,
    val workSlots: Int = 1,
    val isActive: Boolean = true,
    val region: String = "PET_ESTATE"
)
