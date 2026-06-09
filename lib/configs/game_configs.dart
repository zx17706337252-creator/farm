class CropConfig {
  final int cropId;
  final String name;
  final String category;
  final int growTimeSeconds;
  final int sellPrice;
  final int exp;
  final int unlockLevel;
  final String icon;

  const CropConfig({
    required this.cropId,
    required this.name,
    required this.category,
    required this.growTimeSeconds,
    required this.sellPrice,
    required this.exp,
    required this.unlockLevel,
    required this.icon,
  });
}

class CropConfigs {
  static const List<CropConfig> all = [
    // === 基础粮食 ===
    CropConfig(cropId: 1, name: '小麦', category: 'GRAIN', growTimeSeconds: 30, sellPrice: 5, exp: 1, unlockLevel: 1, icon: '🌾'),
    CropConfig(cropId: 2, name: '玉米', category: 'GRAIN', growTimeSeconds: 120, sellPrice: 20, exp: 3, unlockLevel: 1, icon: '🌽'),
    CropConfig(cropId: 3, name: '水稻', category: 'GRAIN', growTimeSeconds: 180, sellPrice: 30, exp: 4, unlockLevel: 3, icon: '🍚'),
    CropConfig(cropId: 4, name: '高粱', category: 'GRAIN', growTimeSeconds: 240, sellPrice: 45, exp: 6, unlockLevel: 5, icon: '🌾'),
    CropConfig(cropId: 5, name: '燕麦', category: 'GRAIN', growTimeSeconds: 300, sellPrice: 55, exp: 7, unlockLevel: 7, icon: '🌾'),

    // === 蔬菜 ===
    CropConfig(cropId: 11, name: '胡萝卜', category: 'VEGETABLE', growTimeSeconds: 60, sellPrice: 10, exp: 2, unlockLevel: 1, icon: '🥕'),
    CropConfig(cropId: 12, name: '土豆', category: 'VEGETABLE', growTimeSeconds: 300, sellPrice: 40, exp: 5, unlockLevel: 2, icon: '🥔'),
    CropConfig(cropId: 13, name: '番茄', category: 'VEGETABLE', growTimeSeconds: 900, sellPrice: 120, exp: 15, unlockLevel: 5, icon: '🍅'),
    CropConfig(cropId: 14, name: '黄瓜', category: 'VEGETABLE', growTimeSeconds: 480, sellPrice: 60, exp: 8, unlockLevel: 6, icon: '🥒'),
    CropConfig(cropId: 15, name: '南瓜', category: 'VEGETABLE', growTimeSeconds: 1200, sellPrice: 180, exp: 20, unlockLevel: 8, icon: '🎃'),
    CropConfig(cropId: 16, name: '洋葱', category: 'VEGETABLE', growTimeSeconds: 360, sellPrice: 50, exp: 6, unlockLevel: 4, icon: '🧅'),
    CropConfig(cropId: 17, name: '白萝卜', category: 'VEGETABLE', growTimeSeconds: 420, sellPrice: 55, exp: 7, unlockLevel: 5, icon: '🥬'),
    CropConfig(cropId: 18, name: '生菜', category: 'VEGETABLE', growTimeSeconds: 240, sellPrice: 35, exp: 5, unlockLevel: 3, icon: '🥬'),
    CropConfig(cropId: 19, name: '卷心菜', category: 'VEGETABLE', growTimeSeconds: 600, sellPrice: 80, exp: 10, unlockLevel: 7, icon: '🥬'),
    CropConfig(cropId: 20, name: '辣椒', category: 'VEGETABLE', growTimeSeconds: 720, sellPrice: 100, exp: 12, unlockLevel: 9, icon: '🌶️'),

    // === 水果 ===
    CropConfig(cropId: 21, name: '草莓', category: 'FRUIT', growTimeSeconds: 600, sellPrice: 80, exp: 10, unlockLevel: 4, icon: '🍓'),
    CropConfig(cropId: 22, name: '蓝莓', category: 'FRUIT', growTimeSeconds: 1800, sellPrice: 300, exp: 25, unlockLevel: 8, icon: '🫐'),
    CropConfig(cropId: 23, name: '西瓜', category: 'FRUIT', growTimeSeconds: 3600, sellPrice: 500, exp: 40, unlockLevel: 12, icon: '🍉'),
    CropConfig(cropId: 24, name: '哈密瓜', category: 'FRUIT', growTimeSeconds: 4200, sellPrice: 600, exp: 50, unlockLevel: 14, icon: '🍈'),
    CropConfig(cropId: 25, name: '葡萄', category: 'FRUIT', growTimeSeconds: 7200, sellPrice: 1200, exp: 100, unlockLevel: 15, icon: '🍇'),
    CropConfig(cropId: 26, name: '苹果', category: 'FRUIT', growTimeSeconds: 5400, sellPrice: 800, exp: 70, unlockLevel: 18, icon: '🍎'),
    CropConfig(cropId: 27, name: '梨', category: 'FRUIT', growTimeSeconds: 5400, sellPrice: 820, exp: 72, unlockLevel: 19, icon: '🍐'),
    CropConfig(cropId: 28, name: '樱桃', category: 'FRUIT', growTimeSeconds: 4800, sellPrice: 700, exp: 60, unlockLevel: 20, icon: '🍒'),
    CropConfig(cropId: 29, name: '桃子', category: 'FRUIT', growTimeSeconds: 6000, sellPrice: 900, exp: 80, unlockLevel: 22, icon: '🍑'),
    CropConfig(cropId: 30, name: '柠檬', category: 'FRUIT', growTimeSeconds: 6600, sellPrice: 950, exp: 85, unlockLevel: 25, icon: '🍋'),

    // === 经济作物 ===
    CropConfig(cropId: 31, name: '咖啡豆', category: 'CASH', growTimeSeconds: 3600, sellPrice: 600, exp: 50, unlockLevel: 10, icon: '☕'),
    CropConfig(cropId: 32, name: '茶叶', category: 'CASH', growTimeSeconds: 4800, sellPrice: 850, exp: 70, unlockLevel: 15, icon: '🍵'),
    CropConfig(cropId: 33, name: '可可豆', category: 'CASH', growTimeSeconds: 5400, sellPrice: 950, exp: 80, unlockLevel: 20, icon: '🍫'),
    CropConfig(cropId: 34, name: '甘蔗', category: 'CASH', growTimeSeconds: 3000, sellPrice: 500, exp: 40, unlockLevel: 12, icon: '🎋'),
    CropConfig(cropId: 35, name: '棉花', category: 'CASH', growTimeSeconds: 4200, sellPrice: 700, exp: 55, unlockLevel: 18, icon: '☁️'),

    // === 高级作物 ===
    CropConfig(cropId: 41, name: '松露菌', category: 'ADVANCED', growTimeSeconds: 14400, sellPrice: 3000, exp: 200, unlockLevel: 30, icon: '🍄'),
    CropConfig(cropId: 42, name: '人参', category: 'ADVANCED', growTimeSeconds: 28800, sellPrice: 5000, exp: 300, unlockLevel: 35, icon: '🌿'),
    CropConfig(cropId: 43, name: '灵芝', category: 'ADVANCED', growTimeSeconds: 36000, sellPrice: 7000, exp: 400, unlockLevel: 40, icon: '🍄'),
    CropConfig(cropId: 44, name: '藏红花', category: 'ADVANCED', growTimeSeconds: 21600, sellPrice: 4500, exp: 280, unlockLevel: 38, icon: '🌸'),
    CropConfig(cropId: 45, name: '香草荚', category: 'ADVANCED', growTimeSeconds: 18000, sellPrice: 3800, exp: 250, unlockLevel: 32, icon: '🌱'),

    // === 花卉 ===
    CropConfig(cropId: 51, name: '玫瑰', category: 'FLOWER', growTimeSeconds: 1800, sellPrice: 350, exp: 30, unlockLevel: 12, icon: '🌹'),
    CropConfig(cropId: 52, name: '郁金香', category: 'FLOWER', growTimeSeconds: 2400, sellPrice: 450, exp: 35, unlockLevel: 14, icon: '🌷'),
    CropConfig(cropId: 53, name: '百合', category: 'FLOWER', growTimeSeconds: 3000, sellPrice: 550, exp: 45, unlockLevel: 16, icon: '🌼'),
    CropConfig(cropId: 54, name: '向日葵', category: 'FLOWER', growTimeSeconds: 3600, sellPrice: 650, exp: 50, unlockLevel: 18, icon: '🌻'),
    CropConfig(cropId: 55, name: '薰衣草', category: 'FLOWER', growTimeSeconds: 4200, sellPrice: 750, exp: 60, unlockLevel: 20, icon: '💜'),

    // === 特殊作物 ===
    CropConfig(cropId: 61, name: '金麦穗', category: 'SPECIAL', growTimeSeconds: 7200, sellPrice: 2000, exp: 150, unlockLevel: 25, icon: '🌟'),
    CropConfig(cropId: 62, name: '彩虹莓', category: 'SPECIAL', growTimeSeconds: 10800, sellPrice: 3500, exp: 220, unlockLevel: 40, icon: '🌈'),
    CropConfig(cropId: 63, name: '月光花', category: 'SPECIAL', growTimeSeconds: 14400, sellPrice: 4500, exp: 280, unlockLevel: 45, icon: '🌙'),
    CropConfig(cropId: 64, name: '星辰果', category: 'SPECIAL', growTimeSeconds: 18000, sellPrice: 5500, exp: 350, unlockLevel: 50, icon: '⭐'),
    CropConfig(cropId: 65, name: '翡翠瓜', category: 'SPECIAL', growTimeSeconds: 12600, sellPrice: 4000, exp: 260, unlockLevel: 42, icon: '💎'),

    // === 传奇作物 ===
    CropConfig(cropId: 71, name: '凤凰果', category: 'LEGENDARY', growTimeSeconds: 43200, sellPrice: 15000, exp: 800, unlockLevel: 55, icon: '🔥'),
    CropConfig(cropId: 72, name: '龙血藤', category: 'LEGENDARY', growTimeSeconds: 54000, sellPrice: 20000, exp: 1000, unlockLevel: 60, icon: '🐉'),
    CropConfig(cropId: 73, name: '生命花', category: 'LEGENDARY', growTimeSeconds: 64800, sellPrice: 25000, exp: 1200, unlockLevel: 65, icon: '💚'),
    CropConfig(cropId: 74, name: '天空树果实', category: 'LEGENDARY', growTimeSeconds: 72000, sellPrice: 30000, exp: 1500, unlockLevel: 70, icon: '🌳'),
    CropConfig(cropId: 75, name: '永恒之种', category: 'LEGENDARY', growTimeSeconds: 86400, sellPrice: 50000, exp: 2000, unlockLevel: 80, icon: '✨'),
  ];

