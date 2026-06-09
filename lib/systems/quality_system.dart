import '../models/quality.dart';
import '../models/season_weather.dart';

class QualitySystem {
  static double qualityMultiplier(int qualityIndex) {
    return QualityExtension.multiplierFor(qualityIndex);
  }

  static Quality rollQuality([double bonus = 0.0]) {
    return QualityExtension.roll(bonus);
  }

  static int rollQualityIndex([double bonus = 0.0]) {
    return QualityExtension.roll(bonus).index;
  }
}

class SeasonWeatherSystem {
  static double seasonGrowthMultiplier(Season season) {
    return season.growthMultiplier;
  }

  static double weatherQualityBonus(Weather weather, Season season) {
    return weather.qualityBonus(season);
  }

  static double weatherAnimalBonus(Weather weather) {
    return weather.animalBonus;
  }

  static double seasonAnimalMultiplier(Season season) {
    return season.animalMultiplier;
  }

  static double calculateAnimalEfficiency(Season season, Weather weather, double baseEfficiency) {
    return baseEfficiency * seasonAnimalMultiplier(season) * weatherAnimalBonus(weather);
  }

  static String getSeasonDescription(Season season) {
    return season.description;
  }

  static String getWeatherDescription(Weather weather) {
    return weather.description;
  }
}

extension QualityExtension on Quality {
  double get multiplierVal {
    switch (this) {
      case Quality.common: return 1.0;
      case Quality.good: return 1.2;
      case Quality.rare: return 1.5;
      case Quality.epic: return 2.0;
      case Quality.legendary: return 3.0;
      case Quality.mythical: return 5.0;
    }
  }

  static double multiplierFor(int qualityIndex) {
    return Quality.values[qualityIndex.clamp(0, Quality.values.length - 1)].multiplierVal;
  }

  static Quality roll([double bonus = 0.0]) {
    final seed = DateTime.now().millisecondsSinceEpoch;
    final r = (seed * 1103515245 + 12345) & 0x7fffffff;
    final value = (r % 10000) / 10000.0 + bonus;
    if (value > 0.995) return Quality.mythical;
    if (value > 0.98) return Quality.legendary;
    if (value > 0.94) return Quality.epic;
    if (value > 0.85) return Quality.rare;
    if (value > 0.65) return Quality.good;
    return Quality.common;
  }
}

extension SeasonExtension2 on Season {
  String get description {
    switch (this) {
      case Season.spring: return '万物复苏的春天，作物生长加速 +10%';
      case Season.summer: return '阳光充沛的夏天，一切正常生长';
      case Season.autumn: return '硕果累累的秋天，作物生长加速 +20%';
      case Season.winter: return '白雪皑皑的冬天，作物生长减缓 50%';
    }
  }
}

extension WeatherExtension2 on Weather {
  String get description {
    switch (this) {
      case Weather.sunny: return '☀️ 阳光明媚';
      case Weather.cloudy: return '☁️ 多云天气';
      case Weather.rainy: return '🌧️ 下雨了，品质概率 +5%';
      case Weather.snowy: return '❄️ 下雪了';
      case Weather.rainbow: return '🌈 彩虹出现！品质概率 +10%';
      case Weather.meteor: return '🌠 流星雨！品质概率 +15%';
    }
  }
}
