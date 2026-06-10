package com.farmlife.shared.model

/**
 * 季节
 */
enum class Season(val displayName: String, val emoji: String) {
    SPRING("春天", "🌸"),
    SUMMER("夏天", "☀️"),
    AUTUMN("秋天", "🍂"),
    WINTER("冬天", "❄️");
    
    companion object {
        fun fromOrdinal(ordinal: Int): Season = entries.getOrElse(ordinal % 4) { SPRING }
    }
}