  static CropConfig? getById(int id) {
    try {
      return all.firstWhere((c) => c.cropId == id);
    } catch (_) {
      return null;
    }
  }

  static List<CropConfig> getByCategory(String category) {
    return all.where((c) => c.category == category).toList();
  }

  static List<CropConfig> getByUnlockLevel(int level) {
    return all.where((c) => c.unlockLevel <= level).toList();
  }
}

class AnimalConfig {
  final int animalId;
  final String name;
  final String category;
  final String productName;
  final int productTimeSeconds;
  final int productSellPrice;
  final int exp;
  final int unlockLevel;
  final int purchasePrice;
  final String icon;

  const AnimalConfig({
    required this.animalId,
    required this.name,
    required this.category,
    required this.productName,
    required this.productTimeSeconds,
    required this.productSellPrice,
    required this.exp,
    required this.unlockLevel,
    required this.purchasePrice,
    required this.icon,
  });
}

class AnimalConfigs {
  static const List<AnimalConfig> all = [
    // === 初级动物 ===
    AnimalConfig(animalId: 1, name: '母鸡', category: 'BASIC', productName: '鸡蛋', productTimeSeconds: 300, productSellPrice: 15, exp: 5, unlockLevel: 10, purchasePrice: 200, icon: '🐔'),
    AnimalConfig(animalId: 2, name: '奶牛', category: 'BASIC', productName: '牛奶', productTimeSeconds: 900, productSellPrice: 40, exp: 10, unlockLevel: 10, purchasePrice: 800, icon: '🐄'),
    AnimalConfig(animalId: 3, name: '绵羊', category: 'BASIC', productName: '羊毛', productTimeSeconds: 1200, productSellPrice: 60, exp: 15, unlockLevel: 10, purchasePrice: 1000, icon: '🐑'),
    AnimalConfig(animalId: 4, name: '山羊', category: 'BASIC', productName: '羊奶', productTimeSeconds: 1500, productSellPrice: 80, exp: 20, unlockLevel: 12, purchasePrice: 1200, icon: '🐐'),
    AnimalConfig(animalId: 5, name: '蜜蜂', category: 'BASIC', productName: '蜂蜜', productTimeSeconds: 1800, productSellPrice: 120, exp: 25, unlockLevel: 15, purchasePrice: 1500, icon: '🐝'),

    // === 中级动物 ===
    AnimalConfig(animalId: 11, name: '鸭子', category: 'MID', productName: '鸭蛋', productTimeSeconds: 600, productSellPrice: 25, exp: 8, unlockLevel: 15, purchasePrice: 400, icon: '🦆'),
    AnimalConfig(animalId: 12, name: '火鸡', category: 'MID', productName: '火鸡蛋', productTimeSeconds: 1200, productSellPrice: 60, exp: 15, unlockLevel: 18, purchasePrice: 800, icon: '🦃'),
    AnimalConfig(animalId: 13, name: '兔子', category: 'MID', productName: '兔毛', productTimeSeconds: 1800, productSellPrice: 90, exp: 20, unlockLevel: 20, purchasePrice: 1000, icon: '🐰'),
    AnimalConfig(animalId: 14, name: '猪', category: 'MID', productName: '松露', productTimeSeconds: 3600, productSellPrice: 250, exp: 50, unlockLevel: 22, purchasePrice: 2500, icon: '🐷'),
    AnimalConfig(animalId: 15, name: '鹅', category: 'MID', productName: '鹅蛋', productTimeSeconds: 1500, productSellPrice: 70, exp: 18, unlockLevel: 20, purchasePrice: 900, icon: '🦢'),

    // === 高级动物 ===
    AnimalConfig(animalId: 21, name: '奶水牛', category: 'ADVANCED', productName: '高级牛奶', productTimeSeconds: 2400, productSellPrice: 180, exp: 35, unlockLevel: 30, purchasePrice: 5000, icon: '🐃'),
    AnimalConfig(animalId: 22, name: '骆驼', category: 'ADVANCED', productName: '驼奶', productTimeSeconds: 3000, productSellPrice: 220, exp: 45, unlockLevel: 32, purchasePrice: 6000, icon: '🐪'),
    AnimalConfig(animalId: 23, name: '梅花鹿', category: 'ADVANCED', productName: '鹿茸', productTimeSeconds: 5400, productSellPrice: 500, exp: 80, unlockLevel: 35, purchasePrice: 8000, icon: '🦌'),
    AnimalConfig(animalId: 24, name: '羊驼', category: 'ADVANCED', productName: '羊驼毛', productTimeSeconds: 4800, productSellPrice: 450, exp: 75, unlockLevel: 38, purchasePrice: 7500, icon: '🦙'),
    AnimalConfig(animalId: 25, name: '孔雀', category: 'ADVANCED', productName: '孔雀羽毛', productTimeSeconds: 7200, productSellPrice: 800, exp: 120, unlockLevel: 42, purchasePrice: 12000, icon: '🦚'),

    // === 特殊动物 ===
    AnimalConfig(animalId: 31, name: '黑天鹅', category: 'SPECIAL', productName: '黑天鹅羽毛', productTimeSeconds: 10800, productSellPrice: 1500, exp: 200, unlockLevel: 45, purchasePrice: 20000, icon: '🦢'),
    AnimalConfig(animalId: 32, name: '金鸡', category: 'SPECIAL', productName: '金蛋', productTimeSeconds: 14400, productSellPrice: 3000, exp: 350, unlockLevel: 50, purchasePrice: 30000, icon: '🐓'),
    AnimalConfig(animalId: 33, name: '银羊', category: 'SPECIAL', productName: '银羊毛', productTimeSeconds: 10800, productSellPrice: 2000, exp: 250, unlockLevel: 52, purchasePrice: 25000, icon: '🐏'),
    AnimalConfig(animalId: 34, name: '彩虹牛', category: 'SPECIAL', productName: '彩虹牛奶', productTimeSeconds: 18000, productSellPrice: 5000, exp: 500, unlockLevel: 55, purchasePrice: 50000, icon: '🐮'),
    AnimalConfig(animalId: 35, name: '凤凰鸟', category: 'SPECIAL', productName: '凤凰羽', productTimeSeconds: 36000, productSellPrice: 15000, exp: 1000, unlockLevel: 60, purchasePrice: 100000, icon: '🔥'),
  ];

