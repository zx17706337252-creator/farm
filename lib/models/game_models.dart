import 'quality.dart';
import 'season_weather.dart';

class Player {
  String name;
  int level;
  int gold;
  int collectionScore;
  int totalExp;
  int lastLoginTime;
  int playStartTime;
  List<int> completedTutorialSteps;

  Player({
    this.name = '农夫',
    this.level = 1,
    this.gold = 100,
    this.collectionScore = 0,
    this.totalExp = 0,
    int? lastLoginTime,
    int? playStartTime,
    this.completedTutorialSteps = const [],
  })  : lastLoginTime = lastLoginTime ?? DateTime.now().millisecondsSinceEpoch,
        playStartTime = playStartTime ?? DateTime.now().millisecondsSinceEpoch;

  Player copyWith({
    String? name,
    int? level,
    int? gold,
    int? collectionScore,
    int? totalExp,
    int? lastLoginTime,
    int? playStartTime,
    List<int>? completedTutorialSteps,
  }) {
    return Player(
      name: name ?? this.name,
      level: level ?? this.level,
      gold: gold ?? this.gold,
      collectionScore: collectionScore ?? this.collectionScore,
      totalExp: totalExp ?? this.totalExp,
      lastLoginTime: lastLoginTime ?? this.lastLoginTime,
      playStartTime: playStartTime ?? this.playStartTime,
      completedTutorialSteps: completedTutorialSteps ?? this.completedTutorialSteps,
    );
  }

  Map<String, dynamic> toJson() => {
        'name': name,
        'level': level,
        'gold': gold,
        'collectionScore': collectionScore,
        'totalExp': totalExp,
        'lastLoginTime': lastLoginTime,
        'playStartTime': playStartTime,
        'completedTutorialSteps': completedTutorialSteps,
      };

  factory Player.fromJson(Map<String, dynamic> json) => Player(
        name: json['name'] ?? '农夫',
        level: json['level'] ?? 1,
        gold: json['gold'] ?? 100,
        collectionScore: json['collectionScore'] ?? 0,
        totalExp: json['totalExp'] ?? 0,
        lastLoginTime: json['lastLoginTime'],
        playStartTime: json['playStartTime'],
        completedTutorialSteps: List<int>.from(json['completedTutorialSteps'] ?? []),
      );
}

class Land {
  final String id;
  final int x;
  final int y;
  final String region;
  final bool unlocked;
  final int level;
  final int exp;

  Land({
    required this.id,
    required this.x,
    required this.y,
    this.region = 'FARMLAND',
    this.unlocked = true,
    this.level = 1,
    this.exp = 0,
  });

  Land copyWith({
    String? id,
    int? x,
    int? y,
    String? region,
    bool? unlocked,
    int? level,
    int? exp,
  }) {
    return Land(
      id: id ?? this.id,
      x: x ?? this.x,
      y: y ?? this.y,
      region: region ?? this.region,
      unlocked: unlocked ?? this.unlocked,
      level: level ?? this.level,
      exp: exp ?? this.exp,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'x': x,
        'y': y,
        'region': region,
        'unlocked': unlocked,
        'level': level,
        'exp': exp,
      };

  factory Land.fromJson(Map<String, dynamic> json) => Land(
        id: json['id'],
        x: json['x'],
        y: json['y'],
        region: json['region'] ?? 'FARMLAND',
        unlocked: json['unlocked'] ?? true,
        level: json['level'] ?? 1,
        exp: json['exp'] ?? 0,
      );
}

class Crop {
  final String id;
  final int cropId;
  final String landId;
  final int plantTime;
  final int finishTime;
  final int quality;
  final bool harvested;

  Crop({
    required this.id,
    required this.cropId,
    required this.landId,
    required this.plantTime,
    required this.finishTime,
    this.quality = 0,
    this.harvested = false,
  });

  Crop copyWith({
    String? id,
    int? cropId,
    String? landId,
    int? plantTime,
    int? finishTime,
    int? quality,
    bool? harvested,
  }) {
    return Crop(
      id: id ?? this.id,
      cropId: cropId ?? this.cropId,
      landId: landId ?? this.landId,
      plantTime: plantTime ?? this.plantTime,
      finishTime: finishTime ?? this.finishTime,
      quality: quality ?? this.quality,
      harvested: harvested ?? this.harvested,
    );
  }

  bool get isReady => finishTime <= DateTime.now().millisecondsSinceEpoch && !harvested;

  Map<String, dynamic> toJson() => {
        'id': id,
        'cropId': cropId,
        'landId': landId,
        'plantTime': plantTime,
        'finishTime': finishTime,
        'quality': quality,
        'harvested': harvested,
      };

