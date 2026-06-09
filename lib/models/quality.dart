import 'package:flutter/material.dart';

enum Quality {
  common,
  good,
  rare,
  epic,
  legendary,
  mythical,
}

extension QualityExtension on Quality {
  double get multiplier {
    switch (this) {
      case Quality.common:
        return 1.0;
      case Quality.good:
        return 1.2;
      case Quality.rare:
        return 1.5;
      case Quality.epic:
        return 2.0;
      case Quality.legendary:
        return 3.0;
      case Quality.mythical:
        return 5.0;
    }
  }

  String get displayName {
    switch (this) {
      case Quality.common:
        return '普通';
      case Quality.good:
        return '优良';
      case Quality.rare:
        return '稀有';
      case Quality.epic:
        return '史诗';
      case Quality.legendary:
        return '传说';
      case Quality.mythical:
        return '神话';
    }
  }

  String get hexColor {
    switch (this) {
      case Quality.common:
        return '#9E9E9E';
      case Quality.good:
        return '#4CAF50';
      case Quality.rare:
        return '#2196F3';
      case Quality.epic:
        return '#9C27B0';
      case Quality.legendary:
        return '#FF9800';
      case Quality.mythical:
        return '#E91E63';
    }
  }

  Color get color {
    switch (this) {
      case Quality.common:
        return const Color(0xFF9E9E9E);
      case Quality.good:
        return const Color(0xFF4CAF50);
      case Quality.rare:
        return const Color(0xFF2196F3);
      case Quality.epic:
        return const Color(0xFF9C27B0);
      case Quality.legendary:
        return const Color(0xFFFF9800);
      case Quality.mythical:
        return const Color(0xFFE91E63);
    }
  }

  int get ordinal => index;

  static Quality fromIndex(int index) {
    return Quality.values.elementAt(index.clamp(0, Quality.values.length - 1));
  }

  static Quality roll([double bonus = 0.0]) {
    final r = (1.0 + bonus) * _rand();
    if (r > 500) return Quality.mythical;
    if (r > 200) return Quality.legendary;
    if (r > 50) return Quality.epic;
    if (r > 10) return Quality.rare;
    if (r > 2) return Quality.good;
    return Quality.common;
  }

  static double _rand() {
    return (DateTime.now().millisecondsSinceEpoch % 1000) / 1000.0 * 100;
  }
}
