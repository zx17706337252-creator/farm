import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../configs/game_configs.dart';
import '../models/game_models.dart';
import '../models/quality.dart';
import '../models/season_weather.dart';
import '../repository/game_repository.dart';
import '../systems/leveling_system.dart';
import '../systems/quality_system.dart';
import '../systems/time_system.dart';

// ============ 懒初始化的全局 GameRepository ============
GameRepository? _repoInstance;
Future<GameRepository> _ensureRepo() async {
  _repoInstance ??= await GameRepository.init();
  return _repoInstance!;
}

class GameEngine {
  final Ref _ref;
  final GameRepository _repo;
  GameEngine._(this._ref, this._repo);

  static Future<GameEngine> create(Ref ref) async {
    final repo = await _ensureRepo();
    return GameEngine._(ref, repo);
  }

  // ===== Player =====
  Future<Player> loadPlayer() async {
    final player = _repo.getPlayer();
    final updated =
        player.copyWith(lastLoginTime: TimeSystem.currentTimeMs());
    await _repo.updatePlayer(updated);
    return updated;
  }

  Future<void> addGold(Player player, int amount) async {
    await _repo
        .updatePlayer(player.copyWith(gold: player.gold + amount));
  }

  Future<void> spendGold(Player player, int amount) async {
    await _repo
        .updatePlayer(player.copyWith(gold: player.gold - amount));
  }

  Future<void> addExp(Player player, int exp) async {
    final newTotalExp = player.totalExp + exp;
    final newLevel = LevelingSystem.calculateLevel(newTotalExp);
    await _repo.updatePlayer(player.copyWith(
      totalExp: newTotalExp,
      level: newLevel,
    ));
  }

  // ===== Land / Crop =====
  Future<List<Land>> loadLands() => _repo.getAllLands();
  Future<List<Crop>> loadCrops() => _repo.getAllCrops();

  Future<bool> plantCrop(Land land, CropConfig config, Player player) async {
    if (player.gold < config.sellPrice * 2) return false;
    final season = TimeSystem.currentSeason(player.playStartTime);
    final seasonMul = SeasonWeatherSystem.seasonGrowthMultiplier(season);
    final adjusted = (config.growTimeSeconds * 1000 / seasonMul).toInt();
    final finishTime = TimeSystem.currentTimeMs() + adjusted;
    final weather = TimeSystem.currentWeather();
    final weatherBonus =
        SeasonWeatherSystem.weatherQualityBonus(weather, season);
    final qualityIdx = QualitySystem.rollQualityIndex(weatherBonus);
    final crop = Crop(
      id: 'crop_${DateTime.now().millisecondsSinceEpoch}_${land.id}',
      cropId: config.cropId,
      landId: land.id,
      plantTime: TimeSystem.currentTimeMs(),
      finishTime: finishTime,
      quality: qualityIdx,
    );
    await _repo.insertCrop(crop);
    await spendGold(player, config.sellPrice * 2);
    await _repo.addLog('PLANT', '种植了 ${config.icon}${config.name}');
    return true;
  }

  Future<(int gold, int exp)?> harvestCrop(Crop crop, Player player) async {
    if (!crop.isReady) return null;
    final config = CropConfigs.getById(crop.cropId);
    if (config == null) return null;
    final qualityMul = QualitySystem.qualityMultiplier(crop.quality);
    final basePrice = config.sellPrice;
    final baseExp = config.exp;
    final finalPrice = (basePrice * qualityMul).toInt();
    final finalExp = (baseExp * qualityMul).toInt();
    final existing = _repo.findInventoryItem('CROP', crop.cropId, crop.quality);
    if (existing != null) {
      await _repo.updateInventoryItem(InventoryItem(
          id: existing.id,
          itemType: 'CROP',
          configId: crop.cropId,
          quantity: existing.quantity + 1,
          quality: crop.quality));
    } else {
      await _repo.insertInventoryItem(InventoryItem(
          id: 'inv_${DateTime.now().millisecondsSinceEpoch}',
          itemType: 'CROP',
          configId: crop.cropId,
          quantity: 1,
          quality: crop.quality));
    }
    await addGold(player, finalPrice);
    await addExp(player, finalExp);
    await _repo.deleteCrop(crop.id);
    await updateCollection('CROP', crop.cropId, crop.quality);
    await _repo.addLog(
        'HARVEST', '收获了 ${config.icon}${config.name} 获得 ${finalPrice}金币');
    return (finalPrice, finalExp);
  }