  factory Crop.fromJson(Map<String, dynamic> json) => Crop(
        id: json['id'],
        cropId: json['cropId'],
        landId: json['landId'],
        plantTime: json['plantTime'],
        finishTime: json['finishTime'],
        quality: json['quality'] ?? 0,
        harvested: json['harvested'] ?? false,
      );
}

class Animal {
  final String id;
  final int animalId;
  final int level;
  final int exp;
  final int friendship;
  final int quality;
  final int lastProduceTime;
  final String region;

  Animal({
    required this.id,
    required this.animalId,
    this.level = 1,
    this.exp = 0,
    this.friendship = 10,
    this.quality = 0,
    int? lastProduceTime,
    this.region = 'LIVESTOCK',
  }) : lastProduceTime = lastProduceTime ?? DateTime.now().millisecondsSinceEpoch;

  Animal copyWith({
    String? id,
    int? animalId,
    int? level,
    int? exp,
    int? friendship,
    int? quality,
    int? lastProduceTime,
    String? region,
  }) {
    return Animal(
      id: id ?? this.id,
      animalId: animalId ?? this.animalId,
      level: level ?? this.level,
      exp: exp ?? this.exp,
      friendship: friendship ?? this.friendship,
      quality: quality ?? this.quality,
      lastProduceTime: lastProduceTime ?? this.lastProduceTime,
      region: region ?? this.region,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'animalId': animalId,
        'level': level,
        'exp': exp,
        'friendship': friendship,
        'quality': quality,
        'lastProduceTime': lastProduceTime,
        'region': region,
      };

  factory Animal.fromJson(Map<String, dynamic> json) => Animal(
        id: json['id'],
        animalId: json['animalId'],
        level: json['level'] ?? 1,
        exp: json['exp'] ?? 0,
        friendship: json['friendship'] ?? 10,
        quality: json['quality'] ?? 0,
        lastProduceTime: json['lastProduceTime'],
        region: json['region'] ?? 'LIVESTOCK',
      );
}

class Pet {
  final String id;
  final int petId;
  final int level;
  final int exp;
  final int friendship;
  final int quality;
  final int workSlots;
  final bool isActive;
  final String region;
  final String? currentMission;

  Pet({
    required this.id,
    required this.petId,
    this.level = 1,
    this.exp = 0,
    this.friendship = 10,
    this.quality = 0,
    this.workSlots = 1,
    this.isActive = true,
    this.region = 'PET_ESTATE',
    this.currentMission,
  });

  Pet copyWith({
    String? id,
    int? petId,
    int? level,
    int? exp,
    int? friendship,
    int? quality,
    int? workSlots,
    bool? isActive,
    String? region,
    String? currentMission,
  }) {
    return Pet(
      id: id ?? this.id,
      petId: petId ?? this.petId,
      level: level ?? this.level,
      exp: exp ?? this.exp,
      friendship: friendship ?? this.friendship,
      quality: quality ?? this.quality,
      workSlots: workSlots ?? this.workSlots,
      isActive: isActive ?? this.isActive,
      region: region ?? this.region,
      currentMission: currentMission,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'petId': petId,
        'level': level,
        'exp': exp,
        'friendship': friendship,
        'quality': quality,
        'workSlots': workSlots,
        'isActive': isActive,
        'region': region,
        'currentMission': currentMission,
      };

  factory Pet.fromJson(Map<String, dynamic> json) => Pet(
        id: json['id'],
        petId: json['petId'],
        level: json['level'] ?? 1,
        exp: json['exp'] ?? 0,
        friendship: json['friendship'] ?? 10,
        quality: json['quality'] ?? 0,
        workSlots: json['workSlots'] ?? 1,
        isActive: json['isActive'] ?? true,
        region: json['region'] ?? 'PET_ESTATE',
        currentMission: json['currentMission'],
      );
}

class Factory {
  final String id;
  final int factoryType;
  final int level;
  final int exp;
  final int queueSize;
  final String region;

  Factory({
    required this.id,
    required this.factoryType,
    this.level = 1,
    this.exp = 0,
    this.queueSize = 1,
    this.region = 'PROCESSING',
  });

