package com.farmlife.app.config

/**
 * 加工建筑配置 - 15座加工建筑
 */
data class FactoryConfig(
    val factoryId: Int,
    val name: String,
    val category: String,
    val unlockLevel: Int,
    val baseQueueSize: Int,
    val purchasePrice: Int,
    val icon: String,
    val description: String
)

/**
 * 加工配方 - 原料 -> 成品
 */
data class RecipeConfig(
    val recipeId: Int,
    val name: String,
    val icon: String,
    val factoryId: Int,
    val inputItemType: String,
    val inputConfigId: Int,
    val inputQuantity: Int,
    val outputSellPrice: Int,
    val outputExp: Int,
    val processTimeSeconds: Int
)

object FactoryConfigs {
    val ALL: List<FactoryConfig> = listOf(
        // === 一级建筑 ===
        FactoryConfig(1, "面粉坊", "TIER1", 30, 1, 2000, "🌾", "将小麦加工成面粉"),
        FactoryConfig(2, "乳品厂", "TIER1", 30, 1, 3000, "🥛", "将牛奶加工成乳制品"),
        FactoryConfig(3, "榨油坊", "TIER1", 32, 1, 3500, "🫒", "压榨植物油"),
        FactoryConfig(4, "糖厂", "TIER1", 34, 1, 4000, "🍯", "将甘蔗加工成糖"),
        FactoryConfig(5, "纺织坊", "TIER1", 35, 1, 4500, "🧵", "将棉花加工成布料"),

        // === 二级建筑 ===
        FactoryConfig(11, "面包坊", "TIER2", 35, 2, 8000, "🍞", "烘焙面包和三明治"),
        FactoryConfig(12, "果酱坊", "TIER2", 36, 2, 9000, "🍯", "制作果酱"),
        FactoryConfig(13, "甜品屋", "TIER2", 38, 2, 10000, "🍰", "制作高级甜品"),
        FactoryConfig(14, "咖啡馆", "TIER2", 40, 2, 12000, "☕", "精品咖啡制作"),
        FactoryConfig(15, "茶工坊", "TIER2", 42, 2, 12000, "🍵", "精品茶叶加工"),

        // === 三级建筑 ===
        FactoryConfig(21, "高级餐厅", "TIER3", 45, 3, 30000, "🍽️", "高级料理，订单大户"),
        FactoryConfig(22, "香水工坊", "TIER3", 48, 3, 35000, "🌸", "花卉加工成香水"),
        FactoryConfig(23, "巧克力工厂", "TIER3", 50, 3, 40000, "🍫", "精品巧克力"),
        FactoryConfig(24, "蜂蜜工坊", "TIER3", 52, 3, 45000, "🍯", "皇家蜂蜜制品"),
        FactoryConfig(25, "收藏工坊", "TIER3", 60, 3, 100000, "🏛️", "传奇收藏品加工")
    )

    val RECIPES: List<RecipeConfig> = listOf(
        // 面粉坊
        RecipeConfig(1, "面粉", "🌾", 1, "CROP", 1, 3, 20, 3, 30),
        // 乳品厂
        RecipeConfig(11, "奶酪", "🧀", 2, "ANIMAL", 2, 2, 120, 15, 300),
        RecipeConfig(12, "黄油", "🧈", 2, "ANIMAL", 2, 3, 300, 25, 600),
        // 面包坊
        RecipeConfig(21, "面包", "🍞", 11, "PROCESSED", 1, 2, 80, 10, 120),
        RecipeConfig(22, "三明治", "🥪", 11, "PROCESSED", 1, 4, 250, 30, 300),
        // 果酱坊
        RecipeConfig(31, "草莓酱", "🍯", 12, "CROP", 21, 3, 200, 20, 300),
        RecipeConfig(32, "蓝莓酱", "🫐", 12, "CROP", 22, 2, 500, 40, 600),
        // 咖啡馆
        RecipeConfig(41, "咖啡", "☕", 14, "CROP", 31, 2, 2500, 80, 1800),
        // 蜂蜜工坊
        RecipeConfig(51, "蜂蜜糖浆", "🍯", 24, "ANIMAL", 5, 2, 300, 30, 600),
        RecipeConfig(52, "蜂蜜蛋糕", "🍰", 24, "ANIMAL", 5, 3, 800, 60, 1200)
    )

    fun getFactoryById(id: Int): FactoryConfig? = ALL.firstOrNull { it.factoryId == id }

    fun getRecipesByFactory(factoryId: Int): List<RecipeConfig> = RECIPES.filter { it.factoryId == factoryId }

    fun getRecipeById(id: Int): RecipeConfig? = RECIPES.firstOrNull { it.recipeId == id }

    fun getByUnlockLevel(level: Int): List<FactoryConfig> = ALL.filter { it.unlockLevel <= level }
}
