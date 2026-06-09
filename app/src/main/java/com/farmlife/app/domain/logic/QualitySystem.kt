package com.farmlife.app.domain.logic

import com.farmlife.app.data.model.Quality

/**
 * 品质系统 - 品质乘数 & 品质抽取
 */
object QualitySystem {
    fun qualityMultiplier(quality: Quality): Double = quality.multiplier

    fun qualityMultiplier(qualityIndex: Int): Double {
        val quality = Quality.values().getOrNull(qualityIndex) ?: Quality.COMMON
        return quality.multiplier
    }

    fun rollQuality(bonus: Double = 0.0): Quality = Quality.roll(bonus)

    fun qualityIndex(quality: Quality): Int = quality.ordinal

    fun qualityFromIndex(index: Int): Quality {
        return Quality.values().getOrNull(index) ?: Quality.COMMON
    }

    fun getQualityColor(quality: Quality): String = quality.hexColor

    fun getQualityName(quality: Quality): String = quality.displayName
}

/**
 * 幸运系统 - 影响暴击收获概率、品质加成
 */
object LuckSystem {
    const val BASE_CRIT_DOUBLE = 0.05  // 5% 双倍
    const val BASE_CRIT_TRIPLE = 0.01  // 1% 三倍

    /**
     * 计算暴击倍率
     * @return 1.0(普通), 2.0(双倍), 3.0(三倍)
     */
    fun rollCrit(luckBonus: Double = 0.0): Double {
        val roll = Math.random()
        val doubleChance = BASE_CRIT_DOUBLE + luckBonus
        val tripleChance = BASE_CRIT_TRIPLE + luckBonus * 0.5

        return when {
            roll < tripleChance -> 3.0
            roll < doubleChance + tripleChance -> 2.0
            else -> 1.0
        }
    }

    /**
     * 品质加成抽取概率提升
     */
    fun qualityLuckBonus(luckBonus: Double): Double {
        return luckBonus.coerceAtMost(0.3)
    }
}

/**
 * 收藏评分系统
 */
object CollectionScoreSystem {
    fun scoreForQuality(qualityIndex: Int): Int {
        return when (qualityIndex) {
            0 -> 1      // COMMON
            1 -> 3      // GOOD
            2 -> 10     // RARE
            3 -> 50     // EPIC
            4 -> 200    // LEGENDARY
            5 -> 500    // MYTHIC
            else -> 1
        }
    }

    fun collectionLevel(totalScore: Int): Int {
        return when {
            totalScore >= 5000 -> 30
            totalScore >= 2000 -> 20
            totalScore >= 500 -> 10
            totalScore >= 100 -> 5
            totalScore >= 10 -> 1
            else -> 0
        }
    }

    fun collectionTitle(level: Int): String {
        return when {
            level >= 30 -> "世界收藏家"
            level >= 20 -> "传奇馆长"
            level >= 10 -> "收藏专家"
            level >= 5 -> "农场学者"
            level >= 1 -> "收藏新手"
            else -> "刚入门"
        }
    }
}