  Factory copyWith({
    String? id,
    int? factoryType,
    int? level,
    int? exp,
    int? queueSize,
    String? region,
  }) {
    return Factory(
      id: id ?? this.id,
      factoryType: factoryType ?? this.factoryType,
      level: level ?? this.level,
      exp: exp ?? this.exp,
      queueSize: queueSize ?? this.queueSize,
      region: region ?? this.region,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'factoryType': factoryType,
        'level': level,
        'exp': exp,
        'queueSize': queueSize,
        'region': region,
      };

  factory Factory.fromJson(Map<String, dynamic> json) => Factory(
        id: json['id'],
        factoryType: json['factoryType'],
        level: json['level'] ?? 1,
        exp: json['exp'] ?? 0,
        queueSize: json['queueSize'] ?? 1,
        region: json['region'] ?? 'PROCESSING',
      );
}

class ProductionQueue {
  final String id;
  final String factoryId;
  final int recipeId;
  final int startTime;
  final int finishTime;
  final bool isCollected;

  ProductionQueue({
    required this.id,
    required this.factoryId,
    required this.recipeId,
    required this.startTime,
    required this.finishTime,
    this.isCollected = false,
  });

  ProductionQueue copyWith({
    String? id,
    String? factoryId,
    int? recipeId,
    int? startTime,
    int? finishTime,
    bool? isCollected,
  }) {
    return ProductionQueue(
      id: id ?? this.id,
      factoryId: factoryId ?? this.factoryId,
      recipeId: recipeId ?? this.recipeId,
      startTime: startTime ?? this.startTime,
      finishTime: finishTime ?? this.finishTime,
      isCollected: isCollected ?? this.isCollected,
    );
  }

  bool get isReady => finishTime <= DateTime.now().millisecondsSinceEpoch && !isCollected;

  Map<String, dynamic> toJson() => {
        'id': id,
        'factoryId': factoryId,
        'recipeId': recipeId,
        'startTime': startTime,
        'finishTime': finishTime,
        'isCollected': isCollected,
      };

  factory ProductionQueue.fromJson(Map<String, dynamic> json) => ProductionQueue(
        id: json['id'],
        factoryId: json['factoryId'],
        recipeId: json['recipeId'],
        startTime: json['startTime'],
        finishTime: json['finishTime'],
        isCollected: json['isCollected'] ?? false,
      );
}

class InventoryItem {
  final String id;
  final String itemType;
  final int configId;
  int quantity;
  final int quality;

  InventoryItem({
    required this.id,
    required this.itemType,
    required this.configId,
    required this.quantity,
    this.quality = 0,
  });

  InventoryItem copyWith({
    String? id,
    String? itemType,
    int? configId,
    int? quantity,
    int? quality,
  }) {
    return InventoryItem(
      id: id ?? this.id,
      itemType: itemType ?? this.itemType,
      configId: configId ?? this.configId,
      quantity: quantity ?? this.quantity,
      quality: quality ?? this.quality,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'itemType': itemType,
        'configId': configId,
        'quantity': quantity,
        'quality': quality,
      };

  factory InventoryItem.fromJson(Map<String, dynamic> json) => InventoryItem(
        id: json['id'],
        itemType: json['itemType'],
        configId: json['configId'],
        quantity: json['quantity'],
        quality: json['quality'] ?? 0,
      );
}

class CollectionRecord {
  final String id;
  final String collectionType;
  final int configId;
  final int highestQuality;
  final int totalObtained;

  CollectionRecord({
    required this.id,
    required this.collectionType,
    required this.configId,
    this.highestQuality = 0,
    this.totalObtained = 0,
  });

  CollectionRecord copyWith({
    String? id,
    String? collectionType,
    int? configId,
    int? highestQuality,
    int? totalObtained,
  }) {
    return CollectionRecord(
      id: id ?? this.id,
      collectionType: collectionType ?? this.collectionType,
      configId: configId ?? this.configId,
      highestQuality: highestQuality ?? this.highestQuality,
      totalObtained: totalObtained ?? this.totalObtained,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'collectionType': collectionType,
        'configId': configId,
        'highestQuality': highestQuality,
        'totalObtained': totalObtained,
      };

  factory CollectionRecord.fromJson(Map<String, dynamic> json) => CollectionRecord(
        id: json['id'],
        collectionType: json['collectionType'],
        configId: json['configId'],
        highestQuality: json['highestQuality'] ?? 0,
        totalObtained: json['totalObtained'] ?? 0,
      );
}

class Tree {
  final String id;
  final int configId;
  final int x;
  final int y;
  final int plantedTime;
  final int nextHarvestTime;
  final int level;
  final int exp;

  Tree({
    required this.id,
    required this.configId,
    required this.x,
    required this.y,
    required this.plantedTime,
    required this.nextHarvestTime,
    this.level = 1,
    this.exp = 0,
  });

