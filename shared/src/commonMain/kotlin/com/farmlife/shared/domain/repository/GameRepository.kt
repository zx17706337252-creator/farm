package com.farmlife.shared.domain.repository

import com.farmlife.shared.model.*

/**
 * 游戏数据仓库接口
 * 定义所有数据操作的抽象方法，由各平台实现
 */
interface GameRepository {
    
    // ===== 玩家数据 =====
    suspend fun getPlayer(): Player?
    suspend fun updatePlayer(player: Player)
    suspend fun initializePlayer()
    
    // ===== 土地 =====
    suspend fun getAllLands(): List<LandInstance>
    suspend fun getLandById(landId: Long): LandInstance?
    suspend fun insertLand(land: LandInstance)
    suspend fun updateLand(land: LandInstance)
    suspend fun getUnlockedLandsCount(): Int
    
    // ===== 作物 =====
    suspend fun getAllCrops(): List<CropInstance>
    suspend fun getCropByLand(landId: Long): CropInstance?
    suspend fun insertCrop(crop: CropInstance)
    suspend fun updateCrop(crop: CropInstance)
    suspend fun deleteCrop(instanceId: Long)
    
    // ===== 动物 =====
    suspend fun getAllAnimals(): List<AnimalInstance>
    suspend fun insertAnimal(animal: AnimalInstance)
    suspend fun updateAnimal(animal: AnimalInstance)
    suspend fun deleteAnimal(instanceId: Long)
    
    // ===== 宠物 =====
    suspend fun getAllPets(): List<PetInstance>
    suspend fun insertPet(pet: PetInstance)
    suspend fun updatePet(pet: PetInstance)
    suspend fun deletePet(instanceId: Long)
    
    // ===== 库存 =====
    suspend fun getInventory(): List<InventoryItem>
    suspend fun addInventoryItem(item: InventoryItem)
    suspend fun updateInventoryItem(item: InventoryItem)
    suspend fun deleteInventoryItem(itemId: Long)
    suspend fun findInventoryItem(type: String, configId: Int, quality: Int): InventoryItem?
    
    // ===== 果树 =====
    suspend fun getAllTrees(): List<TreeInstance>
    suspend fun insertTree(tree: TreeInstance)
    suspend fun updateTree(tree: TreeInstance)
    
    // ===== 鱼塘 =====
    suspend fun getAllPonds(): List<PondInstance>
    suspend fun getPondById(pondId: Long): PondInstance?
    suspend fun updatePond(pond: PondInstance)
    
    // ===== 鱼类 =====
    suspend fun getAllFish(): List<FishInstance>
    suspend fun insertFish(fish: FishInstance)
    suspend fun updateFish(fish: FishInstance)
    suspend fun deleteFish(fishId: Long)
    
    // ===== 工厂 =====
    suspend fun getAllFactories(): List<FactoryInstance>
    suspend fun insertFactory(factory: FactoryInstance)
    suspend fun updateFactory(factory: FactoryInstance)
    
    // ===== 图鉴 =====
    suspend fun getAllCollections(): List<CollectionRecord>
    suspend fun findCollection(type: String, configId: Int): CollectionRecord?
    suspend fun insertCollection(record: CollectionRecord)
    suspend fun updateCollection(record: CollectionRecord)
    
    // ===== 统计 =====
    suspend fun getStats(): PlayerStatistics
    suspend fun updateStats(stats: PlayerStatistics)
    
    // ===== 设置 =====
    suspend fun getSettings(): GameSettings
    suspend fun updateSettings(settings: GameSettings)
    
    // ===== 日志 =====
    suspend fun addLog(type: String, message: String)
    
    // ===== 教程 =====
    suspend fun getTutorialProgress(): Set<Int>
    suspend fun markTutorialComplete(stepId: Int)
    suspend fun updateTutorialCurrentStep(stepId: Int)
    
    // ===== 初始化 =====
    suspend fun initializeIfNeeded(): Boolean
}