  // ===== Animal =====
  Future<List<Animal>> loadAnimals() => _repo.getAllAnimals();

  Future<bool> buyAnimal(AnimalConfig config, Player player) async {
    if (player.gold < config.purchasePrice) return false;
    final quality = QualitySystem.rollQualityIndex();
    final animal = Animal(
      id: 'animal_${DateTime.now().millisecondsSinceEpoch}',
      animalId: config.animalId,
      quality: quality,
      lastProduceTime: TimeSystem.currentTimeMs(),
      region: 'LIVESTOCK',
    );
    await _repo.insertAnimal(animal);
    await spendGold(player, config.purchasePrice);
    await updateCollection('ANIMAL', config.animalId, quality);
    await _repo.addLog('BUY', '购买了 ${config.icon}${config.name}');
    return true;
  }

  Future<(int gold, int exp)?> collectAnimalProduct(
      Animal animal, Player player) async {
    final config = AnimalConfigs.getById(animal.animalId);
    if (config == null) return null;
    final season = TimeSystem.currentSeason(player.playStartTime);
    final weather = TimeSystem.currentWeather();
    final efficiency = SeasonWeatherSystem.calculateAnimalEfficiency(
        season,
        weather,
        AnimalLevelingSystem.efficiencyMultiplier(animal.level));
    final productTime =
        (config.productTimeSeconds * 1000 / efficiency).toInt();
    final now = TimeSystem.currentTimeMs();
    if (now - animal.lastProduceTime < productTime) return null;
    final qualityIdx = QualitySystem.rollQualityIndex();
    final qualityMul = QualitySystem.qualityMultiplier(qualityIdx);
    final expGained = config.exp;
    final existing = _repo.findInventoryItem('ANIMAL', animal.animalId, qualityIdx);
    if (existing != null) {
      await _repo.updateInventoryItem(InventoryItem(
          id: existing.id,
          itemType: 'ANIMAL',
          configId: animal.animalId,
          quantity: existing.quantity + 1,
          quality: qualityIdx));
    } else {
      await _repo.insertInventoryItem(InventoryItem(
          id: 'inv_${DateTime.now().millisecondsSinceEpoch}',
          itemType: 'ANIMAL',
          configId: animal.animalId,
          quantity: 1,
          quality: qualityIdx));
    }
    await _repo.updateAnimal(animal.copyWith(
        exp: animal.exp + expGained,
        level: AnimalLevelingSystem.calculateLevel(animal.exp + expGained),
        lastProduceTime: now));
    final gainGold = (config.productSellPrice * qualityMul).toInt();
    await addGold(player, gainGold);
    await addExp(player, expGained);
    await _repo.addLog('COLLECT', '收获了 ${config.icon}${config.name} 的产品');
    return (gainGold, expGained);
  }

  // ===== Pet =====
  Future<List<Pet>> loadPets() => _repo.getAllPets();

  Future<bool> buyPet(PetConfig config, Player player) async {
    if (player.gold < config.purchasePrice) return false;
    final quality = QualitySystem.rollQualityIndex();
    final pet = Pet(
      id: 'pet_${DateTime.now().millisecondsSinceEpoch}',
      petId: config.petId,
      quality: quality,
      workSlots: 1,
      isActive: true,
      region: 'PET_ESTATE',
    );
    await _repo.insertPet(pet);
    await spendGold(player, config.purchasePrice);
    await updateCollection('PET', config.petId, quality);
    await _repo.addLog('PET', '获得新宠物：${config.icon}${config.name}');
    return true;
  }

  // ===== Factory =====
  Future<List<Factory>> loadFactories() => _repo.getAllFactories();
  Future<List<ProductionQueue>> loadQueues() => _repo.getAllQueues();

  Future<bool> buyFactory(FactoryConfig config, Player player) async {
    if (player.gold < config.purchasePrice) return false;
    final factory = Factory(
      id: 'factory_${DateTime.now().millisecondsSinceEpoch}',
      factoryType: config.factoryId,
      queueSize: 1,
      region: 'PROCESSING',
    );
    await _repo.insertFactory(factory);
    await spendGold(player, config.purchasePrice);
    await _repo.addLog('BUILD', '建造了 ${config.icon}${config.name}');
    return true;
  }

