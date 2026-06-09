package com.farmlife.app.domain.logic

import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather

/**
 * 时间/季节/天气系统
 */
object TimeSystem {
    fun currentTimeMs(): Long = System.currentTimeMillis()

    fun nowSeconds(): Long = System.currentTimeMillis() / 1000

    /**
     * 计算作物成熟时间（毫秒）
     */
    fun calculateCropFinishTime(baseSeconds: Int, landLevel: Int = 1): Long {
        val speedMul = LandLevelingSystem.speedMultiplier(landLevel)
        val adjustedSeconds = (baseSeconds / speedMul).toLong()
        return currentTimeMs() + adjustedSeconds * 1000
    }

    /**
     * 计算动物生产完成时间（毫秒）
     */
    fun calculateAnimalProduceTime(baseSeconds: Int, animalLevel: Int = 1): Long {
        val speedMul = AnimalLevelingSystem.efficiencyMultiplier(animalLevel)
        val adjustedSeconds = (baseSeconds / speedMul).toLong()
        return currentTimeMs() + adjustedSeconds * 1000
    }

    /**
     * 计算加工完成时间（毫秒）
     */
    fun calculateProcessFinishTime(baseSeconds: Int, factoryLevel: Int = 1): Long {
        val speedMul = BuildingLevelingSystem.speedMultiplier(factoryLevel)
        val adjustedSeconds = (baseSeconds / speedMul).toLong()
        return currentTimeMs() + adjustedSeconds * 1000
    }

    /**
     * 格式化剩余时间
     */
    fun formatRemainingTime(finishTimeMs: Long): String {
        val remaining = finishTimeMs - currentTimeMs()
        if (remaining <= 0) return "已完成"

        val totalSeconds = remaining / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return when {
            hours > 0 -> "${hours}小时${minutes}分"
            minutes > 0 -> "${minutes}分${seconds}秒"
            else -> "${seconds}秒"
        }
    }

    fun progressPercent(startTimeMs: Long, finishTimeMs: Long): Float {
        val total = finishTimeMs - startTimeMs
        if (total <= 0) return 1f
        val elapsed = currentTimeMs() - startTimeMs
        return (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    }

    /**
     * 根据游戏天数计算当前季节
     */
    fun currentSeason(startGameTimeMs: Long): Season {
        val daysPlayed = (currentTimeMs() - startGameTimeMs) / (1000 * 60 * 60 * 24)
        return Season.fromDay(daysPlayed)
    }

    fun currentWeather(): Weather {
        val roll = Math.random()
        return when {
            roll < 0.01 -> Weather.METEOR
            roll < 0.03 -> Weather.RAINBOW
            roll < 0.2 -> Weather.RAINY
            roll < 0.4 -> Weather.CLOUDY
            roll < 0.5 -> Weather.SNOWY
            else -> Weather.SUNNY
        }
    }
}
