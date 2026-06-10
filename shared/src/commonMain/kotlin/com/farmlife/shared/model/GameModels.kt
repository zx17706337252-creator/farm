package com.farmlife.shared.model

/**
 * 玩家数据
 */
data class Player(
    val gold: Long = 500,
    val level: Int = 1,
    val exp: Int = 0,
    val collectionScore: Int = 0
)

/**
 * 土地实例
 */
data class LandInstance(
    val landId: Long = 0,
    val region: String = "FARMLAND",
    val index: Int = 0,
    val unlocked: Boolean = false
)

/**
 * 作物实例
 */
data class CropInstance(
    val instanceId: Long = 0,
    val landId: Long = 0,
    val cropId: Int = 0,
    val plantTime: Long = 0,
    val finishTime: Long = 0,
    val quality: Int = 0
)

/**
 * 动物实例
 */
data class AnimalInstance(
    val instanceId: Long = 0,
    val animalId: Int = 0,
    val level: Int = 1,
    val exp: Int = 0,
    val lastProduceTime: Long = 0,
    val quality: Int = 0
)

/**
 * 宠物实例
 */
data class PetInstance(
    val instanceId: Long = 0,
    val petId: Int = 0,
    val level: Int = 1,
    val exp: Int = 0,
    val friendliness: Int = 50,
    val isActive: Boolean = false,
    val workSlots: Int = 1
)

/**
 * 库存物品
 */
data class InventoryItem(
    val itemId: Long = 0,
    val itemType: String = "",
    val configId: Int = 0,
    val quantity: Int = 0,
    val quality: Int = 0
)

/**
 * 果树实例
 */
data class TreeInstance(
    val treeId: Long = 0,
    val configId: Int = 0,
    val plantTime: Long = 0,
    val harvestTime: Long = 0,
    val quality: Int = 0,
    val region: String = "ORCHARD"
)

/**
 * 鱼塘实例
 */
data class PondInstance(
    val pondId: Long = 0,
    val unlocked: Boolean = false,
    val region: String = "FISHPOND"
)

/**
 * 鱼类实例
 */
data class FishInstance(
    val fishId: Long = 0,
    val pondId: Long = 0,
    val configId: Int = 0,
    val finishTime: Long = 0,
    val quality: Int = 0
)

/**
 * 工厂实例
 */
data class FactoryInstance(
    val factoryId: Long = 0,
    val factoryType: String = "",
    val level: Int = 1,
    val unlocked: Boolean = false
)

/**
 * 生产队列项
 */
data class ProductionQueueItem(
    val queueId: Long = 0,
    val factoryId: Long = 0,
    val recipeId: Int = 0,
    val startTime: Long = 0,
    val finishTime: Long = 0,
    val status: String = "PENDING"
)

/**
 * 图鉴记录
 */
data class CollectionRecord(
    val recordId: Long = 0,
    val type: String = "",
    val configId: Int = 0,
    val name: String = "",
    val highestQuality: Int = 0,
    val count: Int = 0
)

/**
 * 游戏设置
 */
data class GameSettings(
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val autoSaveInterval: Int = 60
)

/**
 * 玩家统计
 */
data class PlayerStatistics(
    val totalPlant: Int = 0,
    val totalHarvest: Int = 0,
    val totalEarned: Long = 0,
    val totalSpent: Long = 0,
    val playTime: Long = 0
)