  static AnimalConfig? getById(int id) {
    try {
      return all.firstWhere((a) => a.animalId == id);
    } catch (_) {
      return null;
    }
  }

  static List<AnimalConfig> getByUnlockLevel(int level) {
    return all.where((a) => a.unlockLevel <= level).toList();
  }

  static List<AnimalConfig> getByCategory(String category) {
    return all.where((a) => a.category == category).toList();
  }
}

class TreeConfig {
  final int treeId;
  final String name;
  final int growTimeHours;
  final int harvestTimeHours;
  final int sellPrice;
  final int exp;
  final int unlockLevel;
  final String icon;

  const TreeConfig({
    required this.treeId,
    required this.name,
    required this.growTimeHours,
    required this.harvestTimeHours,
    required this.sellPrice,
    required this.exp,
    required this.unlockLevel,
    required this.icon,
  });
}

class TreeConfigs {
  static const List<TreeConfig> all = [
    TreeConfig(treeId: 1, name: '苹果树', growTimeHours: 24, harvestTimeHours: 8, sellPrice: 500, exp: 40, unlockLevel: 20, icon: '🍎'),
    TreeConfig(treeId: 2, name: '樱桃树', growTimeHours: 36, harvestTimeHours: 6, sellPrice: 800, exp: 60, unlockLevel: 22, icon: '🍒'),
    TreeConfig(treeId: 3, name: '梨树', growTimeHours: 48, harvestTimeHours: 8, sellPrice: 600, exp: 50, unlockLevel: 21, icon: '🍐'),
    TreeConfig(treeId: 4, name: '桃树', growTimeHours: 30, harvestTimeHours: 7, sellPrice: 700, exp: 55, unlockLevel: 23, icon: '🍑'),
    TreeConfig(treeId: 5, name: '橘子树', growTimeHours: 36, harvestTimeHours: 6, sellPrice: 650, exp: 52, unlockLevel: 24, icon: '🍊'),
    TreeConfig(treeId: 6, name: '柠檬树', growTimeHours: 42, harvestTimeHours: 8, sellPrice: 750, exp: 58, unlockLevel: 25, icon: '🍋'),
    TreeConfig(treeId: 7, name: '芒果树', growTimeHours: 60, harvestTimeHours: 10, sellPrice: 1200, exp: 80, unlockLevel: 28, icon: '🥭'),
    TreeConfig(treeId: 8, name: '椰子树', growTimeHours: 72, harvestTimeHours: 12, sellPrice: 1500, exp: 100, unlockLevel: 30, icon: '🥥'),
    TreeConfig(treeId: 9, name: '蓝莓树', growTimeHours: 30, harvestTimeHours: 6, sellPrice: 400, exp: 35, unlockLevel: 20, icon: '🫐'),
    TreeConfig(treeId: 10, name: '葡萄树', growTimeHours: 48, harvestTimeHours: 8, sellPrice: 900, exp: 65, unlockLevel: 26, icon: '🍇'),
  ];

