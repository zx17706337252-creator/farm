package com.farmlife.app.domain.logic

/**
 * 玩家升级公式系统
 * 等级越高，所需经验越多
 * 公式：expNeeded(level) = level * 50 + level^2 * 10
 */
object LevelingSystem {
    fun expForNextLevel(currentLevel: Int): Long {
        val level = currentLevel.coerceAtLeast(1)
        return (level * 50L + level * level * 10L)
    }

    fun totalExpForLevel(targetLevel: Int): Long {
        var total = 0L
        for (i in 1 until targetLevel) {
            total += expForNextLevel(i)
        }
        return total
    }

    fun calculateLevel(totalExp: Long): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForNextLevel(level)) {
            remaining -= expForNextLevel(level)
            level++
        }
        return level
    }

    fun expInCurrentLevel(totalExp: Long): Long {
        val currentLevel = calculateLevel(totalExp)
        var consumed = 0L
        for (i in 1 until currentLevel) {
            consumed += expForNextLevel(i)
        }
        return totalExp - consumed
    }

    fun progressPercent(totalExp: Long): Float {
        val currentLevel = calculateLevel(totalExp)
        val needed = expForNextLevel(currentLevel).toFloat()
        val inLevel = expInCurrentLevel(totalExp).toFloat()
        return (inLevel / needed).coerceIn(0f, 1f)
    }
}

/**
 * 土地等级系统
 * 1-10级，影响成熟速度、产量
 */
object LandLevelingSystem {
    private const val MAX_LEVEL = 10

    fun speedMultiplier(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1.0 + (lv - 1) * 0.05
    }

    fun yieldMultiplier(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1.0 + (lv - 1) * 0.05
    }

    fun expForNextLevel(currentLevel: Int): Long {
        val lv = currentLevel.coerceAtLeast(1)
        return lv * 20L
    }

    fun calculateLevel(totalExp: Long): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForNextLevel(level) && level < MAX_LEVEL) {
            remaining -= expForNextLevel(level)
            level++
        }
        return level
    }
}

/**
 * 动物等级系统
 * 1-20级，影响生产效率、稀有掉落率
 */
object AnimalLevelingSystem {
    private const val MAX_LEVEL = 20

    fun efficiencyMultiplier(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1.0 + (lv - 1) * 0.03
    }

    fun rareDropBonus(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return (lv - 1) * 0.002
    }

    fun expForNextLevel(currentLevel: Int): Long {
        val lv = currentLevel.coerceAtLeast(1)
        return lv * 30L
    }

    fun calculateLevel(totalExp: Long): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForNextLevel(level) && level < MAX_LEVEL) {
            remaining -= expForNextLevel(level)
            level++
        }
        return level
    }
}

/**
 * 宠物等级系统
 * 1-100级，影响移动速度、工作范围、幸运值
 */
object PetLevelingSystem {
    private const val MAX_LEVEL = 100

    fun workSpeedMultiplier(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1.0 + (lv - 1) * 0.015
    }

    fun workRange(level: Int): Int {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 5 + (lv / 2)
    }

    fun luckyBonus(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return (lv - 1) * 0.001
    }

    fun workSlots(level: Int): Int {
        return when {
            level >= 100 -> 5
            level >= 80 -> 4
            level >= 50 -> 3
            level >= 20 -> 2
            else -> 1
        }
    }

    fun expForNextLevel(currentLevel: Int): Long {
        val lv = currentLevel.coerceAtLeast(1)
        return lv * 50L
    }

    fun calculateLevel(totalExp: Long): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForNextLevel(level) && level < MAX_LEVEL) {
            remaining -= expForNextLevel(level)
            level++
        }
        return level
    }
}

/**
 * 建筑等级系统
 * 1-20级
 */
object BuildingLevelingSystem {
    private const val MAX_LEVEL = 20

    fun speedMultiplier(level: Int): Double {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1.0 + (lv - 1) * 0.04
    }

    fun queueSize(level: Int): Int {
        val lv = level.coerceIn(1, MAX_LEVEL)
        return 1 + (lv / 5)
    }

    fun expForNextLevel(currentLevel: Int): Long {
        val lv = currentLevel.coerceAtLeast(1)
        return lv * 100L
    }

    fun calculateLevel(totalExp: Long): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForNextLevel(level) && level < MAX_LEVEL) {
            remaining -= expForNextLevel(level)
            level++
        }
        return level
    }
}
