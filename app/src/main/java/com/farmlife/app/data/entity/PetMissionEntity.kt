package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * PetMission表 - 宠物任务（探险/自动工作）
 */
@Entity(
    tableName = "pet_mission",
    foreignKeys = [ForeignKey(
        entity = PetInstanceEntity::class,
        parentColumns = ["instanceId"],
        childColumns = ["petInstanceId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PetMissionEntity(
    @PrimaryKey(autoGenerate = true) val missionId: Long = 0,
    val petInstanceId: Long,
    val missionType: String,
    val startTime: Long,
    val finishTime: Long,
    val isCompleted: Boolean = false
)
