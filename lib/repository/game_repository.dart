import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

import '../models/game_models.dart';

const _keyPlayer = 'farmlife_player';
const _keyLands = 'farmlife_lands';
const _keyCrops = 'farmlife_crops';
const _keyAnimals = 'farmlife_animals';
const _keyPets = 'farmlife_pets';
const _keyFactories = 'farmlife_factories';
const _keyQueues = 'farmlife_queues';
const _keyInventory = 'farmlife_inventory';
const _keyCollections = 'farmlife_collections';
const _keyTrees = 'farmlife_trees';
const _keyFish = 'farmlife_fish';
const _keyPonds = 'farmlife_ponds';
const _keyPetFacilities = 'farmlife_pet_facilities';
const _keyLogs = 'farmlife_logs';
const _keyTutorial = 'farmlife_tutorial';
const _keyInitialized = 'farmlife_initialized';

class GameRepository {
  final SharedPreferences _prefs;
  GameRepository(this._prefs);

  static Future<GameRepository> init() async {
    final prefs = await SharedPreferences.getInstance();
    return GameRepository(prefs);
  }

  bool isInitialized() {
    return _prefs.getBool(_keyInitialized) ?? false;
  }

  Future<void> markInitialized() async {
    await _prefs.setBool(_keyInitialized, true);
  }

  // ==================== Player ====================
  Player getPlayer() {
    final data = _prefs.getString(_keyPlayer);
    if (data == null) {
      return Player(
        name: '农夫',
        level: 1,
        gold: 500,
        collectionScore: 0,
        totalExp: 0,
        playStartTime: DateTime.now().millisecondsSinceEpoch,
        lastLoginTime: DateTime.now().millisecondsSinceEpoch,
      );
    }
    return Player.fromJson(json.decode(data));
  }

  Future<void> updatePlayer(Player player) async {
    await _prefs.setString(_keyPlayer, json.encode(player.toJson()));
  }

  // ==================== Lands ====================
  List<Land> getAllLands() {
    final data = _prefs.getString(_keyLands);
    if (data == null) return _defaultLands();
    final list = json.decode(data) as List;
    return list.map((e) => Land.fromJson(e)).toList();
  }