  Future<bool> startProduction(
      Factory factory, RecipeConfig recipe, Player player) async {
    final input =
        _repo.findInventoryItem(recipe.inputItemType, recipe.inputConfigId, 0);
    if (input == null || input.quantity < recipe.inputQuantity) return false;
    if (input.quantity == recipe.inputQuantity) {
      await _repo.deleteInventoryItem(input.id);
    } else {
      await _repo.updateInventoryItem(InventoryItem(
          id: input.id,
          itemType: input.itemType,
          configId: input.configId,
          quantity: input.quantity - recipe.inputQuantity,
          quality: input.quality));
    }
    final finishTime = TimeSystem.calculateProcessFinishTime(
        recipe.processTimeSeconds, factory.level);
    final queue = ProductionQueue(
      id: 'queue_${DateTime.now().millisecondsSinceEpoch}',
      factoryId: factory.id,
      recipeId: recipe.recipeId,
      startTime: TimeSystem.currentTimeMs(),
      finishTime: finishTime,
    );
    await _repo.insertQueue(queue);
    await _repo.addLog('PROCESS', '开始生产：${recipe.icon}${recipe.name}');
    return true;
  }

  Future<(int gold, int exp)?> collectProduction(
      ProductionQueue queue, Player player) async {
    if (!queue.isReady) return null;
    final recipe = FactoryConfigs.getRecipeById(queue.recipeId);
    if (recipe == null) return null;
    final qualityIdx = QualitySystem.rollQualityIndex();
    final qualityMul = QualitySystem.qualityMultiplier(qualityIdx);
    final existing =
        _repo.findInventoryItem('PROCESSED', queue.recipeId, qualityIdx);
    if (existing != null) {
      await _repo.updateInventoryItem(InventoryItem(
          id: existing.id,
          itemType: 'PROCESSED',
          configId: queue.recipeId,
          quantity: existing.quantity + 1,
          quality: qualityIdx));
    } else {
      await _repo.insertInventoryItem(InventoryItem(
          id: 'inv_${DateTime.now().millisecondsSinceEpoch}',
          itemType: 'PROCESSED',
          configId: queue.recipeId,
          quantity: 1,
          quality: qualityIdx));
    }
    final factories = await loadFactories();
    final factory = factories.firstWhere(
        (f) => f.id == queue.factoryId,
        orElse: () => Factory(id: '', factoryType: 0));
    if (factory.id.isNotEmpty) {
      await _repo.updateFactory(factory.copyWith(
          level: BuildingLevelingSystem.calculateLevel(factory.exp + recipe.outputExp),
          exp: factory.exp + recipe.outputExp,
          queueSize: BuildingLevelingSystem.queueSize(
              BuildingLevelingSystem.calculateLevel(factory.exp + recipe.outputExp))));
    }
    final gainGold = (recipe.outputSellPrice * qualityMul * 0.5).toInt();
    await addGold(player, gainGold);
    await addExp(player, recipe.outputExp);
    await _repo.updateQueue(queue.copyWith(isCollected: true));
    return (gainGold, recipe.outputExp);
  }

  // ===== Inventory =====
  Future<List<InventoryItem>> loadInventory() => _repo.getAllInventory();

  Future<int> sellInventoryItem(
      InventoryItem item, int quantity, Player player) async {
    if (item.quantity < quantity) return 0;
    final price = _getSellPrice(item);
    final qualityMul = QualitySystem.qualityMultiplier(item.quality);
    final total = (price * quantity * qualityMul).toInt();
    if (item.quantity == quantity) {
      await _repo.deleteInventoryItem(item.id);
    } else {
      await _repo.updateInventoryItem(InventoryItem(
          id: item.id,
          itemType: item.itemType,
          configId: item.configId,
          quantity: item.quantity - quantity,
          quality: item.quality));
    }
    await addGold(player, total);
    return total;
  }

  Future<int> sellAllInventory(Player player) async {
    final items = await loadInventory();
    int total = 0;
    for (final item in items) {
      final price = _getSellPrice(item);
      final qualityMul = QualitySystem.qualityMultiplier(item.quality);
      total += (price * item.quantity * qualityMul).toInt();
    }
    for (final item in items) {
      await _repo.deleteInventoryItem(item.id);
    }
    if (total > 0) await addGold(player, total);
    return total;
  }

