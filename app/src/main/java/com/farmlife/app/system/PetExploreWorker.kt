package com.farmlife.app.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.farmlife.app.FarmLifeApplication
import com.farmlife.app.domain.engine.FarmEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 宠物探险通知系统
 */
class PetExploreWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val engine: FarmEngine by lazy {
        FarmLifeApplication.getInstance().engine
    }

    override suspend fun doWork(): Result {
        val petInstanceId = inputData.getLong("petInstanceId", -1)
        if (petInstanceId == -1L) {
            return Result.failure()
        }

        // 计算探险奖励
        val rewardGold = (Math.random() * 2000 + 500).toLong()
        val rewardExp = (Math.random() * 100 + 20).toInt()

        // 更新玩家数据
        withContext(Dispatchers.IO) {
            val player = engine.player.value ?: return@withContext
            engine.repository.updatePlayer(
                player.copy(gold = player.gold + rewardGold)
            )

            // 更新宠物状态
            val pet = engine.pets.value.firstOrNull { it.instanceId == petInstanceId }
            pet?.let {
                engine.repository.updatePet(it.copy(currentMission = null))
            }
        }

        // 发送通知
        showNotification(petInstanceId, rewardGold, rewardExp)

        return Result.success()
    }

    private fun showNotification(petInstanceId: Long, gold: Long, exp: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pet_explore",
                "宠物探险",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "宠物探险完成通知"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "pet_explore")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🐾 宠物探险归来！")
            .setContentText("获得 💰$gold 金币，⭐$exp 经验")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(petInstanceId.toInt(), notification)
    }
}
