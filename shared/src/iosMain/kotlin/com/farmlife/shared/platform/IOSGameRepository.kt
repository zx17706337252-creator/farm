package com.farmlife.shared.platform

import com.farmlife.shared.domain.repository.GameRepository
import com.farmlife.shared.logic.TimeSystem
import com.farmlife.shared.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * iOS 平台的 GameRepository 实现
 * 使用 SQLite.swift 存储游戏数据
 */
class IOSGameRepository : GameRepository {
    
    // 内存存储（实际项目应使用SQLite.swift）
    private val playerStorage = mutableMapOf<String, Any?>()
    private val landsStorage = mutableListOf<LandInstance>()
    private val cropsStorage = mutableListOf<CropInstance>()
    private val animalsStorage = mutableListOf<AnimalInstance>()
    private val petsStorage = mutableListOf<PetInstance>()
    private val inventoryStorage = mutableListOf<InventoryItem>()
    private val treesStorage = mutableListOf<TreeInstance>()
    private val pondsStorage = mutableListOf<PondInstance>()
    private val fishStorage = mutableListOf<FishInstance>()
    private val factoriesStorage = mutableListOf<FactoryInstance>()
    private val collectionsStorage = mutableListOf<CollectionRecord>()
    private val tutorialCompleted = mutableSetOf<Int>()
    
    init() {
        // 初始化鱼塘
        for (i in 0 until 4) {
            pondsStorage.add(PondInstance(
                pondId = i.toLong(),
                unlocked = false,
                region = "FISHPOND"
            ))
        }
    }
    
    // ===== 玩家数据 =====
    override suspend fun getPlayer(): Player? = playerStorage["player"] as? Player
    
    override suspend fun updatePlayer(player: Player) {
        playerStorage["player"] = player
    }
    
    override suspend fun initializePlayer() {
        if (playerStorage["player"] == null) {
            playerStorage["player"] = Player(gold = 500, level = 1, exp = 0, collectionScore = 0)
            // 初始化9块土地
            for (i in 0 until 9) {
                landsStorage.add(LandInstance(
                    landId = System.currentTimeMillis() + i,
                    region = "FARMLAND",
                    index = i,
                    unlocked = i < 3
                ))
            }
        }
    }
    
    override suspend fun initializeIfNeeded(): Boolean {
        if (playerStorage["player"] == null) {
            initializePlayer()
            return true
        }
        return false
    }
    
    // ===== 土地 =====
    override suspend fun getAllLands(): List<LandInstance> = landsStorage.toList()
    
    override suspend fun getLandById(landId: Long): LandInstance? = 
        landsStorage.find { it.landId == landId }
    
    override suspend fun insertLand(land: LandInstance) {
        landsStorage.add(land)
    }
    
    override suspend fun updateLand(land: LandInstance) {
        val index = landsStorage.indexOfFirst { it.landId == land.landId }
        if (index >= 0) landsStorage[index] = land
    }
    
    override suspend fun getUnlockedLandsCount(): Int = 
        landsStorage.count { it.unlocked }
    
    // ===== 作物 =====
    override suspend fun getAllCrops(): List<CropInstance> = cropsStorage.toList()
    
    override suspend fun getCropByLand(landId: Long): CropInstance? = 
        cropsStorage.find { it.landId == landId }
    
    override suspend fun insertCrop(crop: CropInstance) {
        cropsStorage.add(crop)
    }
    
    override suspend fun updateCrop(crop: CropInstance) {
        val index = cropsStorage.indexOfFirst { it.instanceId == crop.instanceId }
        if (index >= 0) cropsStorage[index] = crop
    }
    
    override suspend fun deleteCrop(instanceId: Long) {
        cropsStorage.removeAll { it.instanceId == instanceId }
    }
    
    // ===== 动物 =====
    override suspend fun getAllAnimals(): List<AnimalInstance> = animalsStorage.toList()
    
    override suspend fun insertAnimal(animal: AnimalInstance) {
        animalsStorage.add(animal)
    }
    
    override suspend fun updateAnimal(animal: AnimalInstance) {
        val index = animalsStorage.indexOfFirst { it.instanceId == animal.instanceId }
        if (index >= 0) animalsStorage[index] = animal
    }
    
    override suspend fun deleteAnimal(instanceId: Long) {
        animalsStorage.removeAll { it.instanceId == instanceId }
    }
    
    // ===== 宠物 =====
    override suspend fun getAllPets(): List<PetInstance> = petsStorage.toList()
    
    override suspend fun insertPet(pet: PetInstance) {
        petsStorage.add(pet)
    }
    
    override suspend fun updatePet(pet: PetInstance) {
        val index = petsStorage.indexOfFirst { it.instanceId == pet.instanceId }
        if (index >= 0) petsStorage[index] = pet
    }
    
