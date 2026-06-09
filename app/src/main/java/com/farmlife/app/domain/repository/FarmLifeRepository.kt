package com.farmlife.app.domain.repository

import android.content.Context
import com.farmlife.app.config.*
import com.farmlife.app.data.database.FarmLifeDatabase
import com.farmlife.app.data.entity.*
import com.farmlife.app.domain.logic.*

/**
 * 存档仓库 - 负责所有数据的增删改查
 */
class FarmLifeRepository(context: Context) {
    private val db = FarmLifeDatabase.getInstance(context)
    private val playerDao = db.playerDao()
    private val statsDao = db.playerStatisticsDao()
    private val landDao = db.landDao()
    private val cropDao = db.cropDao()
    private val animalDao = db.animalDao()
    private val petDao = db.petDao()
    private val missionDao = db.petMissionDao()
    private val inventoryDao = db.inventoryDao()
    private val factoryDao = db.factoryDao()
    private val queueDao = db.productionQueueDao()
    private val collectionDao = db.collectionDao()
    private val achievementDao = db.achievementDao()
    private val logDao = db.farmLogDao()
    private val settingsDao = db.gameSettingsDao()
    private val treeDao = db.treeDao()
    private val fishDao = db.fishDao()
    private val pondDao = db.pondDao()
    private val petFacilityDao = db.petFacilityDao()
    private val tutorialDao = db.tutorialDao()

    // ===== Player =====
    suspend fun getPlayer(): PlayerEntity {
        return playerDao.getPlayer() ?: PlayerEntity().also { playerDao.insertOrUpdate(it) }
    }

    suspend fun updatePlayer(player: PlayerEntity) = playerDao.update(player)

    // ===== Statistics =====
    suspend fun getStats(): PlayerStatisticsEntity {
        return statsDao.get() ?: PlayerStatisticsEntity().also { statsDao.insertOrUpdate(it) }
    }

    suspend fun updateStats(stats: PlayerStatisticsEntity) = statsDao.update(stats)

    // ===== Land =====
    suspend fun getAllLands(): List<LandInstanceEntity> = landDao.getAll()

    suspend fun insertLand(land: LandInstanceEntity): Long = landDao.insert(land)

    suspend fun insertAllLands(lands: List<LandInstanceEntity>) = landDao.insertAll(lands)

    suspend fun updateLand(land: LandInstanceEntity) = landDao.update(land)

    // ===== Crop =====
    suspend fun getAllCrops(): List<CropInstanceEntity> = cropDao.getAll()

    suspend fun getCropsReady(now: Long): List<CropInstanceEntity> = cropDao.getReadyToHarvest(now)

    suspend fun insertCrop(crop: CropInstanceEntity): Long = cropDao.insert(crop)

    suspend fun updateCrop(crop: CropInstanceEntity) = cropDao.update(crop)

    suspend fun deleteCrop(id: Long) = cropDao.delete(id)

    suspend fun deleteCropByLand(landId: Long) = cropDao.deleteByLandId(landId)

    suspend fun getCropByLand(landId: Long): CropInstanceEntity? = cropDao.getByLandId(landId)

    // ===== Animal =====
    suspend fun getAllAnimals(): List<AnimalInstanceEntity> = animalDao.getAll()

    suspend fun insertAnimal(animal: AnimalInstanceEntity): Long = animalDao.insert(animal)

    suspend fun updateAnimal(animal: AnimalInstanceEntity) = animalDao.update(animal)

    // ===== Pet =====
    suspend fun getAllPets(): List<PetInstanceEntity> = petDao.getAll()

    suspend fun insertPet(pet: PetInstanceEntity): Long = petDao.insert(pet)

    suspend fun updatePet(pet: PetInstanceEntity) = petDao.update(pet)

    // ===== PetMission =====
    suspend fun getAllMissions(): List<PetMissionEntity> = missionDao.getAll()

    suspend fun insertMission(mission: PetMissionEntity): Long = missionDao.insert(mission)

    suspend fun updateMission(mission: PetMissionEntity) = missionDao.update(mission)

    // ===== Inventory =====
    suspend fun getAllInventory(): List<InventoryItemEntity> = inventoryDao.getAll()

    suspend fun insertInventoryItem(item: InventoryItemEntity): Long = inventoryDao.insert(item)

    suspend fun updateInventoryItem(item: InventoryItemEntity) = inventoryDao.update(item)

    suspend fun deleteInventoryItem(id: Long) = inventoryDao.delete(id)

    suspend fun findInventoryItem(type: String, configId: Int, quality: Int): InventoryItemEntity?
        = inventoryDao.findItem(type, configId, quality)

    // ===== Factory =====
    suspend fun getAllFactories(): List<FactoryInstanceEntity> = factoryDao.getAll()

    suspend fun insertFactory(factory: FactoryInstanceEntity): Long = factoryDao.insert(factory)

    suspend fun updateFactory(factory: FactoryInstanceEntity) = factoryDao.update(factory)

    // ===== ProductionQueue =====
    suspend fun getAllQueues(): List<ProductionQueueEntity> = queueDao.getAll()

    suspend fun insertQueue(queue: ProductionQueueEntity): Long = queueDao.insert(queue)

    suspend fun updateQueue(queue: ProductionQueueEntity) = queueDao.update(queue)

    // ===== Collection =====
    suspend fun getAllCollections(): List<CollectionRecordEntity> = collectionDao.getAll()

