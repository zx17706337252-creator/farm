package com.farmlife.app.domain.logic

/**
 * 离线收益系统
 * 离线时间最长8小时
 */
object OfflineProgressionSystem {
    const val MAX_OFFLINE_HOURS = 8
    const val MAX_OFFLINE_MS = MAX_OFFLINE_HOURS * 60L * 60L * 1000L

    /**
     * 计算有效离线时间（上限8小时）
     */
    fun effectiveOfflineMs(lastLoginTimeMs: Long, currentTimeMs: Long): Long {
        val offline = currentTimeMs - lastLoginTimeMs
        return offline.coerceIn(0, MAX_OFFLINE_MS)
    }

    /**
     * 判定某任务在离线期间是否完成
     */
    fun taskCompletedOffline(finishTimeMs: Long, lastLoginTimeMs: Long, currentTimeMs: Long): Boolean {
        // 任务完成时间 < 当前时间 且 任务完成时间 > 上次登录（离线时间段内完成）
        return finishTimeMs <= currentTimeMs && finishTimeMs > lastLoginTimeMs
    }

    /**
     * 离线收益总结
     */
    data class OfflineSummary(
        val offlineMinutes: Long,
        val cropsHarvested: Int,
        val animalProducts: Int,
        val factoryItems: Int,
        val coinsGained: Long,
        val expGained: Long
    )
}