    override suspend fun deletePet(instanceId: Long) {
        petsStorage.removeAll { it.instanceId == instanceId }
    }
    
    // ===== 库存 =====
    override suspend fun getInventory(): List<InventoryItem> = inventoryStorage.toList()
    
    override suspend fun addInventoryItem(item: InventoryItem) {
        inventoryStorage.add(item)
    }
    
    override suspend fun updateInventoryItem(item: InventoryItem) {
        val index = inventoryStorage.indexOfFirst { it.itemId == item.itemId }
        if (index >= 0) inventoryStorage[index] = item
    }
    
    override suspend fun deleteInventoryItem(itemId: Long) {
        inventoryStorage.removeAll { it.itemId == itemId }
    }
    
    override suspend fun findInventoryItem(type: String, configId: Int, quality: Int): InventoryItem? =
        inventoryStorage.find { it.itemType == type && it.configId == configId && it.quality == quality }
    
    // ===== 果树 =====
    override suspend fun getAllTrees(): List<TreeInstance> = treesStorage.toList()
    
    override suspend fun insertTree(tree: TreeInstance) {
        treesStorage.add(tree)
    }
    
    override suspend fun updateTree(tree: TreeInstance) {
        val index = treesStorage.indexOfFirst { it.treeId == tree.treeId }
        if (index >= 0) treesStorage[index] = tree
    }
    
    // ===== 鱼塘 =====
    override suspend fun getAllPonds(): List<PondInstance> = pondsStorage.toList()
    
    override suspend fun getPondById(pondId: Long): PondInstance? = 
        pondsStorage.find { it.pondId == pondId }
    
    override suspend fun updatePond(pond: PondInstance) {
        val index = pondsStorage.indexOfFirst { it.pondId == pond.pondId }
        if (index >= 0) pondsStorage[index] = pond
    }
    
    override suspend fun insertPond(pond: PondInstance) {
        pondsStorage.add(pond)
    }
    
    // ===== 鱼类 =====
    override suspend fun getAllFish(): List<FishInstance> = fishStorage.toList()
    
    override suspend fun insertFish(fish: FishInstance) {
        fishStorage.add(fish)
    }
    
    override suspend fun updateFish(fish: FishInstance) {
        val index = fishStorage.indexOfFirst { it.fishId == fish.fishId }
        if (index >= 0) fishStorage[index] = fish
    }
    
    override suspend fun deleteFish(fishId: Long) {
        fishStorage.removeAll { it.fishId == fishId }
    }
    
    // ===== 工厂 =====
    override suspend fun getAllFactories(): List<FactoryInstance> = factoriesStorage.toList()
    
    override suspend fun insertFactory(factory: FactoryInstance) {
        factoriesStorage.add(factory)
    }
    
    override suspend fun updateFactory(factory: FactoryInstance) {
        val index = factoriesStorage.indexOfFirst { it.factoryId == factory.factoryId }
        if (index >= 0) factoriesStorage[index] = factory
    }
    
    // ===== 图鉴 =====
    override suspend fun getAllCollections(): List<CollectionRecord> = collectionsStorage.toList()
    
    override suspend fun findCollection(type: String, configId: Int): CollectionRecord? =
        collectionsStorage.find { it.type == type && it.configId == configId }
    
    override suspend fun insertCollection(record: CollectionRecord) {
        collectionsStorage.add(record)
    }
    
    override suspend fun updateCollection(record: CollectionRecord) {
        val index = collectionsStorage.indexOfFirst { it.recordId == record.recordId }
        if (index >= 0) collectionsStorage[index] = record
    }
    
    // ===== 统计（简化实现）=====
    override suspend fun getStats(): PlayerStatistics = PlayerStatistics()
    
    override suspend fun updateStats(stats: PlayerStatistics) {
        playerStorage["stats"] = stats
    }
    
    // ===== 设置（简化实现）=====
    override suspend fun getSettings(): GameSettings = GameSettings()
    
    override suspend fun updateSettings(settings: GameSettings) {
        playerStorage["settings"] = settings
    }
    
    // ===== 日志 =====
    override suspend fun addLog(type: String, message: String) {
        // iOS端可以使用NSLog或打印到控制台
        println("[$type] $message")
    }
    
    // ===== 教程 =====
    override suspend fun getTutorialProgress(): Set<Int> = tutorialCompleted.toSet()
    
    override suspend fun markTutorialComplete(stepId: Int) {
        tutorialCompleted.add(stepId)
    }
    
    override suspend fun updateTutorialCurrentStep(stepId: Int) {
        playerStorage["currentTutorialStep"] = stepId
    }
}
