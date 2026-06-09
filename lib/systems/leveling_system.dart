class LevelingSystem {
  static int expForNextLevel(int currentLevel) {
    final level = currentLevel.clamp(1, 1000000);
    return level * 50 + level * level * 10;
  }

  static int totalExpForLevel(int targetLevel) {
    int total = 0;
    for (int i = 1; i < targetLevel; i++) {
      total += expForNextLevel(i);
    }
    return total;
  }

  static int calculateLevel(int totalExp) {
    int level = 1;
    int remaining = totalExp;
    while (remaining >= expForNextLevel(level)) {
      remaining -= expForNextLevel(level);
      level++;
    }
    return level;
  }

  static int expInCurrentLevel(int totalExp) {
    final currentLevel = calculateLevel(totalExp);
    int consumed = 0;
    for (int i = 1; i < currentLevel; i++) {
      consumed += expForNextLevel(i);
    }
    return totalExp - consumed;
  }

  static double progressPercent(int totalExp) {
    final currentLevel = calculateLevel(totalExp);
    final needed = expForNextLevel(currentLevel);
    final inLevel = expInCurrentLevel(totalExp);
    return (inLevel / needed).clamp(0.0, 1.0);
  }
}

class LandLevelingSystem {
  static const int maxLevel = 10;

  static double speedMultiplier(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1.0 + (lv - 1) * 0.05;
  }

  static double yieldMultiplier(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1.0 + (lv - 1) * 0.05;
  }

  static int expForNextLevel(int currentLevel) {
    final lv = currentLevel.clamp(1, maxLevel);
    return lv * 20;
  }

  static int calculateLevel(int totalExp) {
    int level = 1;
    int remaining = totalExp;
    while (remaining >= expForNextLevel(level) && level < maxLevel) {
      remaining -= expForNextLevel(level);
      level++;
    }
    return level;
  }
}

class AnimalLevelingSystem {
  static const int maxLevel = 20;

  static double efficiencyMultiplier(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1.0 + (lv - 1) * 0.03;
  }

  static double rareDropBonus(int level) {
    final lv = level.clamp(1, maxLevel);
    return (lv - 1) * 0.002;
  }

  static int expForNextLevel(int currentLevel) {
    final lv = currentLevel.clamp(1, maxLevel);
    return lv * 30;
  }

  static int calculateLevel(int totalExp) {
    int level = 1;
    int remaining = totalExp;
    while (remaining >= expForNextLevel(level) && level < maxLevel) {
      remaining -= expForNextLevel(level);
      level++;
    }
    return level;
  }
}

class PetLevelingSystem {
  static const int maxLevel = 100;

  static double workSpeedMultiplier(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1.0 + (lv - 1) * 0.015;
  }

  static int workRange(int level) {
    final lv = level.clamp(1, maxLevel);
    return 5 + (lv ~/ 2);
  }

  static double luckyBonus(int level) {
    final lv = level.clamp(1, maxLevel);
    return (lv - 1) * 0.001;
  }

  static int workSlots(int level) {
    if (level >= 100) return 5;
    if (level >= 80) return 4;
    if (level >= 50) return 3;
    if (level >= 20) return 2;
    return 1;
  }

  static int expForNextLevel(int currentLevel) {
    final lv = currentLevel.clamp(1, maxLevel);
    return lv * 50;
  }

  static int calculateLevel(int totalExp) {
    int level = 1;
    int remaining = totalExp;
    while (remaining >= expForNextLevel(level) && level < maxLevel) {
      remaining -= expForNextLevel(level);
      level++;
    }
    return level;
  }
}

class BuildingLevelingSystem {
  static const int maxLevel = 20;

  static double speedMultiplier(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1.0 + (lv - 1) * 0.04;
  }

  static int queueSize(int level) {
    final lv = level.clamp(1, maxLevel);
    return 1 + (lv ~/ 5);
  }

  static int expForNextLevel(int currentLevel) {
    final lv = currentLevel.clamp(1, maxLevel);
    return lv * 100;
  }

  static int calculateLevel(int totalExp) {
    int level = 1;
    int remaining = totalExp;
    while (remaining >= expForNextLevel(level) && level < maxLevel) {
      remaining -= expForNextLevel(level);
      level++;
    }
    return level;
  }
}

class LuckSystem {
  static const double baseCritDouble = 0.05;
  static const double baseCritTriple = 0.01;

  static double rollCrit([double luckBonus = 0.0]) {
    final seed = DateTime.now().millisecondsSinceEpoch;
    final r = (seed * 1103515245 + 12345) & 0x7fffffff;
    final roll = (r % 10000) / 10000.0;
    final doubleChance = baseCritDouble + luckBonus;
    final tripleChance = baseCritTriple + luckBonus * 0.5;
    if (roll < tripleChance) return 3.0;
    if (roll < doubleChance + tripleChance) return 2.0;
    return 1.0;
  }

  static double qualityLuckBonus(double luckBonus) {
    return luckBonus.clamp(0.0, 0.3);
  }
}

class CollectionScoreSystem {
  static int scoreForQuality(int qualityIndex) {
    switch (qualityIndex) {
      case 0: return 1;
      case 1: return 3;
      case 2: return 10;
      case 3: return 50;
      case 4: return 200;
      case 5: return 500;
      default: return 1;
    }
  }

  static int collectionLevel(int totalScore) {
    if (totalScore >= 5000) return 30;
    if (totalScore >= 2000) return 20;
    if (totalScore >= 500) return 10;
    if (totalScore >= 100) return 5;
    if (totalScore >= 10) return 1;
    return 0;
  }

  static String collectionTitle(int level) {
    if (level >= 30) return '世界收藏家';
    if (level >= 20) return '传奇馆长';
    if (level >= 10) return '收藏专家';
    if (level >= 5) return '农场学者';
    if (level >= 1) return '收藏新手';
    return '刚入门';
  }
}