  static TreeConfig? getById(int id) {
    try {
      return all.firstWhere((t) => t.treeId == id);
    } catch (_) {
      return null;
    }
  }
}

class FishConfig {
  final int fishId;
  final String name;
  final int growTimeMinutes;
  final int sellPrice;
  final int exp;
  final int unlockLevel;
  final String icon;

  const FishConfig({
    required this.fishId,
    required this.name,
    required this.growTimeMinutes,
    required this.sellPrice,
    required this.exp,
    required this.unlockLevel,
    required this.icon,
  });
}

class FishConfigs {
  static const List<FishConfig> all = [
    FishConfig(fishId: 1, name: '鲫鱼', growTimeMinutes: 30, sellPrice: 50, exp: 5, unlockLevel: 40, icon: '🐟'),
    FishConfig(fishId: 2, name: '鲤鱼', growTimeMinutes: 45, sellPrice: 80, exp: 8, unlockLevel: 42, icon: '🐠'),
    FishConfig(fishId: 3, name: '草鱼', growTimeMinutes: 60, sellPrice: 120, exp: 12, unlockLevel: 44, icon: '🐡'),
    FishConfig(fishId: 4, name: '鲈鱼', growTimeMinutes: 90, sellPrice: 200, exp: 18, unlockLevel: 46, icon: '🐟'),
    FishConfig(fishId: 5, name: '金鱼', growTimeMinutes: 120, sellPrice: 300, exp: 25, unlockLevel: 48, icon: '🐠'),
    FishConfig(fishId: 6, name: '锦鲤', growTimeMinutes: 180, sellPrice: 500, exp: 40, unlockLevel: 50, icon: '🐟'),
    FishConfig(fishId: 7, name: '龙鱼', growTimeMinutes: 300, sellPrice: 1000, exp: 80, unlockLevel: 55, icon: '🐉'),
    FishConfig(fishId: 8, name: '金鱼王', growTimeMinutes: 480, sellPrice: 2000, exp: 150, unlockLevel: 60, icon: '👑'),
  ];

