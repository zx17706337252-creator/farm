package com.farmlife.app.domain.engine

import android.content.Context
import com.farmlife.app.config.*
import com.farmlife.app.data.entity.*
import com.farmlife.app.domain.logic.*
import com.farmlife.app.domain.repository.FarmLifeRepository
import com.farmlife.app.system.TaskScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * FarmEngine - 农场游戏核心引擎
 * 处理所有游戏逻辑：种植、收获、动物生产、宠物工作、加工、订单等
 */
class FarmEngine(private val repository: FarmLifeRepository) {

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + supervisorJob)

    // ===== 游戏状态 Flow =====
    private val _player = MutableStateFlow<PlayerEntity?>(null)
    val player: StateFlow<PlayerEntity?> = _player.asStateFlow()

    private val _lands = MutableStateFlow<List<LandInstanceEntity>>(emptyList())
    val lands: StateFlow<List<LandInstanceEntity>> = _lands.asStateFlow()

    private val _crops = MutableStateFlow<List<CropInstanceEntity>>(emptyList())
    val crops: StateFlow<List<CropInstanceEntity>> = _crops.asStateFlow()

    private val _animals = MutableStateFlow<List<AnimalInstanceEntity>>(emptyList())
    val animals: StateFlow<List<AnimalInstanceEntity>> = _animals.asStateFlow()

    private val _pets = MutableStateFlow<List<PetInstanceEntity>>(emptyList())
    val pets: StateFlow<List<PetInstanceEntity>> = _pets.asStateFlow()

    private val _inventory = MutableStateFlow<List<InventoryItemEntity>>(emptyList())
    val inventory: StateFlow<List<InventoryItemEntity>> = _inventory.asStateFlow()

    private val _factories = MutableStateFlow<List<FactoryInstanceEntity>>(emptyList())
    val factories: StateFlow<List<FactoryInstanceEntity>> = _factories.asStateFlow()

    private val _queues = MutableStateFlow<List<ProductionQueueEntity>>(emptyList())
    val queues: StateFlow<List<ProductionQueueEntity>> = _queues.asStateFlow()

    private val _collections = MutableStateFlow<List<CollectionRecordEntity>>(emptyList())
    val collections: StateFlow<List<CollectionRecordEntity>> = _collections.asStateFlow()

    private val _logs = MutableStateFlow<List<FarmLogEntity>>(emptyList())
    val logs: StateFlow<List<FarmLogEntity>> = _logs.asStateFlow()

    // ===== 新区域状态 =====
    private val _trees = MutableStateFlow<List<TreeInstanceEntity>>(emptyList())
    val trees: StateFlow<List<TreeInstanceEntity>> = _trees.asStateFlow()

    private val _fish = MutableStateFlow<List<FishInstanceEntity>>(emptyList())
    val fish: StateFlow<List<FishInstanceEntity>> = _fish.asStateFlow()

    private val _ponds = MutableStateFlow<List<PondInstanceEntity>>(emptyList())
    val ponds: StateFlow<List<PondInstanceEntity>> = _ponds.asStateFlow()

    private val _petFacilities = MutableStateFlow<List<PetFacilityEntity>>(emptyList())
    val petFacilities: StateFlow<List<PetFacilityEntity>> = _petFacilities.asStateFlow()

    // ===== 天气/季节状态 =====
    private val _season = MutableStateFlow<com.farmlife.app.data.model.Season>(
        com.farmlife.app.data.model.Season.SPRING
    )
    val season: StateFlow<com.farmlife.app.data.model.Season> = _season.asStateFlow()

    private val _weather = MutableStateFlow<com.farmlife.app.data.model.Weather>(
        com.farmlife.app.data.model.Weather.SUNNY
    )
    val weather: StateFlow<com.farmlife.app.data.model.Weather> = _weather.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    private val _tutorialProgress = MutableStateFlow<TutorialProgressEntity?>(null)
    val tutorialProgress: StateFlow<TutorialProgressEntity?> = _tutorialProgress.asStateFlow()

    private val _pendingUnlockDialog = MutableStateFlow<Triple<String, String, String>?>(null)
    val pendingUnlockDialog: StateFlow<Triple<String, String, String>?> = _pendingUnlockDialog.asStateFlow()

    private var playerTotalExp: Long = 0
    private var gameStartTime: Long = System.currentTimeMillis()
    private var previousLevel = 0
    private var toastJob: Job? = null

    // ===== 初始化 =====
    suspend fun initialize() {
        val isNew = repository.initializeIfNeeded()

        if (!isNew) {
            computeOfflineProgress()
        }

        refreshAll()
        updateSeasonWeather()
    }

    private suspend fun refreshAll() {
        val p = repository.getPlayer()
        _player.value = p
        playerTotalExp = LevelingSystem.totalExpForLevel(p.level)

        _lands.value = repository.getAllLands()
        _crops.value = repository.getAllCrops()
        _animals.value = repository.getAllAnimals()
        _pets.value = repository.getAllPets()
        _inventory.value = repository.getAllInventory()
        _factories.value = repository.getAllFactories()
        _queues.value = repository.getAllQueues()
        _collections.value = repository.getAllCollections()
        _logs.value = repository.getRecentLogs()
        _trees.value = repository.getAllTrees()
        _fish.value = repository.getAllFish()
        _ponds.value = repository.getAllPonds()
        _petFacilities.value = repository.getAllPetFacilities()
        _tutorialProgress.value = repository.getTutorialProgress()

        checkLevelUnlock(p.level)
    }

    private fun checkLevelUnlock(newLevel: Int) {
        if (newLevel <= previousLevel) return
        
        when (newLevel) {
            10 -> _pendingUnlockDialog.value = Triple("🐄 牧场解锁！", "可以购买动物进行养殖了！", "ANIMAL")
            12 -> _pendingUnlockDialog.value = Triple("🐕 宠物系统解锁！", "可以购买宠物帮忙干活了！", "PET")
            20 -> _pendingUnlockDialog.value = Triple("🍎 果园解锁！", "可以种植果树，持续产出水果！", "ORCHARD")
            30 -> _pendingUnlockDialog.value = Triple("🏭 工坊解锁！", "可以建造工厂进行加工了！", "FACTORY")
            40 -> _pendingUnlockDialog.value = Triple("🐟 鱼塘解锁！", "可以养鱼获得稀有鱼类！", "FISHPOND")
            50 -> _pendingUnlockDialog.value = Triple("🏠 宠物庄园解锁！", "可以建造宠物设施提升能力！", "PET_ESTATE")
        }
        
        previousLevel = newLevel
    }

    fun dismissUnlockDialog() {
        _pendingUnlockDialog.value = null
    }

    private fun showToast(msg: String) {
        toastJob?.cancel()
        _toast.value = msg
        toastJob = scope.launch {
            delay(2000)
            _toast.value = null
        }
    }

    private fun updateSeasonWeather() {
        _season.value = TimeSystem.currentSeason(gameStartTime)
        _weather.value = TimeSystem.currentWeather()
    }

    // ===== 种植系统（含季节影响）=====
    suspend fun plantCrop(landId: Long, cropId: Int) {
        val cropConfig = repository.getCropConfig(cropId) ?: run {
            showToast("作物不存在")
            return
        }
        val land = _lands.value.firstOrNull { it.landId == landId } ?: run {
            showToast("土地不存在")
            return
        }
        if (_crops.value.any { it.landId == landId }) {
            showToast("土地已种植")
            return
        }
        val p = _player.value ?: return
        if (p.gold < cropConfig.sellPrice * 2L) {
            showToast("金币不足")
            return
        }

        // 应用季节成长加速
        val seasonMul = SeasonWeatherSystem.seasonGrowthMultiplier(_season.value)
        val adjustedGrowTime = (cropConfig.growTimeSeconds / seasonMul).toLong()
        val finishTime = TimeSystem.currentTimeMs() + adjustedGrowTime * 1000

        // 应用天气品质加成
        val weatherBonus = SeasonWeatherSystem.weatherQualityBonus(_weather.value, _season.value)
        val quality = QualitySystem.rollQuality(weatherBonus).ordinal

        val crop = CropInstanceEntity(
            landId = landId,
            cropId = cropId,
            plantTime = TimeSystem.currentTimeMs(),
            finishTime = finishTime,
            quality = quality
        )
        repository.updatePlayer(p.copy(gold = p.gold - cropConfig.sellPrice * 2L))

        repository.insertCrop(crop)

        val stats = repository.getStats()
        repository.updateStats(stats.copy(totalPlant = stats.totalPlant + 1))

        repository.addLog("PLANT", "种植了${cropConfig.icon}${cropConfig.name}")
        refreshAll()
        showToast("种植成功：${cropConfig.name}")
    }

    suspend fun harvestCrop(instanceId: Long) {
        val crop = _crops.value.firstOrNull { it.instanceId == instanceId } ?: return
        if (crop.finishTime > TimeSystem.currentTimeMs()) {
            showToast("作物尚未成熟")
            return
        }

        val cropConfig = repository.getCropConfig(crop.cropId) ?: return
        val land = _lands.value.firstOrNull { it.landId == crop.landId }

        val qualityMultiplier = QualitySystem.qualityMultiplier(crop.quality)
        val landYieldMul = if (land != null) LandLevelingSystem.yieldMultiplier(land.level) else 1.0
        val critMultiplier = LuckSystem.rollCrit(0.02)

        val basePrice = cropConfig.sellPrice
        val baseExp = cropConfig.exp

        val finalPrice = (basePrice * qualityMultiplier * landYieldMul * critMultiplier).toLong()
        val finalExp = (baseExp * qualityMultiplier).toLong()

        val existing = repository.findInventoryItem("CROP", crop.cropId, crop.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.insertInventoryItem(
                InventoryItemEntity(
                    itemType = "CROP",
                    configId = crop.cropId,
                    quantity = (1 * critMultiplier.toInt()).toLong().coerceAtLeast(1),
                    quality = crop.quality
                )
            )
        }

        land?.let {
            val newExp = it.exp + baseExp
            val newLevel = LandLevelingSystem.calculateLevel(newExp)
            repository.updateLand(it.copy(exp = newExp, level = newLevel))
        }

        val p = _player.value ?: return
        val newTotalExp = playerTotalExp + finalExp
        val newLevel = LevelingSystem.calculateLevel(newTotalExp)
        repository.updatePlayer(p.copy(gold = p.gold + finalPrice, level = newLevel))
        playerTotalExp = newTotalExp

        val stats = repository.getStats()
        repository.updateStats(
            stats.copy(
                totalHarvest = stats.totalHarvest + 1,
                totalGoldEarned = stats.totalGoldEarned + finalPrice
            )
        )

        updateCollection("CROP", crop.cropId, crop.quality, cropConfig.name)
        repository.deleteCrop(instanceId)
        repository.addLog("HARVEST", "收获了${cropConfig.icon}${cropConfig.name} 获得 ${finalPrice}金币 ${finalExp}经验")

        if (critMultiplier > 1) showToast("暴击收获！${cropConfig.name} x${critMultiplier.toInt()}")
        refreshAll()
    }

    suspend fun harvestAllReady() {
        val now = TimeSystem.currentTimeMs()
        val ready = _crops.value.filter { it.finishTime <= now }
        if (ready.isEmpty()) {
            showToast("没有已成熟的作物")
            return
        }
        ready.forEach { harvestCrop(it.instanceId) }
        showToast("收获完成！")
    }

    // ===== 仓库系统 =====
    suspend fun sellInventoryItem(itemId: Long, quantity: Long) {
        val item = _inventory.value.firstOrNull { it.itemId == itemId } ?: return
        if (item.quantity < quantity) {
            showToast("数量不足")
            return
        }
        val sellPrice = when (item.itemType) {
            "CROP" -> repository.getCropConfig(item.configId)?.sellPrice ?: 0
            "ANIMAL" -> repository.getAnimalConfig(item.configId)?.productSellPrice ?: 0
            "PROCESSED" -> FactoryConfigs.RECIPES.firstOrNull { it.recipeId == item.configId }?.outputSellPrice ?: 0
            "TREE" -> repository.getTreeConfig(item.configId)?.sellPrice ?: 0
            "FISH" -> repository.getFishConfig(item.configId)?.sellPrice ?: 0
            else -> 0
        }
        val qualityMul = QualitySystem.qualityMultiplier(item.quality)
        val total = (sellPrice * quantity * qualityMul).toLong()

        if (item.quantity == quantity) {
            repository.deleteInventoryItem(itemId)
        } else {
            repository.updateInventoryItem(item.copy(quantity = item.quantity - quantity))
        }

        val p = _player.value ?: return
        repository.updatePlayer(p.copy(gold = p.gold + total))
        repository.addLog("SELL", "出售物品获得 $total 金币")
        refreshAll()
        showToast("出售成功，获得 $total 金币")
    }

    suspend fun sellAllInventory() {
        var total = 0L
        _inventory.value.forEach { item ->
            val sellPrice = when (item.itemType) {
                "CROP" -> repository.getCropConfig(item.configId)?.sellPrice ?: 0
                "ANIMAL" -> repository.getAnimalConfig(item.configId)?.productSellPrice ?: 0
                "PROCESSED" -> FactoryConfigs.RECIPES.firstOrNull { it.recipeId == item.configId }?.outputSellPrice ?: 0
                "TREE" -> repository.getTreeConfig(item.configId)?.sellPrice ?: 0
                "FISH" -> repository.getFishConfig(item.configId)?.sellPrice ?: 0
                else -> 0
            }
            total += (sellPrice * item.quantity * QualitySystem.qualityMultiplier(item.quality)).toLong()
        }

        _inventory.value.forEach { repository.deleteInventoryItem(it.itemId) }

        val p = _player.value ?: return
        repository.updatePlayer(p.copy(gold = p.gold + total))
        refreshAll()
        showToast("全部出售，获得 $total 金币")
    }

    // ===== 动物系统（含季节/天气影响）=====
    suspend fun buyAnimal(animalId: Int) {
        val cfg = repository.getAnimalConfig(animalId) ?: run { showToast("动物不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.purchasePrice) { showToast("金币不足"); return }

        val quality = QualitySystem.rollQuality().ordinal
        val animal = AnimalInstanceEntity(
            animalId = animalId,
            level = 1,
            exp = 0,
            friendship = 10,
            quality = quality,
            lastProduceTime = TimeSystem.currentTimeMs(),
            region = "LIVESTOCK"
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.purchasePrice))
        repository.insertAnimal(animal)
        repository.addLog("BUY", "购买了${cfg.icon}${cfg.name}")
        updateCollection("ANIMAL", animalId, animal.quality, cfg.name)
        refreshAll()
        showToast("购买了 ${cfg.name}")
    }

    suspend fun collectAnimalProduct(instanceId: Long) {
        val animal = _animals.value.firstOrNull { it.instanceId == instanceId } ?: return
        val cfg = repository.getAnimalConfig(animal.animalId) ?: return

        // 应用季节/天气效率影响
        val efficiency = SeasonWeatherSystem.calculateAnimalEfficiency(
            _season.value, _weather.value,
            AnimalLevelingSystem.efficiencyMultiplier(animal.level)
        )
        val productTime = (cfg.productTimeSeconds * 1000L / efficiency).toLong()
        val now = TimeSystem.currentTimeMs()
        if (now - animal.lastProduceTime < productTime) {
            showToast("尚未生产完成")
            return
        }

        val quality = QualitySystem.rollQuality()
        val qualityMul = quality.multiplier

        val existing = repository.findInventoryItem("ANIMAL", animal.animalId, quality.ordinal)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.insertInventoryItem(
                InventoryItemEntity(
                    itemType = "ANIMAL",
                    configId = animal.animalId,
                    quantity = 1,
                    quality = quality.ordinal
                )
            )
        }

        val expGained = cfg.exp
        val newExp = animal.exp + expGained
        val newLevel = AnimalLevelingSystem.calculateLevel(newExp)
        val newFriendship = (animal.friendship + 2).coerceAtMost(100)

        repository.updateAnimal(
            animal.copy(
                level = newLevel,
                exp = newExp,
                friendship = newFriendship,
                lastProduceTime = now
            )
        )

        val p = _player.value ?: return
        val gainGold = (cfg.productSellPrice * qualityMul).toLong()
        repository.updatePlayer(p.copy(gold = p.gold + gainGold))

        val stats = repository.getStats()
        repository.updateStats(
            stats.copy(
                totalAnimalProduct = stats.totalAnimalProduct + 1,
                totalGoldEarned = stats.totalGoldEarned + gainGold
            )
        )

        repository.addLog("COLLECT", "收获了${cfg.productName}，获得 ${gainGold}金币")
        refreshAll()
    }

    // ===== 宠物系统 =====
    suspend fun buyPet(petId: Int) {
        val cfg = repository.getPetConfig(petId) ?: run { showToast("宠物不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.purchasePrice) { showToast("金币不足"); return }

        val pet = PetInstanceEntity(
            petId = petId,
            level = 1,
            exp = 0,
            friendship = 10,
            quality = QualitySystem.rollQuality().ordinal,
            workSlots = 1,
            isActive = true,
            region = "PET_ESTATE"
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.purchasePrice))
        repository.insertPet(pet)
        repository.addLog("PET", "获得新宠物：${cfg.icon}${cfg.name}")
        updateCollection("PET", petId, pet.quality, cfg.name)
        val stats = repository.getStats()
        repository.updateStats(stats.copy(totalPetObtained = stats.totalPetObtained + 1))
        refreshAll()
        showToast("获得宠物 ${cfg.name}！")
    }

    suspend fun petAutoWork() {
        val activePets = _pets.value.filter { it.isActive }
        if (activePets.isEmpty()) return

        var worked = false
        activePets.forEach { pet ->
            val cfg = repository.getPetConfig(pet.petId) ?: return@forEach
            when (cfg.primaryWorkType) {
                "HARVEST" -> {
                    val now = TimeSystem.currentTimeMs()
                    val ready = _crops.value.filter { it.finishTime <= now }
                    if (ready.isNotEmpty()) {
                        harvestCrop(ready.first().instanceId)
                        worked = true
                    }
                }
                "COLLECT" -> {
                    val now = TimeSystem.currentTimeMs()
                    val readyAnimal = _animals.value.firstOrNull { beast ->
                        val acfg = repository.getAnimalConfig(beast.animalId) ?: return@firstOrNull false
                        val efficiency = SeasonWeatherSystem.calculateAnimalEfficiency(
                            _season.value, _weather.value,
                            AnimalLevelingSystem.efficiencyMultiplier(beast.level)
                        )
                        val productTime = (acfg.productTimeSeconds * 1000L / efficiency).toLong()
                        now - beast.lastProduceTime >= productTime
                    }
                    if (readyAnimal != null) {
                        collectAnimalProduct(readyAnimal.instanceId)
                        worked = true
                    }
                }
            }

            val petExp = pet.exp + 5
            val petLevel = PetLevelingSystem.calculateLevel(petExp)
            val slots = PetLevelingSystem.workSlots(petLevel)
            repository.updatePet(pet.copy(exp = petExp, level = petLevel, workSlots = slots))
        }

        if (worked) {
            refreshAll()
        }
    }

    // ===== 宠物探险完成奖励（供 WorkManager 调用）=====
    suspend fun completePetExploreReward(petInstanceId: Long, gold: Long) {
        val player = _player.value ?: return
        repository.updatePlayer(player.copy(gold = player.gold + gold))
        val pet = _pets.value.firstOrNull { it.instanceId == petInstanceId }
        pet?.let {
            repository.updatePet(it.copy(currentMission = null))
        }
        refreshAll()
    }

    suspend fun dispatchPetExplore(context: Context, petInstanceId: Long, minutes: Int) {
        val pet = _pets.value.firstOrNull { it.instanceId == petInstanceId } ?: return

        val mission = PetMissionEntity(
            petInstanceId = petInstanceId,
            missionType = "EXPLORE",
            startTime = TimeSystem.currentTimeMs(),
            finishTime = TimeSystem.currentTimeMs() + minutes * 60L * 1000L
        )
        repository.insertMission(mission)
        repository.updatePet(pet.copy(currentMission = "探险中"))

        // 使用 WorkManager 调度通知
        TaskScheduler.schedulePetExplore(context, petInstanceId, minutes)

        refreshAll()
        showToast("宠物出发探险，${minutes}分钟后归来")
    }

    suspend fun collectExploreReward(missionId: Long) {
        val mission = repository.getAllMissions().firstOrNull { it.missionId == missionId } ?: return
        val reward = (Math.random() * 2000 + 500).toLong()
        val p = _player.value ?: return
        repository.updatePlayer(p.copy(gold = p.gold + reward))

        repository.updateMission(mission.copy(isCompleted = true))
        refreshAll()
        showToast("探险归来，获得 $reward 金币")
    }

    // ===== 加工系统 =====
    suspend fun buyFactory(factoryId: Int) {
        val cfg = repository.getFactoryConfig(factoryId) ?: run { showToast("建筑不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.purchasePrice) { showToast("金币不足"); return }

        val factory = FactoryInstanceEntity(
            factoryType = cfg.factoryId.toString(),
            level = 1,
            exp = 0,
            queueSize = 1,
            region = "PROCESSING"
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.purchasePrice))
        repository.insertFactory(factory)
        repository.addLog("BUILD", "建造了${cfg.icon}${cfg.name}")
        refreshAll()
        showToast("建造了 ${cfg.name}")
    }

    suspend fun startProduction(factoryId: Long, recipeId: Int) {
        val recipe = repository.getRecipeConfig(recipeId) ?: run { showToast("配方不存在"); return }
        val factory = _factories.value.firstOrNull { it.factoryId == factoryId } ?: run {
            showToast("工厂不存在"); return
        }

        val inputItem = repository.findInventoryItem(recipe.inputItemType, recipe.inputConfigId, 0)
        if (inputItem == null || inputItem.quantity < recipe.inputQuantity) {
            showToast("原料不足"); return
        }

        if (inputItem.quantity == recipe.inputQuantity.toLong()) {
            repository.deleteInventoryItem(inputItem.itemId)
        } else {
            repository.updateInventoryItem(inputItem.copy(quantity = inputItem.quantity - recipe.inputQuantity))
        }

        val finishTime = TimeSystem.calculateProcessFinishTime(recipe.processTimeSeconds, factory.level)
        val queue = ProductionQueueEntity(
            factoryId = factoryId,
            recipeId = recipeId,
            startTime = TimeSystem.currentTimeMs(),
            finishTime = finishTime
        )
        repository.insertQueue(queue)

        repository.addLog("PROCESS", "开始生产：${recipe.name}")
        refreshAll()
        showToast("开始生产 ${recipe.name}")
    }

    suspend fun collectProduction(queueId: Long) {
        val q = _queues.value.firstOrNull { it.queueId == queueId } ?: return
        if (q.finishTime > TimeSystem.currentTimeMs()) {
            showToast("尚未完成"); return
        }

        val recipe = repository.getRecipeConfig(q.recipeId) ?: return
        val quality = QualitySystem.rollQuality()
        val qualityMul = quality.multiplier

        val existing = repository.findInventoryItem("PROCESSED", q.recipeId, quality.ordinal)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.insertInventoryItem(
                InventoryItemEntity(
                    itemType = "PROCESSED",
                    configId = q.recipeId,
                    quantity = 1,
                    quality = quality.ordinal
                )
            )
        }

        val factory = _factories.value.firstOrNull { it.factoryId == q.factoryId }
        factory?.let {
            val newExp = it.exp + recipe.outputExp
            val newLevel = BuildingLevelingSystem.calculateLevel(newExp)
            val newQueue = BuildingLevelingSystem.queueSize(newLevel)
            repository.updateFactory(it.copy(level = newLevel, exp = newExp, queueSize = newQueue))
        }

        val p = _player.value ?: return
        val gainGold = (recipe.outputSellPrice * qualityMul * 0.5).toLong()
        repository.updatePlayer(p.copy(gold = p.gold + gainGold))

        repository.updateQueue(q.copy(isCollected = true))
        repository.addLog("PROCESS", "生产完成：${recipe.name}")
        refreshAll()
        showToast("生产完成：${recipe.name}")
    }

    // ===== 收藏系统 =====
    private suspend fun updateCollection(type: String, configId: Int, qualityIndex: Int, name: String) {
        val existing = repository.findCollection(type, configId)
        if (existing != null) {
            repository.updateCollection(
                existing.copy(
                    highestQuality = maxOf(existing.highestQuality, qualityIndex),
                    totalObtained = existing.totalObtained + 1
                )
            )
        } else {
            repository.insertCollection(
                CollectionRecordEntity(
                    collectionType = type,
                    configId = configId,
                    highestQuality = qualityIndex,
                    totalObtained = 1
                )
            )
            repository.addLog("COLLECTION", "解锁新收藏：$name")
        }

        val all = repository.getAllCollections()
        val score = all.sumOf { record ->
            val baseScore = CollectionScoreSystem.scoreForQuality(record.highestQuality)
            baseScore * record.totalObtained
        }
        val p = _player.value ?: return
        val colLevel = CollectionScoreSystem.collectionLevel(score)
        repository.updatePlayer(p.copy(collectionScore = score, level = maxOf(p.level, colLevel / 3 + p.level)))
    }

    // ===== 离线收益 =====
    private suspend fun computeOfflineProgress() {
        val p = repository.getPlayer() ?: return
        val lastLogin = p.lastLoginTime
        val now = TimeSystem.currentTimeMs()

        val crops = repository.getAllCrops()
        val readyCount = crops.count { it.finishTime <= now && !it.harvested }
        if (readyCount > 0) {
            repository.addLog("OFFLINE", "离线期间有 $readyCount 个作物已成熟")
        }

        repository.updatePlayer(p.copy(lastLoginTime = now))

        val offlineSecs = (now - lastLogin) / 1000
        if (offlineSecs > 60) {
            val mins = offlineSecs / 60
            repository.addLog("SYSTEM", "欢迎回来！离线 $mins 分钟")
        }
    }

    // ===== 扩地 =====
    suspend fun expandFarmland() {
        val p = _player.value ?: return
        val cost = 5000L * (_lands.value.size / 100 + 1)
        if (p.gold < cost) {
            showToast("金币不足，需要 $cost"); return
        }
        val currentMaxX = (_lands.value.maxOfOrNull { it.x } ?: 0)
        val startX = currentMaxX + 1
        val newLands = mutableListOf<LandInstanceEntity>()
        for (x in startX until startX + 5) {
            for (y in 0 until 10) {
                newLands.add(LandInstanceEntity(x = x, y = y, region = "FARMLAND", unlocked = true))
            }
        }
        repository.updatePlayer(p.copy(gold = p.gold - cost))
        repository.insertAllLands(newLands)
        repository.addLog("EXPAND", "扩充了 ${newLands.size} 块土地")
        refreshAll()
        showToast("农场扩建完成！")
    }

    // ===== 果园系统 =====
    suspend fun plantTree(x: Int, y: Int, treeId: Int) {
        val cfg = repository.getTreeConfig(treeId) ?: run { showToast("树种不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.sellPrice) { showToast("金币不足"); return }

        val now = TimeSystem.currentTimeMs()
        val tree = TreeInstanceEntity(
            configId = treeId,
            x = x,
            y = y,
            plantedTime = now,
            nextHarvestTime = now + cfg.growTimeHours * 60L * 60L * 1000L,
            level = 1,
            exp = 0
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.sellPrice))
        repository.insertTree(tree)
        repository.addLog("PLANT", "种植了${cfg.icon}${cfg.name}")
        refreshAll()
        showToast("种植了 ${cfg.name}")
    }

    suspend fun harvestTree(treeId: Long) {
        val tree = _trees.value.firstOrNull { it.treeId == treeId } ?: return
        val cfg = repository.getTreeConfig(tree.configId) ?: return
        val now = TimeSystem.currentTimeMs()

        if (now < tree.nextHarvestTime) {
            showToast("还未到收获时间"); return
        }

        val quality = QualitySystem.rollQuality(SeasonWeatherSystem.weatherQualityBonus(_weather.value, _season.value))

        val existing = repository.findInventoryItem("TREE", tree.configId, quality.ordinal)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.insertInventoryItem(
                InventoryItemEntity(
                    itemType = "TREE",
                    configId = tree.configId,
                    quantity = 1,
                    quality = quality.ordinal
                )
            )
        }

        val newExp = tree.exp + cfg.exp
        val newLevel = BuildingLevelingSystem.calculateLevel(newExp)
        repository.updateTree(
            tree.copy(
                exp = newExp,
                level = newLevel,
                nextHarvestTime = now + cfg.harvestTimeHours * 60L * 60L * 1000L
            )
        )

        val p = _player.value ?: return
        val gainGold = (cfg.sellPrice * quality.multiplier).toLong()
        repository.updatePlayer(p.copy(gold = p.gold + gainGold))

        refreshAll()
        showToast("收获了 ${cfg.name}，获得 $gainGold 金币")
    }

    // ===== 鱼塘系统 =====
    suspend fun unlockPond(pondId: Long) {
        val p = _player.value ?: return
        if (p.level < 40) { showToast("需要等级 40"); return }
        if (p.gold < 10000) { showToast("金币不足，需要 10000"); return }

        val pond = _ponds.value.firstOrNull { it.pondId == pondId } ?: return
        repository.updatePlayer(p.copy(gold = p.gold - 10000))
        repository.updatePond(pond.copy(unlocked = true))
        repository.addLog("UNLOCK", "解锁了鱼塘")
        refreshAll()
        showToast("鱼塘解锁成功！")
    }

    suspend fun addFish(pondId: Long, fishId: Int) {
        val cfg = repository.getFishConfig(fishId) ?: run { showToast("鱼类不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.sellPrice) { showToast("金币不足"); return }

        val pond = _ponds.value.firstOrNull { it.pondId == pondId && it.unlocked } ?: run {
            showToast("池塘未解锁"); return
        }

        val fish = FishInstanceEntity(
            configId = fishId,
            pondId = pondId,
            placedTime = TimeSystem.currentTimeMs(),
            finishTime = TimeSystem.currentTimeMs() + cfg.growTimeMinutes * 60L * 1000L,
            quality = QualitySystem.rollQuality().ordinal
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.sellPrice))
        repository.insertFish(fish)
        refreshAll()
        showToast("放入了 ${cfg.name}")
    }

    suspend fun collectFish(fishId: Long) {
        val fish = _fish.value.firstOrNull { it.fishId == fishId } ?: return
        val cfg = repository.getFishConfig(fish.configId) ?: return
        val now = TimeSystem.currentTimeMs()

        if (now < fish.finishTime) {
            showToast("还未成熟"); return
        }

        val existing = repository.findInventoryItem("FISH", fish.configId, fish.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.insertInventoryItem(
                InventoryItemEntity(
                    itemType = "FISH",
                    configId = fish.configId,
                    quantity = 1,
                    quality = fish.quality
                )
            )
        }

        val p = _player.value ?: return
        val qualityMul = QualitySystem.qualityMultiplier(fish.quality)
        val gainGold = (cfg.sellPrice * qualityMul).toLong()
        repository.updatePlayer(p.copy(gold = p.gold + gainGold))

        repository.deleteFish(fishId)
        refreshAll()
        showToast("钓到了 ${cfg.name}，获得 $gainGold 金币")
    }

    // ===== 宠物设施系统 =====
    suspend fun buildPetFacility(facilityId: Int) {
        val cfg = repository.getPetFacilityConfig(facilityId) ?: run { showToast("设施不存在"); return }
        val p = _player.value ?: return
        if (p.level < cfg.unlockLevel) { showToast("需要等级 ${cfg.unlockLevel}"); return }
        if (p.gold < cfg.buildCost) { showToast("金币不足"); return }

        val facility = PetFacilityEntity(
            configId = facilityId,
            level = 1,
            isActive = true
        )
        repository.updatePlayer(p.copy(gold = p.gold - cfg.buildCost))
        repository.insertPetFacility(facility)
        repository.addLog("BUILD", "建造了${cfg.icon}${cfg.name}")
        refreshAll()
        showToast("建造了 ${cfg.name}")
    }

    // ===== 访问配置 =====
    fun allCropConfigs() = CropConfigs.ALL
    fun allAnimalConfigs() = AnimalConfigs.ALL
    fun allPetConfigs() = PetConfigs.ALL
    fun allFactoryConfigs() = FactoryConfigs.ALL
    fun allRecipes() = FactoryConfigs.RECIPES
    fun allRecipesForFactory(factoryId: Int) = FactoryConfigs.getRecipesByFactory(factoryId)
    fun allTreeConfigs() = TreeConfigs.ALL
    fun allFishConfigs() = FishConfigs.ALL
    fun allPetFacilityConfigs() = PetFacilityConfigs.ALL

    fun getSeasonDescription() = SeasonWeatherSystem.getSeasonDescription(_season.value)
    fun getWeatherDescription() = SeasonWeatherSystem.getWeatherDescription(_weather.value)

    // ===== 新手引导 =====
    fun getNextTutorialStep(): TutorialSystem.Step? {
        val progress = _tutorialProgress.value ?: return TutorialSystem.Step.WELCOME
        val playerLevel = _player.value?.level ?: 0
        return TutorialSystem.getNextStep(progress.currentStep, playerLevel)
    }

    suspend fun markTutorialStepComplete(stepId: Int) {
        repository.markTutorialStep(stepId)
        _tutorialProgress.value = repository.getTutorialProgress()
    }

    suspend fun updateTutorialCurrentStep(stepId: Int) {
        val progress = _tutorialProgress.value ?: TutorialProgressEntity()
        repository.updateTutorialProgress(progress.copy(currentStep = stepId))
        _tutorialProgress.value = repository.getTutorialProgress()
    }

    fun isStepCompleted(stepId: Int): Boolean {
        val progress = _tutorialProgress.value ?: return false
        return TutorialSystem.isStepCompleted(progress.completedSteps, stepId)
    }
}
