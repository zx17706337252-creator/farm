package com.farmlife.app.data.enums

enum class FruitTreeType(
    val displayName: String,
    val fruitName: String,
    val fruitEmoji: String,
    val treeEmoji: String,
    val growthTimeSec: Int,
    val baseSellPrice: Int,
    val fruitPerHarvest: Int,
    val unlockLevel: Int
) {
    APPLE_TREE("苹果树", "苹果", "🍎", "🌳", 1800, 12, 3, 20),
    CHERRY_TREE("樱桃树", "樱桃", "🍒", "🌸", 2400, 18, 2, 22),
    PEAR_TREE("梨树", "梨", "🍐", "🌲", 3000, 15, 3, 25),
    ORANGE_TREE("橘子树", "橘子", "🍊", "🌴", 2700, 20, 3, 28),
    PEACH_TREE("桃树", "桃子", "🍑", "🌺", 3300, 22, 2, 32),
    LEMON_TREE("柠檬树", "柠檬", "🍋", "🍋", 3600, 25, 3, 36),
    BANANA_TREE("香蕉树", "香蕉", "🍌", "🌴", 2100, 18, 4, 40),
    OLIVE_TREE("橄榄树", "橄榄", "🫒", "🌿", 4200, 35, 2, 50);
}
