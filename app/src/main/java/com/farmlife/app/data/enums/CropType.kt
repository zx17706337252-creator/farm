package com.farmlife.app.data.enums

enum class CropType(
    val displayName: String,
    val growthTimeSec: Int,
    val baseSellPrice: Int,
    val baseYield: Int,
    val unlockLevel: Int,
    val emoji: String
) {
    WHEAT("小麦", 30, 3, 3, 1, "🌾"),
    CARROT("胡萝卜", 60, 5, 2, 1, "🥕"),
    RADISH("萝卜", 90, 8, 2, 3, "🌱"),
    STRAWBERRY("草莓", 600, 15, 2, 5, "🍓"),
    CORN("玉米", 300, 12, 2, 7, "🌽"),
    BLUEBERRY("蓝莓", 1800, 25, 3, 10, "🫐"),
    TOMATO("番茄", 480, 14, 3, 12, "🍅"),
    PEPPER("辣椒", 720, 18, 2, 15, "🌶️"),
    POTATO("土豆", 240, 10, 3, 18, "🥔"),
    COFFEE("咖啡", 3600, 40, 2, 20, "☕"),
    TEA("茶叶", 2400, 30, 2, 25, "🍃"),
    RICE("水稻", 600, 15, 4, 28, "🍚"),
    PUMPKIN("南瓜", 900, 22, 2, 30, "🎃"),
    GRAPE("葡萄", 1500, 28, 3, 35, "🍇"),
    WATERMELON("西瓜", 1200, 35, 1, 40, "🍉"),
    PINEAPPLE("菠萝", 1800, 45, 1, 45, "🍍"),
    DRAGON_FRUIT("火龙果", 2700, 60, 1, 50, "🐉"),
    MUSHROOM("蘑菇", 420, 20, 3, 55, "🍄"),
    HERB("香草", 360, 25, 1, 60, "🌿"),
    GOLDEN_FLOWER("金花", 7200, 200, 1, 80, "🌻");

    companion object {
        fun getByLevel(level: Int): List<CropType> {
            return values().filter { it.unlockLevel <= level }
        }
    }
}
