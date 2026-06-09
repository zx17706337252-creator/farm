package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Collection表 - 收藏记录
 * collectionType: CROP / ANIMAL / PET / DECORATION / LEGENDARY_ITEM
 */
@Entity(tableName = "collection_record")
data class CollectionRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collectionType: String,
    val configId: Int,
    val highestQuality: Int = 0,
    val firstObtainTime: Long = System.currentTimeMillis(),
    val totalObtained: Int = 1,
    val isUnlocked: Boolean = true
)
