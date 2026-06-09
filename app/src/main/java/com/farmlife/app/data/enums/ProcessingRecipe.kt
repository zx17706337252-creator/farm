package com.farmlife.app.data.enums

enum class ProcessingRecipe(
    val displayName: String,
    val outputName: String,
    val outputEmoji: String,
    val inputs: Map<String, Int>,
    val outputAmount: Int,
    val baseSellPrice: Int,
    val processTimeSec: Int,
    val unlockLevel: Int
) {
    BREAD("面包", "面包", "🍞", mapOf("WHEAT" to 3), 1, 25, 120, 30),
    CAKE("蛋糕", "蛋糕", "🍰", mapOf("WHEAT" to 2, "EGG" to 2, "MILK" to 1), 1, 80, 300, 35),
    BUTTER("黄油", "黄油", "🧈", mapOf("MILK" to 3), 1, 45, 180, 32),
    CHEESE("奶酪", "奶酪", "🧀", mapOf("MILK" to 5), 1, 85, 420, 36),
    STRAWBERRY_JAM("草莓酱", "草莓酱", "🍓", mapOf("STRAWBERRY" to 3), 1, 60, 240, 33),
    BLUEBERRY_JAM("蓝莓酱", "蓝莓酱", "🫐", mapOf("BLUEBERRY" to 3), 1, 100, 300, 37),
    JUICE("果汁", "果汁", "🧃", mapOf("TOMATO" to 2, "CARROT" to 1), 1, 45, 150, 34),
    COFFEE_BEAN("咖啡豆", "咖啡豆", "☕", mapOf("COFFEE" to 2), 1, 120, 360, 40),
    YOGURT("酸奶", "酸奶", "🥛", mapOf("MILK" to 2, "STRAWBERRY" to 1), 1, 70, 240, 42),
    ICE_CREAM("冰淇淋", "冰淇淋", "🍦", mapOf("MILK" to 3, "BLUEBERRY" to 1), 1, 110, 420, 45),
    WINE("葡萄酒", "葡萄酒", "🍷", mapOf("GRAPE" to 5), 1, 200, 900, 50),
    PUMPKIN_PIE("南瓜派", "南瓜派", "🥧", mapOf("PUMPKIN" to 2, "WHEAT" to 2, "EGG" to 1), 1, 120, 480, 55);
}
