enum Season {
  spring,
  summer,
  autumn,
  winter,
}

extension SeasonExtension on Season {
  String get displayName {
    switch (this) {
      case Season.spring:
        return '春天';
      case Season.summer:
        return '夏天';
      case Season.autumn:
        return '秋天';
      case Season.winter:
        return '冬天';
    }
  }

  String get emoji {
    switch (this) {
      case Season.spring:
        return '🌸';
      case Season.summer:
        return '☀️';
      case Season.autumn:
        return '🍂';
      case Season.winter:
        return '❄️';
    }
  }

  double get growthMultiplier {
    switch (this) {
      case Season.spring:
        return 1.1;
      case Season.summer:
        return 1.0;
      case Season.autumn:
        return 1.2;
      case Season.winter:
        return 0.5;
    }
  }

  double get animalMultiplier {
    switch (this) {
      case Season.winter:
        return 0.9;
      default:
        return 1.0;
    }
  }
}

enum Weather {
  sunny,
  cloudy,
  rainy,
  snowy,
  rainbow,
  meteor,
}

extension WeatherExtension on Weather {
  String get displayName {
    switch (this) {
      case Weather.sunny:
        return '阳光明媚';
      case Weather.cloudy:
        return '多云天气';
      case Weather.rainy:
        return '下雨了';
      case Weather.snowy:
        return '下雪了';
      case Weather.rainbow:
        return '彩虹出现！';
      case Weather.meteor:
        return '流星雨！';
    }
  }

  String get emoji {
    switch (this) {
      case Weather.sunny:
        return '☀️';
      case Weather.cloudy:
        return '☁️';
      case Weather.rainy:
        return '🌧️';
      case Weather.snowy:
        return '❄️';
      case Weather.rainbow:
        return '🌈';
      case Weather.meteor:
        return '🌠';
    }
  }

  double qualityBonus(Season season) {
    switch (this) {
      case Weather.sunny:
        return 0.0;
      case Weather.cloudy:
        return 0.02;
      case Weather.rainy:
        return 0.05;
      case Weather.snowy:
        return season == Season.winter ? 0.03 : 0.0;
      case Weather.rainbow:
        return 0.10;
      case Weather.meteor:
        return 0.15;
    }
  }

  double get animalBonus {
    switch (this) {
      case Weather.rainy:
        return 0.9;
      case Weather.snowy:
        return 0.85;
      default:
        return 1.0;
    }
  }
}
