package com.farmlife.shared.config

/**
 * 工厂配置
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
 * 生产配方配置
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
    val ALL = listOf(
        // 一级工厂
        FactoryConfig(1, "磨坊", "加工", 15, 3, 5000, "🏭", "将谷物磨成面粉"),
        FactoryConfig(2, "乳品厂", "加工", 20, 3, 10000, "🏭", "加工牛奶制品"),
        FactoryConfig(3, "面包房", "加工", 25, 4, 15000, "🏭", "烤制美味面包"),
        // 二级工厂
        FactoryConfig(11, "果酱厂", "加工", 35, 4, 30000, "🏭", "制作各种果酱"),
        FactoryConfig(12, "饮料厂", "加工", 40, 5, 50000, "🏭", "生产饮料"),
        FactoryConfig(13, "糖果厂", "加工", 45, 5, 80000, "🏭", "制作糖果甜点"),
        // 三级工厂
        FactoryConfig(21, "酿酒厂", "加工", 55, 6, 150000, "🏭", "酿造美酒"),
        FactoryConfig(22, "香料厂", "加工", 60, 6, 200000, "🏭", "提取香料精华"),
        FactoryConfig(23, "炼金工坊", "加工", 70, 8, 500000, "🏭", "炼制神奇药剂")
    )
    
    val RECIPES = listOf(
        RecipeConfig(1, "面粉", "🌾", 1, "CROP", 1, 3, 30, 15, 60),
        RecipeConfig(11, "奶酪", "🧀", 2, "ANIMAL_PRODUCT", 11, 3, 60, 30, 180),
        RecipeConfig(12, "黄油", "🧈", 2, "ANIMAL_PRODUCT", 11, 5, 100, 50, 300),
        RecipeConfig(21, "面包", "🍞", 3, "PROCESSED", 1, 2, 80, 40, 240),
        RecipeConfig(22, "三明治", "🥪", 3, "PROCESSED", 21, 2, 150, 75, 360),
        RecipeConfig(31, "草莓酱", "🍯", 11, "CROP", 21, 3, 200, 100, 480),
        RecipeConfig(32, "蓝莓酱", "🫐", 11, "CROP", 22, 3, 250, 125, 600),
        RecipeConfig(51, "蜂蜜酒", "🍯", 21, "ANIMAL_PRODUCT", 14, 5, 500, 250, 1200)
    )
    
    fun getFactoryById(id: Int): FactoryConfig? = ALL.find { it.factoryId == id }
    fun getRecipeById(id: Int): RecipeConfig? = RECIPES.find { it.recipeId == id }
}
