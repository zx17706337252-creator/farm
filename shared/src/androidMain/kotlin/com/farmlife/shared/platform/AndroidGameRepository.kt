package com.farmlife.shared.platform

import android.content.Context
import com.farmlife.shared.domain.repository.GameRepository
import com.farmlife.shared.logic.TimeSystem
import com.farmlife.shared.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android 平台的 GameRepository 实现
 * 使用 SQLite 数据库存储游戏数据
 */
class AndroidGameRepository(private val context: Context) : GameRepository {
    
    private val dbHelper = GameDatabaseHelper(context)
    
    override suspend fun getPlayer(): Player? = withContext(Dispatchers.IO) {
        dbHelper.getPlayer()
    }
    
    override suspend fun updatePlayer(player: Player) = withContext(Dispatchers.IO) {
        dbHelper.updatePlayer(player)
    }
    
    override suspend fun initializePlayer() = withContext(Dispatchers.IO) {
        if (dbHelper.getPlayer() == null) {
            dbHelper.insertPlayer(Player(gold = 500, level = 1, exp = 0, collectionScore = 0))
            // 初始化9块土地
            for (i in 0 until 9) {
                dbHelper.insertLand(LandInstance(
                    landId = System.currentTimeMillis() + i,
                    region = "FARMLAND",
                    index = i,
                    unlocked = i < 3  // 前3块默认解锁
                ))
            }
            // 初始化鱼塘
            for (i in 0 until 4) {
                dbHelper.insertPond(PondInstance(
                    pondId = i.toLong(),
                    unlocked = false,
                    region = "FISHPOND"
                ))
            }
        }
    }
    
    override suspend fun initializeIfNeeded(): Boolean = withContext(Dispatchers.IO) {
        if (dbHelper.getPlayer() == null) {
            initializePlayer()
            true
        } else {
            false
        }
    }
    