  int _getSellPrice(InventoryItem item) {
    switch (item.itemType) {
      case 'CROP':
        return CropConfigs.getById(item.configId)?.sellPrice ?? 0;
      case 'ANIMAL':
        return AnimalConfigs.getById(item.configId)?.productSellPrice ?? 0;
      case 'PROCESSED':
        return FactoryConfigs.getRecipeById(item.configId)?.outputSellPrice ?? 0;
      case 'TREE':
        return TreeConfigs.getById(item.configId)?.sellPrice ?? 0;
      case 'FISH':
        return FishConfigs.getById(item.configId)?.sellPrice ?? 0;
      default:
        return 0;
    }
  }

  // ===== Tree =====
  Future<List<Tree>> loadTrees() => _repo.getAllTrees();

  Future<bool> plantTree(
      int x, int y, TreeConfig config, Player player) async {
    if (player.gold < config.sellPrice) return false;
    final now = TimeSystem.currentTimeMs();
    final tree = Tree(
      id: 'tree_${DateTime.now().millisecondsSinceEpoch}',
      configId: config.treeId,
      x: x,
      y: y,
      plantedTime: now,
      nextHarvestTime: now + config.growTimeHours * 3600 * 1000,
    );
    await _repo.insertTree(tree);
    await spendGold(player, config.sellPrice);
    return true;
  }

  Future<(int gold, int exp)?> harvestTree(
      Tree tree, Player player) async {
    if (!tree.isReady) return null;
    final config = TreeConfigs.getById(tree.configId);
    if (config == null) return null;
    final season = TimeSystem.currentSeason(player.playStartTime);
    final weather = TimeSystem.currentWeather();
    final weatherBonus =
        SeasonWeatherSystem.weatherQualityBonus(weather, season);
    final qualityIdx = QualitySystem.rollQualityIndex(weatherBonus);
    final qualityMul = QualitySystem.qualityMultiplier(qualityIdx);
    final existing =
        _repo.findInventoryItem('TREE', tree.configId, qualityIdx);
    if (existing != null) {
      await _repo.updateInventoryItem(InventoryItem(
          id: existing.id,
          itemType: 'TREE',
          configId: tree.configId,
          quantity: existing.quantity + 1,
          quality: qualityIdx));
    } else {
      await _repo.insertInventoryItem(InventoryItem(
          id: 'inv_${DateTime.now().millisecondsSinceEpoch}',
          itemType: 'TREE',
          configId: tree.configId,
          quantity: 1,
          quality: qualityIdx));
    }
    await _repo.updateTree(tree.copyWith(
        exp: tree.exp + config.exp,
        level: BuildingLevelingSystem.calculateLevel(tree.exp + config.exp),
        nextHarvestTime:
            TimeSystem.currentTimeMs() + config.harvestTimeHours * 3600 * 1000));
    final gainGold = (config.sellPrice * qualityMul).toInt();
    await addGold(player, gainGold);
    await addExp(player, config.exp);
    return (gainGold, config.exp);
  }

  // ===== Fish =====
  Future<List<Fish>> loadFish() => _repo.getAllFish();
  Future<List<Pond>> loadPonds() => _repo.getAllPonds();

  Future<bool> addFish(
      Pond pond, FishConfig config, Player player) async {
    if (player.gold < config.sellPrice) return false;
    final fish = Fish(
      id: 'fish_${DateTime.now().millisecondsSinceEpoch}',
      configId: config.fishId,
      pondId: pond.id,
      placedTime: TimeSystem.currentTimeMs(),
      finishTime:
          TimeSystem.currentTimeMs() + config.growTimeMinutes * 60 * 1000,
      quality: QualitySystem.rollQualityIndex(),
    );
    await _repo.insertFish(fish);
    await spendGold(player, config.sellPrice);
    return true;
  }

  Future<(int gold, int exp)?> collectFish(
      Fish fish, Player player) async {
    if (!fish.isReady) return null;
    final config = FishConfigs.getById(fish.configId);
    if (config == null) return null;
    final qualityMul = QualitySystem.qualityMultiplier(fish.quality);
    final existing =
        _repo.findInventoryItem('FISH', fish.configId, fish.quality);
    if (existing != null) {
      await _repo.updateInventoryItem(InventoryItem(
          id: existing.id,
          itemType: 'FISH',
          configId: fish.configId,
          quantity: existing.quantity + 1,
          quality: fish.quality));
    } else {
      await _repo.insertInventoryItem(InventoryItem(
          id: 'inv_${DateTime.now().millisecondsSinceEpoch}',
          itemType: 'FISH',
          configId: fish.configId,
          quantity: 1,
          quality: fish.quality));
    }
    final gainGold = (config.sellPrice * qualityMul).toInt();
    await addGold(player, gainGold);
    await addExp(player, config.exp);
    await _repo.deleteFish(fish.id);
    return (gainGold, config.exp);
  }

