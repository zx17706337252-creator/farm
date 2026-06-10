package com.farmlife.shared.domain.engine

import com.farmlife.shared.config.*
import com.farmlife.shared.domain.repository.GameRepository
import com.farmlife.shared.logic.*
import com.farmlife.shared.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 农场游戏核心引擎
 * 处理所有游戏逻辑 - 种植、收获、动物、宠物、加工、图鉴等
 * 
 * 这是跨平台共享的核心模块，iOS和Android共用同一套业务逻辑
 */
class FarmEngine(private val repository: GameRepository) {
    
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var toastJob: Job? = null
    
    // ===== 游戏状态 Flows =====
    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player.asStateFlow()
    
    private val _lands = MutableStateFlow<List<LandInstance>>(emptyList())
    val lands: StateFlow<List<LandInstance>> = _lands.asStateFlow()
    
    private val _crops = MutableStateFlow<List<CropInstance>>(emptyList())
    val crops: StateFlow<List<CropInstance>> = _crops.asStateFlow()
    
    private val _animals = MutableStateFlow<List<AnimalInstance>>(emptyList())
    val animals: StateFlow<List<AnimalInstance>> = _animals.asStateFlow()
    
    private val _pets = MutableStateFlow<List<PetInstance>>(emptyList())
    val pets: StateFlow<List<PetInstance>> = _pets.asStateFlow()
    
    private val _inventory = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventory: StateFlow<List<InventoryItem>> = _inventory.asStateFlow()
    
    private val _trees = MutableStateFlow<List<TreeInstance>>(emptyList())
    val trees: StateFlow<List<TreeInstance>> = _trees.asStateFlow()
    
    private val _ponds = MutableStateFlow<List<PondInstance>>(emptyList())
    val ponds: StateFlow<List<PondInstance>> = _ponds.asStateFlow()
    
    private val _fish = MutableStateFlow<List<FishInstance>>(emptyList())
    val fish: StateFlow<List<FishInstance>> = _fish.asStateFlow()
    
    private val _factories = MutableStateFlow<List<FactoryInstance>>(emptyList())
    val factories: StateFlow<List<FactoryInstance>> = _factories.asStateFlow()
    
    private val _collections = MutableStateFlow<List<CollectionRecord>>(emptyList())
    val collections: StateFlow<List<CollectionRecord>> = _collections.asStateFlow()
    
    private val _season = MutableStateFlow(Season.SPRING)
    val season: StateFlow<Season> = _season.asStateFlow()
    
    private val _weather = MutableStateFlow(Weather.SUNNY)
    val weather: StateFlow<Weather> = _weather.asStateFlow()
    
    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()
    
    // ===== 初始化 =====
    suspend fun initialize() {
        val isNew = repository.initializeIfNeeded()
        refreshAll()
        
        // 计算当前季节和天气
        _season.value = SeasonWeatherSystem.calculateSeason()
        _weather.value = SeasonWeatherSystem.generateWeather()
        
        // 如果是新游戏，显示欢迎toast
        if (isNew) {
            showToast("🎉 欢迎来到呆柳的农场！")
        }
    }
    
    private suspend fun refreshAll() {
        withContext(Dispatchers.IO) {
            _player.value = repository.getPlayer()
            _lands.value = repository.getAllLands()
            _crops.value = repository.getAllCrops()
            _animals.value = repository.getAllAnimals()
            _pets.value = repository.getAllPets()
            _inventory.value = repository.getInventory()
            _trees.value = repository.getAllTrees()
            _ponds.value = repository.getAllPonds()
            _fish.value = repository.getAllFish()
            _factories.value = repository.getAllFactories()
            _collections.value = repository.getAllCollections()
        }
    }
    
    // ===== Toast 提示 =====
    private fun showToast(msg: String) {
        toastJob?.cancel()
        _toast.value = msg
        toastJob = scope.launch {
            delay(2000)
            _toast.value = null
        }
    }
    
