package com.farmlife.shared.logic

import com.farmlife.shared.model.Quality
import kotlin.random.Random

/**
 * 品质系统
 */
object QualitySystem {
    
    // 基础品质权重
    private val baseWeights = intArrayOf(500, 300, 150, 40, 9, 1) // 普通到神话
    
    /**
     * 投掷品质
     * @param bonus 额外加成，正值提高高品质概率
     */
    fun rollQuality(bonus: Double = 0.0): Quality {
        // 应用加成
        val adjustedWeights = baseWeights.mapIndexed { index, weight ->
            val qualityBonus = Quality.fromOrdinal(index).multiplier - 1.0
            (weight * (1 + bonus + qualityBonus * bonus)).toInt().coerceAtLeast(1)
        }
        
        val total = adjustedWeights.sum()
        var roll = Random.nextInt(total)
        
        for ((index, weight) in adjustedWeights.withIndex()) {
            roll -= weight
            if (roll < 0) {
                return Quality.fromOrdinal(index)
            }
        }
        
        return Quality.COMMON
    }
    
    /**
     * 获取品质倍率
     */
    fun qualityMultiplier(qualityIndex: Int): Double {
        return Quality.fromOrdinal(qualityIndex).multiplier
    }
    
    /**
     * 获取品质显示名称
     */
    fun qualityDisplayName(qualityIndex: Int): String {
        return Quality.fromOrdinal(qualityIndex).displayName
    }
    
    /**
     * 获取品质颜色（十六进制）
     */
    fun qualityColorHex(qualityIndex: Int): Long {
        return Quality.fromOrdinal(qualityIndex).colorHex
    }
}