  // ===== Pet Facility =====
  Future<List<PetFacility>> loadPetFacilities() =>
      _repo.getAllPetFacilities();

  // ===== Collection =====
  Future<void> updateCollection(String type, int configId, int quality) async {
    final existing = _repo.findCollection(type, configId);
    if (existing != null) {
      await _repo.updateCollection(CollectionRecord(
        id: existing.id,
        collectionType: type,
        configId: configId,
        highestQuality:
            existing.highestQuality > quality ? existing.highestQuality : quality,
        totalObtained: existing.totalObtained + 1,
      ));
    } else {
      await _repo.insertCollection(CollectionRecord(
        id: 'col_${DateTime.now().millisecondsSinceEpoch}',
        collectionType: type,
        configId: configId,
        highestQuality: quality,
        totalObtained: 1,
      ));
    }
    _recalcScore();
  }

  void _recalcScore() {
    final records = _repo.getAllCollections();
    final score = records.fold<int>(
        0,
        (sum, r) =>
            sum +
            CollectionScoreSystem.scoreForQuality(r.highestQuality) *
                r.totalObtained);
    final p = _repo.getPlayer();
    _repo.updatePlayer(p.copyWith(collectionScore: score));
  }

  Future<List<CollectionRecord>> loadCollections() =>
      _repo.getAllCollections();

  Season currentSeason(Player player) =>
      TimeSystem.currentSeason(player.playStartTime);
  Weather currentWeather() => TimeSystem.currentWeather();

  Future<List<GameLog>> loadLogs() => _repo.getRecentLogs();
}

// ==================== 全局 Provider ====================
final gameEngineProvider = FutureProvider<GameEngine>((ref) async {
  return GameEngine.create(ref);
});

final playerProvider =
    StateNotifierProvider<PlayerNotifier, AsyncValue<Player>>((ref) {
  return PlayerNotifier(ref);
});

class PlayerNotifier extends StateNotifier<AsyncValue<Player>> {
  final Ref _ref;
  PlayerNotifier(this._ref) : super(const AsyncValue.loading()) {
    _load();
  }
  Future<void> _load() async {
    try {
      final engine = await _ref.read(gameEngineProvider.future);
      final player = await engine.loadPlayer();
      state = AsyncValue.data(player);
    } catch (e, stackTrace) {
      state = AsyncValue.error(e, stackTrace);
    }
  }

  Future<void> refresh() async => _load();
}

// ===== 通用 List 生成器 =====
List<List<T>> _buildListProviders<T>(
    Future<List<T>> Function(GameEngine engine) mapper) {
  return [];
}

// 使用固定 providers（每个模块一个）
final landsProvider = FutureProvider<List<Land>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadLands();
});

final cropsProvider = FutureProvider<List<Crop>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadCrops();
});

final animalsProvider = FutureProvider<List<Animal>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadAnimals();
});

final petsProvider = FutureProvider<List<Pet>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadPets();
});

final factoriesProvider = FutureProvider<List<Factory>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadFactories();
});

final queuesProvider = FutureProvider<List<ProductionQueue>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadQueues();
});

final inventoryProvider = FutureProvider<List<InventoryItem>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadInventory();
});

final collectionsProvider =
    FutureProvider<List<CollectionRecord>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadCollections();
});

final treesProvider = FutureProvider<List<Tree>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadTrees();
});

final fishProvider = FutureProvider<List<Fish>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadFish();
});

final pondsProvider = FutureProvider<List<Pond>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadPonds();
});

final petFacilitiesProvider = FutureProvider<List<PetFacility>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadPetFacilities();
});

final logsProvider = FutureProvider<List<GameLog>>((ref) async {
  final engine = await ref.read(gameEngineProvider.future);
  return engine.loadLogs();
});

final seasonProvider = Provider<Season>((ref) {
  final playerAsync = ref.watch(playerProvider);
  return playerAsync.when(
    data: (player) => TimeSystem.currentSeason(player.playStartTime),
    loading: () => Season.spring,
    error: (_, __) => Season.spring,
  );
});

final weatherProvider = Provider<Weather>((ref) {
  return TimeSystem.currentWeather();
});
