package com.farmlife.shared.model

/**
 * 物品品质等级
 */
enum class Quality(
    val intValue: Int,
    val displayName: String,
    val colorHex: Long,
    val multiplier: Double
) {
    COMMON(0, "普通", 0xFF9E9E9E, 1.0),
    GOOD(1, "优良", 0xFF4CAF50, 1.2),
    RARE(2, "稀有", 0xFF2196F3, 1.5),
    EPIC(3, "史诗", 0xFF9C27B0, 2.0),
    LEGENDARY(4, "传说", 0xFFFF9800, 3.0),
    MYTHICAL(5, "神话", 0xFFE91E63, 5.0);
    
    companion object {
        fun fromOrdinal(ordinal: Int): Quality = entries.getOrElse(ordinal) { COMMON }
        fun fromInt(value: Int): Quality = fromOrdinal(value)
    }
}