    suspend fun insertCollection(record: CollectionRecordEntity): Long = collectionDao.insert(record)

    suspend fun updateCollection(record: CollectionRecordEntity) = collectionDao.update(record)

    suspend fun findCollection(type: String, configId: Int): CollectionRecordEntity?
        = collectionDao.findByTypeAndId(type, configId)

    // ===== Achievement =====
    suspend fun getAllAchievements(): List<AchievementEntity> = achievementDao.getAll()

    suspend fun insertAchievement(achievement: AchievementEntity): Long = achievementDao.insert(achievement)

    suspend fun updateAchievement(achievement: AchievementEntity) = achievementDao.update(achievement)

    // ===== FarmLog =====
    suspend fun getRecentLogs(): List<FarmLogEntity> = logDao.getRecent()

    suspend fun addLog(eventType: String, description: String) {
        logDao.insert(FarmLogEntity(eventType = eventType, description = description))
    }

    // ===== Settings =====
    suspend fun getSettings(): GameSettingsEntity {
        return settingsDao.get() ?: GameSettingsEntity().also { settingsDao.insertOrUpdate(it) }
    }

    suspend fun updateSettings(settings: GameSettingsEntity) = settingsDao.update(settings)

    // ===== Tree (果园) =====
    suspend fun getAllTrees(): List<TreeInstanceEntity> = treeDao.getAll()

    suspend fun insertTree(tree: TreeInstanceEntity): Long = treeDao.insert(tree)

    suspend fun updateTree(tree: TreeInstanceEntity) = treeDao.update(tree)

    suspend fun deleteTree(id: Long) = treeDao.delete(id)

    // ===== Fish (鱼塘) =====
    suspend fun getAllFish(): List<FishInstanceEntity> = fishDao.getAll()

    suspend fun insertFish(fish: FishInstanceEntity): Long = fishDao.insert(fish)

    suspend fun updateFish(fish: FishInstanceEntity) = fishDao.update(fish)

    suspend fun deleteFish(id: Long) = fishDao.delete(id)

    // ===== Pond (池塘) =====
    suspend fun getAllPonds(): List<PondInstanceEntity> = pondDao.getAll()

    suspend fun insertPond(pond: PondInstanceEntity): Long = pondDao.insert(pond)

    suspend fun updatePond(pond: PondInstanceEntity) = pondDao.update(pond)

    // ===== Pet Facility (宠物设施) =====
    suspend fun getAllPetFacilities(): List<PetFacilityEntity> = petFacilityDao.getAll()

    suspend fun insertPetFacility(facility: PetFacilityEntity): Long = petFacilityDao.insert(facility)

    suspend fun updatePetFacility(facility: PetFacilityEntity) = petFacilityDao.update(facility)

    // ===== Tutorial (新手引导) =====
    suspend fun getTutorialProgress(): TutorialProgressEntity {
        return tutorialDao.get() ?: TutorialProgressEntity().also { tutorialDao.insertOrUpdate(it) }
    }

    suspend fun updateTutorialProgress(progress: TutorialProgressEntity) = tutorialDao.update(progress)

    suspend fun markTutorialStep(stepId: Int) {
        val progress = getTutorialProgress()
        val newCompleted = TutorialSystem.markStepCompleted(progress.completedSteps, stepId)
        if (newCompleted != progress.completedSteps) {
            tutorialDao.update(progress.copy(
                completedSteps = newCompleted,
                lastShownTime = System.currentTimeMillis()
            ))
        }
    }

    // ===== 初始化数据 =====
    suspend fun initializeIfNeeded(): Boolean {
        val existing = playerDao.getPlayer()
        if (existing != null) return false

        // 新存档初始化
        playerDao.insertOrUpdate(PlayerEntity())
        statsDao.insertOrUpdate(PlayerStatisticsEntity())
        settingsDao.insertOrUpdate(GameSettingsEntity())

        // 初始10x10农田区
        val initialLands = mutableListOf<LandInstanceEntity>()
        for (x in 0 until 10) {
            for (y in 0 until 10) {
                initialLands.add(LandInstanceEntity(x = x, y = y, region = "FARMLAND", unlocked = true))
            }
        }
        landDao.insertAll(initialLands)

        // 初始2个池塘（鱼塘区解锁后可用）
        val initialPonds = listOf(
            PondInstanceEntity(x = 0, y = 0, unlocked = false),
            PondInstanceEntity(x = 2, y = 0, unlocked = false)
        )
        pondDao.insertAll(initialPonds)

        addLog("SYSTEM", "欢迎来到农场！开始你的庄园之旅吧。")
        return true
    }

    // ===== 配置快捷访问 =====
    fun getCropConfig(id: Int) = CropConfigs.getById(id)
    fun getAnimalConfig(id: Int) = AnimalConfigs.getById(id)
    fun getPetConfig(id: Int) = PetConfigs.getById(id)
    fun getFactoryConfig(id: Int) = FactoryConfigs.getFactoryById(id)
    fun getRecipeConfig(id: Int) = FactoryConfigs.getRecipeById(id)
    fun getTreeConfig(id: Int) = TreeConfigs.getById(id)
    fun getFishConfig(id: Int) = FishConfigs.getById(id)
    fun getPetFacilityConfig(id: Int) = PetFacilityConfigs.getById(id)
}
