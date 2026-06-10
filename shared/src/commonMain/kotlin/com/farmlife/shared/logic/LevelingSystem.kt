package com.farmlife.shared.logic

/**
 * 等级系统
 */
object LevelingSystem {
    
    // 升级所需经验公式：level^2 * 100
    fun expForLevel(level: Int): Int = level * level * 100
    
    // 总经验（从1级到指定等级）
    fun totalExpForLevel(level: Int): Int {
        return (1 until level).sumOf { expForLevel(it) }
    }
    
    /**
     * 根据当前经验计算等级
     */
    fun calculateLevel(totalExp: Int): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForLevel(level)) {
            remaining -= expForLevel(level)
            level++
        }
        return level
    }
    
    /**
     * 获取当前等级进度（0.0 - 1.0）
     */
    fun levelProgress(totalExp: Int): Float {
        val level = calculateLevel(totalExp)
        val currentLevelExp = totalExpForLevel(level)
        val neededForNext = expForLevel(level)
        val progressInLevel = totalExp - currentLevelExp
        return (progressInLevel.toFloat() / neededForNext).coerceIn(0f, 1f)
    }
    
    /**
     * 添加经验并检查是否升级
     */
    fun addExp(currentLevel: Int, currentExp: Int, addedExp: Int): Pair<Int, Int> {
        var level = currentLevel
        var exp = currentExp + addedExp
        
        while (exp >= expForLevel(level)) {
            exp -= expForLevel(level)
            level++
        }
        
        return Pair(level, exp)
    }
}
