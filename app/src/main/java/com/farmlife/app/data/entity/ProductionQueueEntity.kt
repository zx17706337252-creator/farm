package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * ProductionQueue表 - 生产队列（离线收益核心表）
 */
@Entity(
    tableName = "production_queue",
    foreignKeys = [ForeignKey(
        entity = FactoryInstanceEntity::class,
        parentColumns = ["factoryId"],
        childColumns = ["factoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductionQueueEntity(
    @PrimaryKey(autoGenerate = true) val queueId: Long = 0,
    val factoryId: Long,
    val recipeId: Int,
    val startTime: Long,
    val finishTime: Long,
    val isCompleted: Boolean = false,
    val isCollected: Boolean = false
)
