package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * InventoryItem表 - 仓库物品
 * itemType: CROP / ANIMAL_PRODUCT / PROCESSED / PET / ANIMAL / DECORATION
 */
@Entity(tableName = "inventory_item")
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true) val itemId: Long = 0,
    val itemType: String,
    val configId: Int,
    val quantity: Long,
    val quality: Int = 0,
    val obtainTime: Long = System.currentTimeMillis()
)