    // ===== 土地 =====
    override suspend fun getAllLands(): List<LandInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllLands()
    }
    
    override suspend fun getLandById(landId: Long): LandInstance? = withContext(Dispatchers.IO) {
        dbHelper.getLandById(landId)
    }
    
    override suspend fun insertLand(land: LandInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertLand(land)
    }
    
    override suspend fun updateLand(land: LandInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateLand(land)
    }
    
    override suspend fun getUnlockedLandsCount(): Int = withContext(Dispatchers.IO) {
        dbHelper.getAllLands().count { it.unlocked }
    }
    
    // ===== 作物 =====
    override suspend fun getAllCrops(): List<CropInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllCrops()
    }
    
    override suspend fun getCropByLand(landId: Long): CropInstance? = withContext(Dispatchers.IO) {
        dbHelper.getCropByLand(landId)
    }
    
    override suspend fun insertCrop(crop: CropInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertCrop(crop)
    }
    
    override suspend fun updateCrop(crop: CropInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateCrop(crop)
    }
    
    override suspend fun deleteCrop(instanceId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteCrop(instanceId)
    }
    
    // ===== 动物 =====
    override suspend fun getAllAnimals(): List<AnimalInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllAnimals()
    }
    
    override suspend fun insertAnimal(animal: AnimalInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertAnimal(animal)
    }
    
    override suspend fun updateAnimal(animal: AnimalInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateAnimal(animal)
    }
    
    override suspend fun deleteAnimal(instanceId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteAnimal(instanceId)
    }
    
    // ===== 宠物 =====
    override suspend fun getAllPets(): List<PetInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllPets()
    }
    
    override suspend fun insertPet(pet: PetInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertPet(pet)
    }
    
    override suspend fun updatePet(pet: PetInstance) = withContext(Dispatchers.IO) {
        dbHelper.updatePet(pet)
    }
    
    override suspend fun deletePet(instanceId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deletePet(instanceId)
    }
    
    // ===== 库存 =====
    override suspend fun getInventory(): List<InventoryItem> = withContext(Dispatchers.IO) {
        dbHelper.getInventory()
    }
    
    override suspend fun addInventoryItem(item: InventoryItem) = withContext(Dispatchers.IO) {
        dbHelper.addInventoryItem(item)
    }
    
    override suspend fun updateInventoryItem(item: InventoryItem) = withContext(Dispatchers.IO) {
        dbHelper.updateInventoryItem(item)
    }
    
    override suspend fun deleteInventoryItem(itemId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteInventoryItem(itemId)
    }
    
    override suspend fun findInventoryItem(type: String, configId: Int, quality: Int): InventoryItem? = withContext(Dispatchers.IO) {
        dbHelper.findInventoryItem(type, configId, quality)
    }
    
    // ===== 果树 =====
    override suspend fun getAllTrees(): List<TreeInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllTrees()
    }
    
    override suspend fun insertTree(tree: TreeInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertTree(tree)
    }
    
    override suspend fun updateTree(tree: TreeInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateTree(tree)
    }
    
    // ===== 鱼塘 =====
    override suspend fun getAllPonds(): List<PondInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllPonds()
    }
    
    override suspend fun getPondById(pondId: Long): PondInstance? = withContext(Dispatchers.IO) {
        dbHelper.getPondById(pondId)
    }
    
    override suspend fun updatePond(pond: PondInstance) = withContext(Dispatchers.IO) {
        dbHelper.updatePond(pond)
    }
    
    override suspend fun insertPond(pond: PondInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertPond(pond)
    }
    
    // ===== 鱼类 =====
    override suspend fun getAllFish(): List<FishInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllFish()
    }
    
    override suspend fun insertFish(fish: FishInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertFish(fish)
    }
    
    override suspend fun updateFish(fish: FishInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateFish(fish)
    }
    
    override suspend fun deleteFish(fishId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteFish(fishId)
    }
    
    // ===== 工厂 =====
    override suspend fun getAllFactories(): List<FactoryInstance> = withContext(Dispatchers.IO) {
        dbHelper.getAllFactories()
    }
    
    override suspend fun insertFactory(factory: FactoryInstance) = withContext(Dispatchers.IO) {
        dbHelper.insertFactory(factory)
    }
    
    override suspend fun updateFactory(factory: FactoryInstance) = withContext(Dispatchers.IO) {
        dbHelper.updateFactory(factory)
    }
    
    // ===== 图鉴 =====
    override suspend fun getAllCollections(): List<CollectionRecord> = withContext(Dispatchers.IO) {
        dbHelper.getAllCollections()
    }
    
    override suspend fun findCollection(type: String, configId: Int): CollectionRecord? = withContext(Dispatchers.IO) {
        dbHelper.findCollection(type, configId)
    }
    
    override suspend fun insertCollection(record: CollectionRecord) = withContext(Dispatchers.IO) {
        dbHelper.insertCollection(record)
    }
    
    override suspend fun updateCollection(record: CollectionRecord) = withContext(Dispatchers.IO) {
        dbHelper.updateCollection(record)
    }
    
    // ===== 统计 =====
    override suspend fun getStats(): PlayerStatistics = withContext(Dispatchers.IO) {
        dbHelper.getStats() ?: PlayerStatistics()
    }
    
    override suspend fun updateStats(stats: PlayerStatistics) = withContext(Dispatchers.IO) {
        dbHelper.updateStats(stats)
    }
    
    // ===== 设置 =====
    override suspend fun getSettings(): GameSettings = withContext(Dispatchers.IO) {
        dbHelper.getSettings() ?: GameSettings()
    }
    
    override suspend fun updateSettings(settings: GameSettings) = withContext(Dispatchers.IO) {
        dbHelper.updateSettings(settings)
    }
    
    // ===== 日志 =====
    override suspend fun addLog(type: String, message: String) = withContext(Dispatchers.IO) {
        dbHelper.addLog(type, message)
    }
    
    // ===== 教程 =====
    override suspend fun getTutorialProgress(): Set<Int> = withContext(Dispatchers.IO) {
        dbHelper.getTutorialProgress().toSet()
    }
    
    override suspend fun markTutorialComplete(stepId: Int) = withContext(Dispatchers.IO) {
        dbHelper.markTutorialComplete(stepId)
    }
    
    override suspend fun updateTutorialCurrentStep(stepId: Int) = withContext(Dispatchers.IO) {
        dbHelper.updateTutorialCurrentStep(stepId)
    }
}
