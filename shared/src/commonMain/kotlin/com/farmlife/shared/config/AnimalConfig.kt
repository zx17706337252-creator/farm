package com.farmlife.shared.config

/**
 * 动物配置
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
    val ALL = listOf(
        // 初级动物 (1-5)
        AnimalConfig(1, "母鸡", "家禽", "鸡蛋", 120, 10, 5, 1, 100, "🐔"),
        AnimalConfig(2, "鸭子", "家禽", "鸭蛋", 180, 15, 8, 2, 200, "🦆"),
        AnimalConfig(3, "兔子", "毛皮", "兔毛", 300, 25, 12, 3, 500, "🐰"),
        AnimalConfig(4, "羊", "毛皮", "羊毛", 600, 50, 25, 5, 1000, "🐑"),
        AnimalConfig(5, "猪", "家畜", "猪肉", 900, 80, 40, 8, 2000, "🐷"),
        
        // 中级动物 (11-15)
        AnimalConfig(11, "牛", "家畜", "牛奶", 600, 100, 50, 12, 5000, "🐄"),
        AnimalConfig(12, "马", "运输", "马匹", 1800, 200, 100, 15, 10000, "🐴"),
        AnimalConfig(13, "羊驼", "珍稀", "羊驼毛", 1200, 300, 150, 18, 15000, "🦙"),
        AnimalConfig(14, "奶牛", "家畜", "高级牛奶", 900, 200, 100, 20, 20000, "🐄"),
        AnimalConfig(15, "火鸡", "家禽", "火鸡蛋", 480, 150, 75, 22, 8000, "🦃"),
        
        // 高级动物 (21-25)
        AnimalConfig(21, "孔雀", "观赏", "孔雀羽", 3600, 500, 250, 30, 50000, "🦚"),
        AnimalConfig(22, "驯鹿", "珍稀", "鹿茸", 7200, 1000, 500, 35, 80000, "🦌"),
        AnimalConfig(23, "独角兽", "传说", "魔法精华", 14400, 5000, 2500, 50, 200000, "🦄"),
        AnimalConfig(24, "金龙", "传说", "龙鳞", 28800, 10000, 5000, 60, 500000, "🐉"),
        AnimalConfig(25, "凤凰", "神话", "凤凰羽毛", 43200, 20000, 10000, 70, 1000000, "🦅")
    )
    
    fun getById(id: Int): AnimalConfig? = ALL.find { it.animalId == id }
}