  static FishConfig? getById(int id) {
    try {
      return all.firstWhere((f) => f.fishId == id);
    } catch (_) {
      return null;
    }
  }
}

class PetConfig {
  final int petId;
  final String name;
  final String category;
  final String primaryWorkType;
  final double baseSpeed;
  final int baseRange;
  final int unlockLevel;
  final int purchasePrice;
  final String icon;
  final String description;

  const PetConfig({
    required this.petId,
    required this.name,
    required this.category,
    required this.primaryWorkType,
    required this.baseSpeed,
    required this.baseRange,
    required this.unlockLevel,
    required this.purchasePrice,
    required this.icon,
    required this.description,
  });
}

class PetConfigs {
  static const List<PetConfig> all = [
    // === 基础宠物 ===
    PetConfig(petId: 1, name: '柴犬', category: 'BASIC', primaryWorkType: 'HARVEST', baseSpeed: 1.0, baseRange: 5, unlockLevel: 1, purchasePrice: 1000, icon: '🐕', description: '自动收菜，勤劳的小助手'),
    PetConfig(petId: 2, name: '橘猫', category: 'BASIC', primaryWorkType: 'COLLECT', baseSpeed: 1.1, baseRange: 4, unlockLevel: 5, purchasePrice: 1500, icon: '🐱', description: '自动收集鸡蛋，灵活敏捷'),
    PetConfig(petId: 3, name: '布偶猫', category: 'BASIC', primaryWorkType: 'COLLECT', baseSpeed: 1.0, baseRange: 4, unlockLevel: 8, purchasePrice: 2000, icon: '😺', description: '自动收集牛奶，温柔细致'),
    PetConfig(petId: 4, name: '金毛犬', category: 'BASIC', primaryWorkType: 'CARRY', baseSpeed: 1.2, baseRange: 8, unlockLevel: 10, purchasePrice: 2500, icon: '🦮', description: '搬运仓库，力大无穷'),
    PetConfig(petId: 5, name: '边境牧羊犬', category: 'BASIC', primaryWorkType: 'FEED', baseSpeed: 1.1, baseRange: 10, unlockLevel: 12, purchasePrice: 3000, icon: '🐶', description: '管理养殖区，聪明伶俐'),

    // === 进阶宠物 ===
    PetConfig(petId: 11, name: '柯基', category: 'ADVANCED', primaryWorkType: 'HARVEST', baseSpeed: 1.3, baseRange: 6, unlockLevel: 15, purchasePrice: 4000, icon: '🐕‍🦺', description: '加快收获速度，短腿大能量'),
    PetConfig(petId: 12, name: '哈士奇', category: 'ADVANCED', primaryWorkType: 'EXPLORE', baseSpeed: 1.4, baseRange: 15, unlockLevel: 18, purchasePrice: 5000, icon: '🐺', description: '探险专家，精力充沛'),
    PetConfig(petId: 13, name: '德牧', category: 'ADVANCED', primaryWorkType: 'PROCESS', baseSpeed: 1.3, baseRange: 12, unlockLevel: 20, purchasePrice: 6000, icon: '🐕', description: '区域管理，严肃认真'),
    PetConfig(petId: 14, name: '狸花猫', category: 'ADVANCED', primaryWorkType: 'COLLECT', baseSpeed: 1.2, baseRange: 8, unlockLevel: 22, purchasePrice: 5500, icon: '🐈', description: '寻找稀有掉落，野性敏锐'),
    PetConfig(petId: 15, name: '缅因猫', category: 'ADVANCED', primaryWorkType: 'CARRY', baseSpeed: 1.3, baseRange: 10, unlockLevel: 25, purchasePrice: 7000, icon: '🐈‍⬛', description: '仓库管理，沉稳可靠'),

    // === 高级宠物 ===
    PetConfig(petId: 21, name: '白狐', category: 'HIGH', primaryWorkType: 'HARVEST', baseSpeed: 1.5, baseRange: 12, unlockLevel: 30, purchasePrice: 15000, icon: '🦊', description: '幸运加成，带来好运'),
    PetConfig(petId: 22, name: '雪狐', category: 'HIGH', primaryWorkType: 'PROCESS', baseSpeed: 1.5, baseRange: 12, unlockLevel: 32, purchasePrice: 18000, icon: '❄️', description: '品质加成，冰雪聪明'),
    PetConfig(petId: 23, name: '猫头鹰', category: 'HIGH', primaryWorkType: 'EXPLORE', baseSpeed: 1.4, baseRange: 20, unlockLevel: 35, purchasePrice: 20000, icon: '🦉', description: '夜间探险，夜视超群'),
    PetConfig(petId: 24, name: '乌鸦', category: 'HIGH', primaryWorkType: 'COLLECT', baseSpeed: 1.5, baseRange: 15, unlockLevel: 38, purchasePrice: 22000, icon: '🐦', description: '稀有材料发现，眼光独到'),
    PetConfig(petId: 25, name: '猎鹰', category: 'HIGH', primaryWorkType: 'CARRY', baseSpeed: 1.8, baseRange: 25, unlockLevel: 40, purchasePrice: 25000, icon: '🦅', description: '快速运输，风驰电掣'),

    // === 传说宠物 ===
    PetConfig(petId: 31, name: '凤凰', category: 'LEGEND', primaryWorkType: 'PROCESS', baseSpeed: 2.0, baseRange: 30, unlockLevel: 45, purchasePrice: 80000, icon: '🔥', description: '全局生产加速，浴火重生'),
    PetConfig(petId: 32, name: '青龙', category: 'LEGEND', primaryWorkType: 'HARVEST', baseSpeed: 2.0, baseRange: 35, unlockLevel: 48, purchasePrice: 100000, icon: '🐉', description: '成长加速，东方守护者'),
    PetConfig(petId: 33, name: '白虎', category: 'LEGEND', primaryWorkType: 'HARVEST', baseSpeed: 2.0, baseRange: 35, unlockLevel: 50, purchasePrice: 120000, icon: '🐅', description: '幸运提升，西方战神'),
    PetConfig(petId: 34, name: '玄武', category: 'LEGEND', primaryWorkType: 'CARRY', baseSpeed: 2.0, baseRange: 40, unlockLevel: 52, purchasePrice: 150000, icon: '🐢', description: '仓库容量提升，北方守护神'),
    PetConfig(petId: 35, name: '麒麟', category: 'LEGEND', primaryWorkType: 'FEED', baseSpeed: 2.2, baseRange: 30, unlockLevel: 55, purchasePrice: 180000, icon: '🦄', description: '综合辅助，祥瑞之兽'),

    // === 神话宠物 ===
    PetConfig(petId: 41, name: '星灵猫', category: 'MYTH', primaryWorkType: 'HARVEST', baseSpeed: 2.5, baseRange: 50, unlockLevel: 60, purchasePrice: 300000, icon: '⭐', description: '全图自动收获，星辰之灵'),
    PetConfig(petId: 42, name: '月神狐', category: 'MYTH', primaryWorkType: 'PLANT', baseSpeed: 2.5, baseRange: 50, unlockLevel: 62, purchasePrice: 350000, icon: '🌙', description: '全图自动播种，月光化身'),
    PetConfig(petId: 43, name: '森林精灵', category: 'MYTH', primaryWorkType: 'WATER', baseSpeed: 2.5, baseRange: 50, unlockLevel: 65, purchasePrice: 400000, icon: '🧚', description: '自动浇水，自然之力'),
    PetConfig(petId: 44, name: '机械犬', category: 'MYTH', primaryWorkType: 'CARRY', baseSpeed: 3.0, baseRange: 60, unlockLevel: 68, purchasePrice: 450000, icon: '🤖', description: '自动运输，钢铁意志'),
    PetConfig(petId: 45, name: '机械猫', category: 'MYTH', primaryWorkType: 'COLLECT', baseSpeed: 3.0, baseRange: 60, unlockLevel: 70, purchasePrice: 480000, icon: '🤖', description: '自动仓储，精密计算'),
    PetConfig(petId: 46, name: '云雀', category: 'MYTH', primaryWorkType: 'EXPLORE', baseSpeed: 2.8, baseRange: 80, unlockLevel: 72, purchasePrice: 500000, icon: '☁️', description: '天气控制，云端飞翔'),
    PetConfig(petId: 47, name: '小龙', category: 'MYTH', primaryWorkType: 'PROCESS', baseSpeed: 3.0, baseRange: 50, unlockLevel: 75, purchasePrice: 600000, icon: '🐲', description: '自动加工，幼年神龙'),
    PetConfig(petId: 48, name: '独角兽', category: 'MYTH', primaryWorkType: 'HARVEST', baseSpeed: 3.0, baseRange: 60, unlockLevel: 78, purchasePrice: 700000, icon: '🦄', description: '传奇品质提升，圣洁生灵'),
    PetConfig(petId: 49, name: '光辉凤凰', category: 'MYTH', primaryWorkType: 'PROCESS', baseSpeed: 3.2, baseRange: 70, unlockLevel: 80, purchasePrice: 800000, icon: '🌟', description: '生产效率提升，涅槃重生'),
    PetConfig(petId: 50, name: '世界树精灵', category: 'MYTH', primaryWorkType: 'PROCESS', baseSpeed: 4.0, baseRange: 100, unlockLevel: 90, purchasePrice: 2000000, icon: '🌳', description: '全农场管理，世界树化身'),
  ];

