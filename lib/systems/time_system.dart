import '../models/season_weather.dart';

class TimeSystem {
  static int currentTimeMs() {
    return DateTime.now().millisecondsSinceEpoch;
  }

  static Season currentSeason(int gameStartTime) {
    final now = DateTime.now();
    final start = DateTime.fromMillisecondsSinceEpoch(gameStartTime);
    final daysPlayed = now.difference(start).inDays;
    final seasonIndex = (daysPlayed ~/ 7) % 4;
    return Season.values[seasonIndex];
  }

  static Weather currentWeather() {
    final seed = DateTime.now().millisecondsSinceEpoch ~/ 1000;
    final r = (seed * 1103515245 + 12345) & 0x7fffffff;
    final value = r % 100;
    if (value < 40) return Weather.sunny;
    if (value < 65) return Weather.cloudy;
    if (value < 85) return Weather.rainy;
    if (value < 95) return Weather.snowy;
    if (value < 99) return Weather.rainbow;
    return Weather.meteor;
  }

  static int calculateProcessFinishTime(int processTimeSeconds, int factoryLevel) {
    final speedMultiplier = 1.0 + (factoryLevel - 1) * 0.04;
    final adjustedTime = (processTimeSeconds * 1000 / speedMultiplier).toInt();
    return currentTimeMs() + adjustedTime;
  }

  static String formatDuration(int milliseconds) {
    if (milliseconds <= 0) return '已完成';
    final seconds = (milliseconds / 1000).round();
    if (seconds < 60) return '${seconds}秒';
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    if (minutes < 60) return '${minutes}分${remainingSeconds > 0 ? "${remainingSeconds}秒" : ""}';
    final hours = minutes ~/ 60;
    final remainingMinutes = minutes % 60;
    if (hours < 24) return '${hours}时${remainingMinutes}分';
    final days = hours ~/ 24;
    final remainingHours = hours % 24;
    return '${days}天${remainingHours}时';
  }

  static String formatTime(int timestampMs) {
    final dt = DateTime.fromMillisecondsSinceEpoch(timestampMs);
    return '${dt.month}/${dt.day} ${dt.hour.toString().padLeft(2, '0')}:${dt.minute.toString().padLeft(2, '0')}';
  }
}
