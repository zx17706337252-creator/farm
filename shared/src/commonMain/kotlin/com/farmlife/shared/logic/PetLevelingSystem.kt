package com.farmlife.shared.logic

import com.farmlife.shared.config.PetConfigs

/**
 * 宠物等级系统
 */
object PetLevelingSystem {
    
    // 宠物升级所需经验：level^1.5 * 50
    fun expForLevel(level: Int): Int = (level * level * 1.5 * 50).toInt()
    
    /**
     * 计算宠物等级
     */
    fun calculateLevel(totalExp: Int): Int {
        var level = 1
        var remaining = totalExp
        while (remaining >= expForLevel(level) && level < 100) {
            remaining -= expForLevel(level)
            level++
        }
        return level
    }
    
    /**
     * 计算宠物工作槽位
     */
    fun workSlots(level: Int): Int {
        return when {
            level >= 50 -> 5
            level >= 30 -> 4
            level >= 20 -> 3
            level >= 10 -> 2
            else -> 1
        }
    }
    
    /**
     * 计算宠物工作效率
     */
    fun workEfficiency(petId: Int, level: Int): Double {
        val config = PetConfigs.getById(petId) ?: return 1.0
        val baseEfficiency = config.baseSpeed
        val levelBonus = 1.0 + (level - 1) * 0.1 // 每级提升10%
        return baseEfficiency * levelBonus
    }
    
    /**
     * 计算宠物工作范围
     */
    fun workRange(petId: Int, level: Int): Int {
        val config = PetConfigs.getById(petId) ?: return 5
        return (config.baseRange * (1.0 + (level - 1) * 0.05)).toInt() // 每级提升5%
    }
    
    /**
     * 添加经验
     */
    fun addExp(currentLevel: Int, currentExp: Int, addedExp: Int): Pair<Int, Int> {
        var level = currentLevel
        var exp = currentExp + addedExp
        
        while (exp >= expForLevel(level) && level < 100) {
            exp -= expForLevel(level)
            level++
        }
        
        return Pair(level, exp)
    }
}