  static PetConfig? getById(int id) {
    try {
      return all.firstWhere((p) => p.petId == id);
    } catch (_) {
      return null;
    }
  }

  static List<PetConfig> getByUnlockLevel(int level) {
    return all.where((p) => p.unlockLevel <= level).toList();
  }
}

class FactoryConfig {
  final int factoryId;
  final String name;
  final String category;
  final int unlockLevel;
  final int baseQueueSize;
  final int purchasePrice;
  final String icon;
  final String description;

  const FactoryConfig({
    required this.factoryId,
    required this.name,
    required this.category,
    required this.unlockLevel,
    required this.baseQueueSize,
    required this.purchasePrice,
    required this.icon,
    required this.description,
  });
}

class RecipeConfig {
  final int recipeId;
  final String name;
  final String icon;
  final int factoryId;
  final String inputItemType;
  final int inputConfigId;
  final int inputQuantity;
  final int outputSellPrice;
  final int outputExp;
  final int processTimeSeconds;

  const RecipeConfig({
    required this.recipeId,
    required this.name,
    required this.icon,
    required this.factoryId,
    required this.inputItemType,
    required this.inputConfigId,
    required this.inputQuantity,
    required this.outputSellPrice,
    required this.outputExp,
    required this.processTimeSeconds,
  });
}

