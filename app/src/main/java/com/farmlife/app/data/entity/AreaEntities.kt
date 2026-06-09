package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 果树实例表
 */
@Entity(tableName = "tree_instance")
data class TreeInstanceEntity(
    @PrimaryKey(autoGenerate = true) val treeId: Long = 0,
    val configId: Int,
    val x: Int,
    val y: Int,
    val plantedTime: Long,
    val nextHarvestTime: Long,
    val level: Int = 1,
    val exp: Long = 0
)

/**
 * 鱼塘实例表
 */
@Entity(tableName = "fish_instance")
data class FishInstanceEntity(
    @PrimaryKey(autoGenerate = true) val fishId: Long = 0,
    val configId: Int,
    val pondId: Int,
    val placedTime: Long,
    val finishTime: Long,
    val quality: Int = 0
)

/**
 * 鱼塘池塘表
 */
@Entity(tableName = "pond_instance")
data class PondInstanceEntity(
    @PrimaryKey(autoGenerate = true) val pondId: Long = 0,
    val x: Int,
    val y: Int,
    val unlocked: Boolean = false
)

/**
 * 宠物设施表
 */
@Entity(tableName = "pet_facility")
data class PetFacilityEntity(
    @PrimaryKey(autoGenerate = true) val facilityId: Long = 0,
    val configId: Int,
    val level: Int = 1,
    val x: Int = 0,
    val y: Int = 0,
    val isActive: Boolean = false
)
