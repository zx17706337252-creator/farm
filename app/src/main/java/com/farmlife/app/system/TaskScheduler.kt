package com.farmlife.app.system

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * 任务调度管理器
 */
object TaskScheduler {

    /**
     * 调度宠物探险任务
     */
    fun schedulePetExplore(context: Context, petInstanceId: Long, minutes: Int) {
        val workRequest = OneTimeWorkRequestBuilder<PetExploreWorker>()
            .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
            .addTag("pet_explore_$petInstanceId")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    /**
     * 取消宠物探险任务
     */
    fun cancelPetExplore(context: Context, petInstanceId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag("pet_explore_$petInstanceId")
    }
}
