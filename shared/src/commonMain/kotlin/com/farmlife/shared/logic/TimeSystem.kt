package com.farmlife.shared.logic

import com.farmlife.shared.model.Season
import com.farmlife.shared.model.Weather

/**
 * 时间系统
 */
object TimeSystem {
    
    /**
     * 获取当前时间戳（毫秒）
     */
    fun currentTimeMs(): Long = System.currentTimeMillis()
    
    /**
     * 获取游戏内时间（根据离线时长计算）
     * 最大离线时间为8小时
     */
    fun calculateOfflineProgress(lastSaveTime: Long, maxOfflineHours: Int = 8): Long {
        val now = currentTimeMs()
        val elapsed = now - lastSaveTime
        val maxOfflineMs = maxOfflineHours * 60 * 60 * 1000L
        return minOf(elapsed, maxOfflineMs)
    }
    
    /**
     * 格式化时间差为可读字符串
     */
    fun formatTimeRemaining(finishTime: Long): String {
        val now = currentTimeMs()
        val remaining = finishTime - now
        if (remaining <= 0) return "已完成"
        
        val seconds = remaining / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        
        return when {
            hours > 0 -> "${hours}小时${minutes % 60}分钟"
            minutes > 0 -> "${minutes}分钟${seconds % 60}秒"
            else -> "${seconds}秒"
        }
    }
    
    /**
     * 判断作物是否已成熟
     */
    fun isReady(finishTime: Long): Boolean = currentTimeMs() >= finishTime
    
    /**
     * 获取进度百分比
     */
    fun getProgress(plantTime: Long, finishTime: Long): Float {
        val now = currentTimeMs()
        val total = finishTime - plantTime
        if (total <= 0) return 1f
        val elapsed = now - plantTime
        return (elapsed.toFloat() / total).coerceIn(0f, 1f)
    }
}