class FactoryConfigs {
  static const List<FactoryConfig> all = [
    // === 一级建筑 ===
    FactoryConfig(factoryId: 1, name: '面粉坊', category: 'TIER1', unlockLevel: 30, baseQueueSize: 1, purchasePrice: 2000, icon: '🌾', description: '将小麦加工成面粉'),
    FactoryConfig(factoryId: 2, name: '乳品厂', category: 'TIER1', unlockLevel: 30, baseQueueSize: 1, purchasePrice: 3000, icon: '🥛', description: '将牛奶加工成乳制品'),
    FactoryConfig(factoryId: 3, name: '榨油坊', category: 'TIER1', unlockLevel: 32, baseQueueSize: 1, purchasePrice: 3500, icon: '🫒', description: '压榨植物油'),
    FactoryConfig(factoryId: 4, name: '糖厂', category: 'TIER1', unlockLevel: 34, baseQueueSize: 1, purchasePrice: 4000, icon: '🍯', description: '将甘蔗加工成糖'),
    FactoryConfig(factoryId: 5, name: '纺织坊', category: 'TIER1', unlockLevel: 35, baseQueueSize: 1, purchasePrice: 4500, icon: '🧵', description: '将棉花加工成布料'),

    // === 二级建筑 ===
    FactoryConfig(factoryId: 11, name: '面包坊', category: 'TIER2', unlockLevel: 35, baseQueueSize: 2, purchasePrice: 8000, icon: '🍞', description: '烘焙面包和三明治'),
    FactoryConfig(factoryId: 12, name: '果酱坊', category: 'TIER2', unlockLevel: 36, baseQueueSize: 2, purchasePrice: 9000, icon: '🍯', description: '制作果酱'),
    FactoryConfig(factoryId: 13, name: '甜品屋', category: 'TIER2', unlockLevel: 38, baseQueueSize: 2, purchasePrice: 10000, icon: '🍰', description: '制作高级甜品'),
    FactoryConfig(factoryId: 14, name: '咖啡馆', category: 'TIER2', unlockLevel: 40, baseQueueSize: 2, purchasePrice: 12000, icon: '☕', description: '精品咖啡制作'),
    FactoryConfig(factoryId: 15, name: '茶工坊', category: 'TIER2', unlockLevel: 42, baseQueueSize: 2, purchasePrice: 12000, icon: '🍵', description: '精品茶叶加工'),

    // === 三级建筑 ===
    FactoryConfig(factoryId: 21, name: '高级餐厅', category: 'TIER3', unlockLevel: 45, baseQueueSize: 3, purchasePrice: 30000, icon: '🍽️', description: '高级料理，订单大户'),
    FactoryConfig(factoryId: 22, name: '香水工坊', category: 'TIER3', unlockLevel: 48, baseQueueSize: 3, purchasePrice: 35000, icon: '🌸', description: '花卉加工成香水'),
    FactoryConfig(factoryId: 23, name: '巧克力工厂', category: 'TIER3', unlockLevel: 50, baseQueueSize: 3, purchasePrice: 40000, icon: '🍫', description: '精品巧克力'),
    FactoryConfig(factoryId: 24, name: '蜂蜜工坊', category: 'TIER3', unlockLevel: 52, baseQueueSize: 3, purchasePrice: 45000, icon: '🍯', description: '皇家蜂蜜制品'),
    FactoryConfig(factoryId: 25, name: '收藏工坊', category: 'TIER3', unlockLevel: 60, baseQueueSize: 3, purchasePrice: 100000, icon: '🏛️', description: '传奇收藏品加工'),
  ];