  List<Land> _defaultLands() {
    final lands = <Land>[];
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 6; y++) {
        lands.add(Land(id: 'land_${x}_$y', x: x, y: y, region: 'FARMLAND', unlocked: true));
      }
    }
    return lands;
  }

  Future<void> insertAllLands(List<Land> lands) async {
    await _prefs.setString(_keyLands, json.encode(lands.map((e) => e.toJson()).toList()));
  }

  Future<void> updateLand(Land land) async {
    final lands = getAllLands();
    final idx = lands.indexWhere((l) => l.id == land.id);
    if (idx >= 0) {
      lands[idx] = land;
    } else {
      lands.add(land);
    }
    await insertAllLands(lands);
  }

  // ==================== Crops ====================
  List<Crop> getAllCrops() {
    final data = _prefs.getString(_keyCrops);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Crop.fromJson(e)).toList();
  }

  Future<void> insertCrop(Crop crop) async {
    final crops = getAllCrops();
    crops.removeWhere((c) => c.landId == crop.landId);
    crops.add(crop);
    await _prefs.setString(_keyCrops, json.encode(crops.map((e) => e.toJson()).toList()));
  }

  Future<void> deleteCrop(String cropId) async {
    final crops = getAllCrops();
    crops.removeWhere((c) => c.id == cropId);
    await _prefs.setString(_keyCrops, json.encode(crops.map((e) => e.toJson()).toList()));
  }

  // ==================== Animals ====================
  List<Animal> getAllAnimals() {
    final data = _prefs.getString(_keyAnimals);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Animal.fromJson(e)).toList();
  }

  Future<void> insertAnimal(Animal animal) async {
    final animals = getAllAnimals();
    animals.add(animal);
    await _prefs.setString(_keyAnimals, json.encode(animals.map((e) => e.toJson()).toList()));
  }

  Future<void> updateAnimal(Animal animal) async {
    final animals = getAllAnimals();
    final idx = animals.indexWhere((a) => a.id == animal.id);
    if (idx >= 0) animals[idx] = animal;
    await _prefs.setString(_keyAnimals, json.encode(animals.map((e) => e.toJson()).toList()));
  }

  // ==================== Pets ====================
  List<Pet> getAllPets() {
    final data = _prefs.getString(_keyPets);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Pet.fromJson(e)).toList();
  }

  Future<void> insertPet(Pet pet) async {
    final pets = getAllPets();
    pets.add(pet);
    await _prefs.setString(_keyPets, json.encode(pets.map((e) => e.toJson()).toList()));
  }

  Future<void> updatePet(Pet pet) async {
    final pets = getAllPets();
    final idx = pets.indexWhere((p) => p.id == pet.id);
    if (idx >= 0) pets[idx] = pet;
    await _prefs.setString(_keyPets, json.encode(pets.map((e) => e.toJson()).toList()));
  }

  // ==================== Factories ====================
  List<Factory> getAllFactories() {
    final data = _prefs.getString(_keyFactories);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Factory.fromJson(e)).toList();
  }

  Future<void> insertFactory(Factory factory) async {
    final factories = getAllFactories();
    factories.add(factory);
    await _prefs.setString(_keyFactories, json.encode(factories.map((e) => e.toJson()).toList()));
  }

  Future<void> updateFactory(Factory factory) async {
    final factories = getAllFactories();
    final idx = factories.indexWhere((f) => f.id == factory.id);
    if (idx >= 0) factories[idx] = factory;
    await _prefs.setString(_keyFactories, json.encode(factories.map((e) => e.toJson()).toList()));
  }

  // ==================== Production Queues ====================
  List<ProductionQueue> getAllQueues() {
    final data = _prefs.getString(_keyQueues);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => ProductionQueue.fromJson(e)).toList();
  }

  Future<void> insertQueue(ProductionQueue queue) async {
    final queues = getAllQueues();
    queues.add(queue);
    await _prefs.setString(_keyQueues, json.encode(queues.map((e) => e.toJson()).toList()));
  }

  Future<void> updateQueue(ProductionQueue queue) async {
    final queues = getAllQueues();
    final idx = queues.indexWhere((q) => q.id == queue.id);
    if (idx >= 0) queues[idx] = queue;
    await _prefs.setString(_keyQueues, json.encode(queues.map((e) => e.toJson()).toList()));
  }

  // ==================== Inventory ====================
  List<InventoryItem> getAllInventory() {
    final data = _prefs.getString(_keyInventory);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => InventoryItem.fromJson(e)).toList();
  }

  InventoryItem? findInventoryItem(String itemType, int configId, int quality) {
    final items = getAllInventory();
    try {
      return items.firstWhere((i) => i.itemType == itemType && i.configId == configId && i.quality == quality);
    } catch (_) {
      return null;
    }
  }

  Future<void> insertInventoryItem(InventoryItem item) async {
    final items = getAllInventory();
    final existing = items.where((i) => i.itemType == item.itemType && i.configId == item.configId && i.quality == item.quality).toList();
    if (existing.isNotEmpty) {
      final idx = items.indexWhere((i) => i.id == existing.first.id);
      items[idx] = InventoryItem(id: existing.first.id, itemType: item.itemType, configId: item.configId, quantity: existing.first.quantity + item.quantity, quality: item.quality);
    } else {
      items.add(item);
    }
    await _prefs.setString(_keyInventory, json.encode(items.map((e) => e.toJson()).toList()));
  }

  Future<void> updateInventoryItem(InventoryItem item) async {
    final items = getAllInventory();
    final idx = items.indexWhere((i) => i.id == item.id);
    if (idx >= 0) items[idx] = item;
    await _prefs.setString(_keyInventory, json.encode(items.map((e) => e.toJson()).toList()));
  }

  Future<void> deleteInventoryItem(String itemId) async {
    final items = getAllInventory();
    items.removeWhere((i) => i.id == itemId);
    await _prefs.setString(_keyInventory, json.encode(items.map((e) => e.toJson()).toList()));
  }

  // ==================== Collections ====================
  List<CollectionRecord> getAllCollections() {
    final data = _prefs.getString(_keyCollections);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => CollectionRecord.fromJson(e)).toList();
  }

  CollectionRecord? findCollection(String collectionType, int configId) {
    final records = getAllCollections();
    try {
      return records.firstWhere((r) => r.collectionType == collectionType && r.configId == configId);
    } catch (_) {
      return null;
    }
  }

  Future<void> insertCollection(CollectionRecord record) async {
    final records = getAllCollections();
    records.add(record);
    await _prefs.setString(_keyCollections, json.encode(records.map((e) => e.toJson()).toList()));
  }

  Future<void> updateCollection(CollectionRecord record) async {
    final records = getAllCollections();
    final idx = records.indexWhere((r) => r.id == record.id);
    if (idx >= 0) records[idx] = record;
    await _prefs.setString(_keyCollections, json.encode(records.map((e) => e.toJson()).toList()));
  }

  // ==================== Trees ====================
  List<Tree> getAllTrees() {
    final data = _prefs.getString(_keyTrees);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Tree.fromJson(e)).toList();
  }

  Future<void> insertTree(Tree tree) async {
    final trees = getAllTrees();
    trees.add(tree);
    await _prefs.setString(_keyTrees, json.encode(trees.map((e) => e.toJson()).toList()));
  }

  Future<void> updateTree(Tree tree) async {
    final trees = getAllTrees();
    final idx = trees.indexWhere((t) => t.id == tree.id);
    if (idx >= 0) trees[idx] = tree;
    await _prefs.setString(_keyTrees, json.encode(trees.map((e) => e.toJson()).toList()));
  }

  Future<void> deleteTree(String treeId) async {
    final trees = getAllTrees();
    trees.removeWhere((t) => t.id == treeId);
    await _prefs.setString(_keyTrees, json.encode(trees.map((e) => e.toJson()).toList()));
  }

  // ==================== Fish ====================
  List<Fish> getAllFish() {
    final data = _prefs.getString(_keyFish);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => Fish.fromJson(e)).toList();
  }

  Future<void> insertFish(Fish fish) async {
    final fishList = getAllFish();
    fishList.add(fish);
    await _prefs.setString(_keyFish, json.encode(fishList.map((e) => e.toJson()).toList()));
  }

  Future<void> deleteFish(String fishId) async {
    final fishList = getAllFish();
    fishList.removeWhere((f) => f.id == fishId);
    await _prefs.setString(_keyFish, json.encode(fishList.map((e) => e.toJson()).toList()));
  }

  // ==================== Ponds ====================
  List<Pond> getAllPonds() {
    final data = _prefs.getString(_keyPonds);
    if (data == null) return _defaultPonds();
    final list = json.decode(data) as List;
    return list.map((e) => Pond.fromJson(e)).toList();
  }

  List<Pond> _defaultPonds() {
    return [
      Pond(id: 'pond_1', x: 0, y: 0, unlocked: false),
      Pond(id: 'pond_2', x: 1, y: 0, unlocked: false),
      Pond(id: 'pond_3', x: 0, y: 1, unlocked: false),
      Pond(id: 'pond_4', x: 1, y: 1, unlocked: false),
    ];
  }

  Future<void> updatePond(Pond pond) async {
    final ponds = getAllPonds();
    final idx = ponds.indexWhere((p) => p.id == pond.id);
    if (idx >= 0) ponds[idx] = pond;
    await _prefs.setString(_keyPonds, json.encode(ponds.map((e) => e.toJson()).toList()));
  }

  // ==================== Pet Facilities ====================
  List<PetFacility> getAllPetFacilities() {
    final data = _prefs.getString(_keyPetFacilities);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.map((e) => PetFacility.fromJson(e)).toList();
  }

  Future<void> insertPetFacility(PetFacility facility) async {
    final facilities = getAllPetFacilities();
    facilities.add(facility);
    await _prefs.setString(_keyPetFacilities, json.encode(facilities.map((e) => e.toJson()).toList()));
  }

  // ==================== Logs ====================
  List<GameLog> getRecentLogs() {
    final data = _prefs.getString(_keyLogs);
    if (data == null) return [];
    final list = json.decode(data) as List;
    final logs = list.map((e) => GameLog.fromJson(e)).toList();
    logs.sort((a, b) => b.timestamp.compareTo(a.timestamp));
    return logs.take(50).toList();
  }

  Future<void> addLog(String type, String message) async {
    final logs = getRecentLogs();
    logs.insert(0, GameLog(id: 'log_${DateTime.now().millisecondsSinceEpoch}', type: type, message: message));
    await _prefs.setString(_keyLogs, json.encode(logs.take(200).map((e) => e.toJson()).toList()));
  }

  // ==================== Tutorial ====================
  List<int> getCompletedTutorialSteps() {
    final data = _prefs.getString(_keyTutorial);
    if (data == null) return [];
    final list = json.decode(data) as List;
    return list.cast<int>();
  }

  Future<void> markTutorialStep(int stepId) async {
    final steps = getCompletedTutorialSteps();
    if (!steps.contains(stepId)) steps.add(stepId);
    await _prefs.setString(_keyTutorial, json.encode(steps));
  }
}
