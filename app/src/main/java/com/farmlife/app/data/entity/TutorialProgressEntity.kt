package com.farmlife.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 新手引导进度表
 */
@Entity(tableName = "tutorial_progress")
data class TutorialProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val currentStep: Int = 0,
    val completedSteps: String = "",
    val isNewPlayer: Boolean = true,
    val lastShownTime: Long = 0
)
