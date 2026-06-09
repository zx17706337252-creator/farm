package com.farmlife.app.data.enums

enum class AnimalType(
    val displayName: String,
    val productName: String,
    val productEmoji: String,
    val animalEmoji: String,
    val productionTimeSec: Int,
    val baseSellPrice: Int,
    val baseRareDropChance: Double,
    val unlockLevel: Int,
    val feedCost: Int
) {
    CHICKEN("鸡", "鸡蛋", "🥚", "🐔", 300, 8, 0.05, 10, 2),
    COW("牛", "牛奶", "🥛", "🐄", 900, 20, 0.03, 15, 5),
    SHEEP("羊", "羊毛", "🧶", "🐑", 1200, 25, 0.04, 18, 6),
    BEE("蜜蜂", "蜂蜜", "🍯", "🐝", 600, 15, 0.06, 20, 3),
    DUCK("鸭", "鸭蛋", "🪺", "🦆", 420, 12, 0.04, 25, 3),
    PIG("猪", "松露", "🟤", "🐷", 1800, 45, 0.08, 30, 10),
    RABBIT("兔", "兔毛", "⚪", "🐰", 720, 18, 0.05, 35, 4),
    GOAT("山羊", "山羊奶", "🥛", "🐐", 1000, 30, 0.05, 40, 7);

    companion object {
        fun getByLevel(level: Int): List<AnimalType> {
            return values().filter { it.unlockLevel <= level }
        }
    }
}
