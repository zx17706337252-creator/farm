package com.farmlife.app.config

/**
 * 动物配置 - 20种动物
 */
data class AnimalConfig(
    val animalId: Int,
    val name: String,
    val category: String,
    val productName: String,
    val productTimeSeconds: Int,
    val productSellPrice: Int,
    val exp: Int,
    val unlockLevel: Int,
    val purchasePrice: Int,
    val icon: String
)

object AnimalConfigs {
    val ALL: List<AnimalConfig> = listOf(
        // === 初级动物 ===
        AnimalConfig(1, "母鸡", "BASIC", "鸡蛋", 300, 15, 5, 10, 200, "🐔"),
        AnimalConfig(2, "奶牛", "BASIC", "牛奶", 900, 40, 10, 10, 800, "🐄"),
        AnimalConfig(3, "绵羊", "BASIC", "羊毛", 1200, 60, 15, 10, 1000, "🐑"),
        AnimalConfig(4, "山羊", "BASIC", "羊奶", 1500, 80, 20, 12, 1200, "🐐"),
        AnimalConfig(5, "蜜蜂", "BASIC", "蜂蜜", 1800, 120, 25, 15, 1500, "🐝"),

        // === 中级动物 ===
        AnimalConfig(11, "鸭子", "MID", "鸭蛋", 600, 25, 8, 15, 400, "🦆"),
        AnimalConfig(12, "火鸡", "MID", "火鸡蛋", 1200, 60, 15, 18, 800, "🦃"),
        AnimalConfig(13, "兔子", "MID", "兔毛", 1800, 90, 20, 20, 1000, "🐰"),
        AnimalConfig(14, "猪", "MID", "松露", 3600, 250, 50, 22, 2500, "🐷"),
        AnimalConfig(15, "鹅", "MID", "鹅蛋", 1500, 70, 18, 20, 900, "🦢"),

        // === 高级动物 ===
        AnimalConfig(21, "奶水牛", "ADVANCED", "高级牛奶", 2400, 180, 35, 30, 5000, "🐃"),
        AnimalConfig(22, "骆驼", "ADVANCED", "驼奶", 3000, 220, 45, 32, 6000, "🐪"),
        AnimalConfig(23, "梅花鹿", "ADVANCED", "鹿茸", 5400, 500, 80, 35, 8000, "🦌"),
        AnimalConfig(24, "羊驼", "ADVANCED", "羊驼毛", 4800, 450, 75, 38, 7500, "🦙"),
        AnimalConfig(25, "孔雀", "ADVANCED", "孔雀羽毛", 7200, 800, 120, 42, 12000, "🦚"),

        // === 特殊动物 ===
        AnimalConfig(31, "黑天鹅", "SPECIAL", "黑天鹅羽毛", 10800, 1500, 200, 45, 20000, "🦢"),
        AnimalConfig(32, "金鸡", "SPECIAL", "金蛋", 14400, 3000, 350, 50, 30000, "🐓"),
        AnimalConfig(33, "银羊", "SPECIAL", "银羊毛", 10800, 2000, 250, 52, 25000, "🐏"),
        AnimalConfig(34, "彩虹牛", "SPECIAL", "彩虹牛奶", 18000, 5000, 500, 55, 50000, "🐮"),
        AnimalConfig(35, "凤凰鸟", "SPECIAL", "凤凰羽", 36000, 15000, 1000, 60, 100000, "🔥")
    )

    fun getById(id: Int): AnimalConfig? = ALL.firstOrNull { it.animalId == id }

    fun getByUnlockLevel(level: Int): List<AnimalConfig> = ALL.filter { it.unlockLevel <= level }

    fun getByCategory(category: String): List<AnimalConfig> = ALL.filter { it.category == category }
}
