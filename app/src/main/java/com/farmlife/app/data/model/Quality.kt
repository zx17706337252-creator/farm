package com.farmlife.app.data.model

/**
 * 品质枚举 - 贯穿整个游戏的统一品质系统
 * 普通(白) -> 优良(绿) -> 稀有(蓝) -> 史诗(紫) -> 传说(金) -> 神话(彩)
 */
enum class Quality(val displayName: String, val multiplier: Double, val hexColor: String) {
    COMMON("普通", 1.0, "#F5F5F5"),
    GOOD("优良", 1.2, "#4CAF50"),
    RARE("稀有", 1.5, "#2196F3"),
    EPIC("史诗", 2.2, "#9C27B0"),
    LEGENDARY("传说", 3.5, "#FFD700"),
    MYTHIC("神话", 5.0, "#FF6EC7");

    companion object {
        /** 按照基础概率抽取品质 */
        fun roll(baseQualityBonus: Double = 0.0): Quality {
            val roll = Math.random()
            val adjusted = (roll - baseQualityBonus).coerceIn(0.0, 0.999)
            return when {
                adjusted < 0.85 -> COMMON
                adjusted < 0.95 -> GOOD
                adjusted < 0.99 -> RARE
                adjusted < 0.999 -> EPIC
                else -> LEGENDARY
            }
        }
    }
}
