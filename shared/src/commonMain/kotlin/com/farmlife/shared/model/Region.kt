package com.farmlife.shared.model

/**
 * 区域类型
 */
enum class Region(val displayName: String, val emoji: String, val unlockLevel: Int) {
    FARMLAND("农田", "🌾", 1),
    LIVESTOCK("牧场", "🐄", 10),
    ORCHARD("果园", "🍎", 20),
    FACTORY("工坊", "🏭", 30),
    FISHPOND("鱼塘", "🐟", 40),
    PET_ESTATE("宠物庄园", "🐕", 12),
    MUSEUM("博物馆", "🏛️", 50);
}