  static const List<RecipeConfig> recipes = [
    // 面粉坊
    RecipeConfig(recipeId: 1, name: '面粉', icon: '🌾', factoryId: 1, inputItemType: 'CROP', inputConfigId: 1, inputQuantity: 3, outputSellPrice: 20, outputExp: 3, processTimeSeconds: 30),
    // 乳品厂
    RecipeConfig(recipeId: 11, name: '奶酪', icon: '🧀', factoryId: 2, inputItemType: 'ANIMAL', inputConfigId: 2, inputQuantity: 2, outputSellPrice: 120, outputExp: 15, processTimeSeconds: 300),
    RecipeConfig(recipeId: 12, name: '黄油', icon: '🧈', factoryId: 2, inputItemType: 'ANIMAL', inputConfigId: 2, inputQuantity: 3, outputSellPrice: 300, outputExp: 25, processTimeSeconds: 600),
    // 面包坊
    RecipeConfig(recipeId: 21, name: '面包', icon: '🍞', factoryId: 11, inputItemType: 'PROCESSED', inputConfigId: 1, inputQuantity: 2, outputSellPrice: 80, outputExp: 10, processTimeSeconds: 120),
    RecipeConfig(recipeId: 22, name: '三明治', icon: '🥪', factoryId: 11, inputItemType: 'PROCESSED', inputConfigId: 1, inputQuantity: 4, outputSellPrice: 250, outputExp: 30, processTimeSeconds: 300),
    // 果酱坊
    RecipeConfig(recipeId: 31, name: '草莓酱', icon: '🍯', factoryId: 12, inputItemType: 'CROP', inputConfigId: 21, inputQuantity: 3, outputSellPrice: 200, outputExp: 20, processTimeSeconds: 300),
    RecipeConfig(recipeId: 32, name: '蓝莓酱', icon: '🫐', factoryId: 12, inputItemType: 'CROP', inputConfigId: 22, inputQuantity: 2, outputSellPrice: 500, outputExp: 40, processTimeSeconds: 600),
    // 咖啡馆
    RecipeConfig(recipeId: 41, name: '咖啡', icon: '☕', factoryId: 14, inputItemType: 'CROP', inputConfigId: 31, inputQuantity: 2, outputSellPrice: 2500, outputExp: 80, processTimeSeconds: 1800),
    // 蜂蜜工坊
    RecipeConfig(recipeId: 51, name: '蜂蜜糖浆', icon: '🍯', factoryId: 24, inputItemType: 'ANIMAL', inputConfigId: 5, inputQuantity: 2, outputSellPrice: 300, outputExp: 30, processTimeSeconds: 600),
    RecipeConfig(recipeId: 52, name: '蜂蜜蛋糕', icon: '🍰', factoryId: 24, inputItemType: 'ANIMAL', inputConfigId: 5, inputQuantity: 3, outputSellPrice: 800, outputExp: 60, processTimeSeconds: 1200),
  ];

  static FactoryConfig? getFactoryById(int id) {
    try {
      return all.firstWhere((f) => f.factoryId == id);
    } catch (_) {
      return null;
    }
  }

  static List<RecipeConfig> getRecipesByFactory(int factoryId) {
    return recipes.where((r) => r.factoryId == factoryId).toList();
  }

  static RecipeConfig? getRecipeById(int id) {
    try {
      return recipes.firstWhere((r) => r.recipeId == id);
    } catch (_) {
      return null;
    }
  }

  static List<FactoryConfig> getByUnlockLevel(int level) {
    return all.where((f) => f.unlockLevel <= level).toList();
  }
}

class PetFacilityConfig {
  final int facilityId;
  final String name;
  final String description;
  final int unlockLevel;
  final int buildCost;
  final String icon;

  const PetFacilityConfig({
    required this.facilityId,
    required this.name,
    required this.description,
    required this.unlockLevel,
    required this.buildCost,
    required this.icon,
  });
}

class PetFacilityConfigs {
  static const List<PetFacilityConfig> all = [
    PetFacilityConfig(facilityId: 1, name: '宠物宿舍', description: '宠物休息恢复体力', unlockLevel: 50, buildCost: 5000, icon: '🏠'),
    PetFacilityConfig(facilityId: 2, name: '训练场', description: '宠物训练提升等级', unlockLevel: 52, buildCost: 8000, icon: '🎾'),
    PetFacilityConfig(facilityId: 3, name: '宠物医院', description: '治疗疲劳宠物', unlockLevel: 54, buildCost: 6000, icon: '🏥'),
    PetFacilityConfig(facilityId: 4, name: '游乐场', description: '游玩提升亲密度', unlockLevel: 56, buildCost: 10000, icon: '🎠'),
  ];

  static PetFacilityConfig? getById(int id) {
    try {
      return all.firstWhere((f) => f.facilityId == id);
    } catch (_) {
      return null;
    }
  }
}
