package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * CropInstance表 - 作物实例（真正种植的数据）
 * plantTime: 播种时间 (ms)
 * finishTime: 成熟时间 (ms)
 */
@Entity(
    tableName = "crop_instance",
    foreignKeys = [ForeignKey(
        entity = LandInstanceEntity::class,
        parentColumns = ["landId"],
        childColumns = ["landId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("landId")]
)
data class CropInstanceEntity(
    @PrimaryKey(autoGenerate = true) val instanceId: Long = 0,
    val landId: Long,
    val cropId: Int,
    val plantTime: Long,
    val finishTime: Long,
    val quality: Int = 0,
    val harvested: Boolean = false
)