  Tree copyWith({
    String? id,
    int? configId,
    int? x,
    int? y,
    int? plantedTime,
    int? nextHarvestTime,
    int? level,
    int? exp,
  }) {
    return Tree(
      id: id ?? this.id,
      configId: configId ?? this.configId,
      x: x ?? this.x,
      y: y ?? this.y,
      plantedTime: plantedTime ?? this.plantedTime,
      nextHarvestTime: nextHarvestTime ?? this.nextHarvestTime,
      level: level ?? this.level,
      exp: exp ?? this.exp,
    );
  }

  bool get isReady => nextHarvestTime <= DateTime.now().millisecondsSinceEpoch;

  Map<String, dynamic> toJson() => {
        'id': id,
        'configId': configId,
        'x': x,
        'y': y,
        'plantedTime': plantedTime,
        'nextHarvestTime': nextHarvestTime,
        'level': level,
        'exp': exp,
      };

  factory Tree.fromJson(Map<String, dynamic> json) => Tree(
        id: json['id'],
        configId: json['configId'],
        x: json['x'],
        y: json['y'],
        plantedTime: json['plantedTime'],
        nextHarvestTime: json['nextHarvestTime'],
        level: json['level'] ?? 1,
        exp: json['exp'] ?? 0,
      );
}

class Fish {
  final String id;
  final int configId;
  final String pondId;
  final int placedTime;
  final int finishTime;
  final int quality;

  Fish({
    required this.id,
    required this.configId,
    required this.pondId,
    required this.placedTime,
    required this.finishTime,
    this.quality = 0,
  });

  Fish copyWith({
    String? id,
    int? configId,
    String? pondId,
    int? placedTime,
    int? finishTime,
    int? quality,
  }) {
    return Fish(
      id: id ?? this.id,
      configId: configId ?? this.configId,
      pondId: pondId ?? this.pondId,
      placedTime: placedTime ?? this.placedTime,
      finishTime: finishTime ?? this.finishTime,
      quality: quality ?? this.quality,
    );
  }

  bool get isReady => finishTime <= DateTime.now().millisecondsSinceEpoch;

  Map<String, dynamic> toJson() => {
        'id': id,
        'configId': configId,
        'pondId': pondId,
        'placedTime': placedTime,
        'finishTime': finishTime,
        'quality': quality,
      };

  factory Fish.fromJson(Map<String, dynamic> json) => Fish(
        id: json['id'],
        configId: json['configId'],
        pondId: json['pondId'],
        placedTime: json['placedTime'],
        finishTime: json['finishTime'],
        quality: json['quality'] ?? 0,
      );
}

class Pond {
  final String id;
  final int x;
  final int y;
  final bool unlocked;

  Pond({
    required this.id,
    required this.x,
    required this.y,
    this.unlocked = false,
  });

  Pond copyWith({
    String? id,
    int? x,
    int? y,
    bool? unlocked,
  }) {
    return Pond(
      id: id ?? this.id,
      x: x ?? this.x,
      y: y ?? this.y,
      unlocked: unlocked ?? this.unlocked,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'x': x,
        'y': y,
        'unlocked': unlocked,
      };

  factory Pond.fromJson(Map<String, dynamic> json) => Pond(
        id: json['id'],
        x: json['x'],
        y: json['y'],
        unlocked: json['unlocked'] ?? false,
      );
}

class PetFacility {
  final String id;
  final int configId;
  final int level;
  final bool isActive;

  PetFacility({
    required this.id,
    required this.configId,
    this.level = 1,
    this.isActive = true,
  });

  PetFacility copyWith({
    String? id,
    int? configId,
    int? level,
    bool? isActive,
  }) {
    return PetFacility(
      id: id ?? this.id,
      configId: configId ?? this.configId,
      level: level ?? this.level,
      isActive: isActive ?? this.isActive,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'configId': configId,
        'level': level,
        'isActive': isActive,
      };

  factory PetFacility.fromJson(Map<String, dynamic> json) => PetFacility(
        id: json['id'],
        configId: json['configId'],
        level: json['level'] ?? 1,
        isActive: json['isActive'] ?? true,
      );
}

class GameLog {
  final String id;
  final String type;
  final String message;
  final int timestamp;

  GameLog({
    required this.id,
    required this.type,
    required this.message,
    int? timestamp,
  }) : timestamp = timestamp ?? DateTime.now().millisecondsSinceEpoch;

  Map<String, dynamic> toJson() => {
        'id': id,
        'type': type,
        'message': message,
        'timestamp': timestamp,
      };

  factory GameLog.fromJson(Map<String, dynamic> json) => GameLog(
        id: json['id'],
        type: json['type'],
        message: json['message'],
        timestamp: json['timestamp'],
      );
}