    // ===== 种植系统 =====
    suspend fun plantCrop(landId: Long, cropId: Int) {
        val cropConfig = CropConfigs.getById(cropId) ?: run {
            showToast("作物不存在"); return
        }
        
        val land = _lands.value.find { it.landId == landId } ?: run {
            showToast("土地不存在"); return
        }
        
        if (_crops.value.any { it.landId == landId }) {
            showToast("土地已种植作物"); return
        }
        
        val p = _player.value ?: return
        val cost = cropConfig.sellPrice * 2L
        if (p.gold < cost) {
            showToast("金币不足，需要 $cost 金"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - cost)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        // 计算生长时间（考虑季节和天气）
        val seasonMul = SeasonWeatherSystem.seasonGrowthMultiplier(_season.value)
        val adjustedGrowTime = (cropConfig.growTimeSeconds / seasonMul).toLong()
        val weatherBonus = SeasonWeatherSystem.weatherQualityBonus(_weather.value)
        val quality = QualitySystem.rollQuality(weatherBonus)
        
        val crop = CropInstance(
            landId = landId,
            cropId = cropId,
            plantTime = TimeSystem.currentTimeMs(),
            finishTime = TimeSystem.currentTimeMs() + adjustedGrowTime * 1000,
            quality = quality.intValue
        )
        
        repository.insertCrop(crop)
        _crops.value = repository.getAllCrops()
        
        // 更新图鉴
        updateCollection(ItemCategories.CROP, cropId, quality.intValue, cropConfig.name)
        
        // 更新统计
        val stats = repository.getStats()
        repository.updateStats(stats.copy(totalPlant = stats.totalPlant + 1))
        
        showToast("${cropConfig.icon} 种植成功：${cropConfig.name}")
    }
    
    suspend fun harvestCrop(instanceId: Long) {
        val crop = _crops.value.find { it.instanceId == instanceId } ?: run {
            showToast("作物不存在"); return
        }
        
        if (!TimeSystem.isReady(crop.finishTime)) {
            showToast("作物还未成熟"); return
        }
        
        val cropConfig = CropConfigs.getById(crop.cropId) ?: return
        
        // 计算收益
        val quality = Quality.fromOrdinal(crop.quality)
        val basePrice = cropConfig.sellPrice
        val finalPrice = (basePrice * quality.multiplier).toLong()
        val exp = cropConfig.exp
        
        // 添加到库存
        val existing = repository.findInventoryItem(ItemCategories.CROP, crop.cropId, crop.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.addInventoryItem(InventoryItem(
                itemType = ItemCategories.CROP,
                configId = crop.cropId,
                quantity = 1,
                quality = crop.quality
            ))
        }
        
        // 返还种子（50%概率）
        if (Math.random() < 0.5) {
            val seedExisting = repository.findInventoryItem(ItemCategories.CROP_SEED, crop.cropId, 0)
            if (seedExisting != null) {
                repository.updateInventoryItem(seedExisting.copy(quantity = seedExisting.quantity + 1))
            } else {
                repository.addInventoryItem(InventoryItem(
                    itemType = ItemCategories.CROP_SEED,
                    configId = crop.cropId,
                    quantity = 1,
                    quality = 0
                ))
            }
        }
        
        // 删除作物
        repository.deleteCrop(instanceId)
        _crops.value = repository.getAllCrops()
        
        // 刷新库存
        _inventory.value = repository.getInventory()
        
        // 添加经验和金币
        addExpAndGold(exp, finalPrice)
        
        // 更新统计
        val stats = repository.getStats()
        repository.updateStats(stats.copy(totalHarvest = stats.totalHarvest + 1))
        
        showToast("${cropConfig.icon} 收获成功！+${finalPrice}金币")
    }
    
    suspend fun harvestAllReady() {
        val ready = _crops.value.filter { TimeSystem.isReady(it.finishTime) }
        if (ready.isEmpty()) {
            showToast("没有已成熟的作物"); return
        }
        
        ready.forEach { harvestCrop(it.instanceId) }
        showToast("收获完成！共 ${ready.size} 个作物")
    }
    
    suspend fun expandFarmland() {
        val p = _player.value ?: return
        val currentCount = _lands.value.count { it.unlocked }
        val cost = (currentCount + 1) * 100L
        
        if (p.gold < cost) {
            showToast("扩建需要 $cost 金币"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - cost)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        // 添加新土地
        val newLand = LandInstance(
            landId = System.currentTimeMillis(),
            region = "FARMLAND",
            index = currentCount,
            unlocked = true
        )
        repository.insertLand(newLand)
        _lands.value = repository.getAllLands()
        
        showToast("🏠 扩建成功！新土地已解锁")
    }
    
    // ===== 动物系统 =====
    suspend fun buyAnimal(animalId: Int) {
        val config = AnimalConfigs.getById(animalId) ?: run {
            showToast("动物不存在"); return
        }
        
        val p = _player.value ?: return
        if (p.level < config.unlockLevel) {
            showToast("需要 ${config.unlockLevel} 级才能购买"); return
        }
        
        if (p.gold < config.purchasePrice) {
            showToast("金币不足"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - config.purchasePrice)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        val quality = QualitySystem.rollQuality()
        val animal = AnimalInstance(
            animalId = animalId,
            level = 1,
            exp = 0,
            lastProduceTime = TimeSystem.currentTimeMs(),
            quality = quality.intValue
        )
        
        repository.insertAnimal(animal)
        _animals.value = repository.getAllAnimals()
        
        // 更新图鉴
        updateCollection(ItemCategories.ANIMAL_PRODUCT, animalId, quality.intValue, config.name)
        
        showToast("${config.icon} 购买成功：${config.name}")
    }
    
    suspend fun collectAnimalProduct(instanceId: Long) {
        val animal = _animals.value.find { it.instanceId == instanceId } ?: run {
            showToast("动物不存在"); return
        }
        
        val config = AnimalConfigs.getById(animal.animalId) ?: return
        val now = TimeSystem.currentTimeMs()
        
        if (now < animal.lastProduceTime + config.productTimeSeconds * 1000L) {
            showToast("动物还未产出"); return
        }
        
        // 添加到库存
        val existing = repository.findInventoryItem(ItemCategories.ANIMAL_PRODUCT, animal.animalId, animal.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.addInventoryItem(InventoryItem(
                itemType = ItemCategories.ANIMAL_PRODUCT,
                configId = animal.animalId,
                quantity = 1,
                quality = animal.quality
            ))
        }
        
        // 更新产出时间
        repository.updateAnimal(animal.copy(lastProduceTime = now))
        _animals.value = repository.getAllAnimals()
        
        // 刷新库存
        _inventory.value = repository.getInventory()
        
        showToast("${config.icon} 获得 ${config.productName}")
    }
    
    // ===== 宠物系统 =====
    suspend fun buyPet(petId: Int) {
        val config = PetConfigs.getById(petId) ?: run {
            showToast("宠物不存在"); return
        }
        
        val p = _player.value ?: return
        if (p.level < config.unlockLevel) {
            showToast("需要 ${config.unlockLevel} 级才能购买"); return
        }
        
        if (p.gold < config.purchasePrice) {
            showToast("金币不足"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - config.purchasePrice)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        val pet = PetInstance(
            petId = petId,
            level = 1,
            exp = 0,
            friendliness = 50,
            isActive = true,
            workSlots = 1
        )
        
        repository.insertPet(pet)
        _pets.value = repository.getAllPets()
        
        showToast("${config.icon} 获得新宠物：${config.name}")
    }
    
    // ===== 果树系统 =====
    suspend fun plantTree(configId: Int) {
        val config = TreeConfigs.getById(configId) ?: run {
            showToast("果树配置不存在"); return
        }
        
        val p = _player.value ?: return
        if (p.level < config.unlockLevel) {
            showToast("需要 ${config.unlockLevel} 级才能种植"); return
        }
        
        val cost = config.sellPrice / 2
        if (p.gold < cost) {
            showToast("金币不足，需要 $cost 金"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - cost)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        val quality = QualitySystem.rollQuality()
        val growTimeMs = config.growTimeHours * 60L * 60L * 1000L
        
        val tree = TreeInstance(
            configId = configId,
            plantTime = TimeSystem.currentTimeMs(),
            harvestTime = TimeSystem.currentTimeMs() + growTimeMs,
            quality = quality.intValue,
            region = "ORCHARD"
        )
        
        repository.insertTree(tree)
        _trees.value = repository.getAllTrees()
        
        updateCollection(ItemCategories.TREE_FRUIT, configId, quality.intValue, config.name)
        
        showToast("${config.icon} 种植成功：${config.name}")
    }
    
    suspend fun harvestTree(treeId: Long) {
        val tree = _trees.value.find { it.treeId == treeId } ?: run {
            showToast("果树不存在"); return
        }
        
        if (!TimeSystem.isReady(tree.harvestTime)) {
            showToast("还未成熟"); return
        }
        
        val config = TreeConfigs.getById(tree.configId) ?: return
        val quality = Quality.fromOrdinal(tree.quality)
        val finalPrice = (config.sellPrice * quality.multiplier).toLong()
        
        // 添加到库存
        val existing = repository.findInventoryItem(ItemCategories.TREE_FRUIT, tree.configId, tree.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.addInventoryItem(InventoryItem(
                itemType = ItemCategories.TREE_FRUIT,
                configId = tree.configId,
                quantity = 1,
                quality = tree.quality
            ))
        }
        
        // 更新收获时间
        val harvestIntervalMs = config.harvestTimeHours * 60L * 60L * 1000L
        repository.updateTree(tree.copy(harvestTime = TimeSystem.currentTimeMs() + harvestIntervalMs))
        _trees.value = repository.getAllTrees()
        
        _inventory.value = repository.getInventory()
        addExpAndGold(config.exp, finalPrice)
        
        showToast("${config.icon} 收获成功！+${finalPrice}金币")
    }
    
    // ===== 鱼塘系统 =====
    suspend fun unlockPond(pondId: Long) {
        val p = _player.value ?: return
        val cost = 10000L
        
        if (p.gold < cost) {
            showToast("解锁需要 $cost 金币"); return
        }
        
        val pond = _ponds.value.find { it.pondId == pondId } ?: run {
            showToast("鱼塘不存在"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - cost)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        repository.updatePond(pond.copy(unlocked = true))
        _ponds.value = repository.getAllPonds()
        
        showToast("🐟 鱼塘解锁成功！")
    }
    
    suspend fun addFish(pondId: Long, fishId: Int) {
        val fishConfig = FishConfigs.getById(fishId) ?: run {
            showToast("鱼类不存在"); return
        }
        
        val p = _player.value ?: return
        if (p.level < fishConfig.unlockLevel) {
            showToast("需要 ${fishConfig.unlockLevel} 级才能养"); return
        }
        
        val cost = fishConfig.sellPrice / 2
        if (p.gold < cost) {
            showToast("金币不足"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - cost)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        val quality = QualitySystem.rollQuality()
        val growTimeMs = fishConfig.growTimeMinutes * 60L * 1000L
        
        val newFish = FishInstance(
            pondId = pondId,
            configId = fishId,
            finishTime = TimeSystem.currentTimeMs() + growTimeMs,
            quality = quality.intValue
        )
        
        repository.insertFish(newFish)
        _fish.value = repository.getAllFish()
        
        updateCollection(ItemCategories.FISH, fishId, quality.intValue, fishConfig.name)
        
        showToast("${fishConfig.icon} 放入成功：${fishConfig.name}")
    }
    
    suspend fun collectFish(fishId: Long) {
        val fish = _fish.value.find { it.fishId == fishId } ?: run {
            showToast("鱼不存在"); return
        }
        
        if (!TimeSystem.isReady(fish.finishTime)) {
            showToast("还未成熟"); return
        }
        
        val config = FishConfigs.getById(fish.configId) ?: return
        val quality = Quality.fromOrdinal(fish.quality)
        val finalPrice = (config.sellPrice * quality.multiplier).toLong()
        
        // 添加到库存
        val existing = repository.findInventoryItem(ItemCategories.FISH, fish.configId, fish.quality)
        if (existing != null) {
            repository.updateInventoryItem(existing.copy(quantity = existing.quantity + 1))
        } else {
            repository.addInventoryItem(InventoryItem(
                itemType = ItemCategories.FISH,
                configId = fish.configId,
                quantity = 1,
                quality = fish.quality
            ))
        }
        
        // 重新放入一条鱼
        repository.updateFish(fish.copy(finishTime = TimeSystem.currentTimeMs() + config.growTimeMinutes * 60 * 1000L))
        _fish.value = repository.getAllFish()
        
        _inventory.value = repository.getInventory()
        addExpAndGold(config.exp, finalPrice)
        
        showToast("${config.icon} 收获成功！+${finalPrice}金币")
    }
    
    // ===== 工厂系统 =====
    suspend fun buyFactory(factoryId: Int) {
        val config = FactoryConfigs.getFactoryById(factoryId) ?: run {
            showToast("工厂不存在"); return
        }
        
        val p = _player.value ?: return
        if (p.level < config.unlockLevel) {
            showToast("需要 ${config.unlockLevel} 级才能购买"); return
        }
        
        if (p.gold < config.purchasePrice) {
            showToast("金币不足"); return
        }
        
        // 先扣金币
        val newPlayer = p.copy(gold = p.gold - config.purchasePrice)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        val factory = FactoryInstance(
            factoryType = factoryId.toString(),
            level = 1,
            unlocked = true
        )
        
        repository.insertFactory(factory)
        _factories.value = repository.getAllFactories()
        
        showToast("🏭 工厂建造成功：${config.name}")
    }
    
    // ===== 仓库系统 =====
    suspend fun sellItem(itemId: Long) {
        val item = _inventory.value.find { it.itemId == itemId } ?: run {
            showToast("物品不存在"); return
        }
        
        val price = calculateItemPrice(item)
        val finalPrice = price * item.quantity
        
        // 从库存移除
        if (item.quantity <= 1) {
            repository.deleteInventoryItem(itemId)
        } else {
            repository.updateInventoryItem(item.copy(quantity = item.quantity - 1))
        }
        
        // 添加金币
        val p = _player.value ?: return
        val newPlayer = p.copy(gold = p.gold + finalPrice)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        _inventory.value = repository.getInventory()
        
        showToast("💰 出售成功！+${finalPrice}金币")
    }
    
    suspend fun sellAllItems() {
        val items = _inventory.value
        if (items.isEmpty()) {
            showToast("仓库为空"); return
        }
        
        var totalPrice = 0L
        items.forEach { item ->
            totalPrice += calculateItemPrice(item) * item.quantity
            repository.deleteInventoryItem(item.itemId)
        }
        
        val p = _player.value ?: return
        val newPlayer = p.copy(gold = p.gold + totalPrice)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        _inventory.value = repository.getInventory()
        showToast("💰 全部出售成功！+${totalPrice}金币")
    }
    
    private fun calculateItemPrice(item: InventoryItem): Long {
        val basePrice = when (item.itemType) {
            ItemCategories.CROP -> CropConfigs.getById(item.configId)?.sellPrice ?: 10
            ItemCategories.ANIMAL_PRODUCT -> AnimalConfigs.getById(item.configId)?.productSellPrice ?: 10
            ItemCategories.TREE_FRUIT -> TreeConfigs.getById(item.configId)?.sellPrice ?: 10
            ItemCategories.FISH -> FishConfigs.getById(item.configId)?.sellPrice ?: 10
            ItemCategories.PROCESSED -> FactoryConfigs.getRecipeById(item.configId)?.outputSellPrice ?: 50
            else -> 10
        }
        val quality = Quality.fromOrdinal(item.quality)
        return (basePrice * quality.multiplier).toLong()
    }
    
    // ===== 图鉴系统 =====
    private suspend fun updateCollection(type: String, configId: Int, qualityIndex: Int, name: String) {
        val existing = repository.findCollection(type, configId)
        if (existing != null) {
            val newHighest = maxOf(existing.highestQuality, qualityIndex)
            repository.updateCollection(existing.copy(
                highestQuality = newHighest,
                count = existing.count + 1
            ))
        } else {
            repository.insertCollection(CollectionRecord(
                type = type,
                configId = configId,
                name = name,
                highestQuality = qualityIndex,
                count = 1
            ))
        }
        
        _collections.value = repository.getAllCollections()
        
        // 更新玩家收藏分
        val p = _player.value ?: return
        val newScore = _collections.value.sumOf { 
            (it.highestQuality + 1) * 10 + it.count 
        }
        val newPlayer = p.copy(collectionScore = newScore)
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
    }
    
    // ===== 经验与金币 =====
    private suspend fun addExpAndGold(exp: Int, gold: Long) {
        val p = _player.value ?: return
        
        val (newLevel, newExp) = LevelingSystem.addExp(p.level, p.exp, exp)
        val newGold = p.gold + gold
        
        val newPlayer = p.copy(
            level = newLevel,
            exp = newExp,
            gold = newGold
        )
        
        repository.updatePlayer(newPlayer)
        _player.value = newPlayer
        
        if (newLevel > p.level) {
            showToast("🎉 升级！现在是 ${newLevel} 级！")
        }
    }
    
    // ===== 教程系统 =====
    suspend fun getNextTutorialStep(): TutorialSystem.Step? {
        val completed = repository.getTutorialProgress()
        return TutorialSystem.getNextStep(completed)
    }
    
    suspend fun markTutorialStepComplete(stepId: Int) {
        repository.markTutorialComplete(stepId)
    }
    
    suspend fun updateTutorialCurrentStep(stepId: Int) {
        repository.updateTutorialCurrentStep(stepId)
    }
    
    // ===== 清理资源 =====
    fun cancel() {
        scope.cancel()
    }
}
