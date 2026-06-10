package com.farmlife.shared.model

/**
 * 天气
 */
enum class Weather(val displayName: String, val emoji: String, val qualityBonus: Double) {
    SUNNY("晴天", "☀️", 0.1),
    CLOUDY("多云", "☁️", 0.0),
    RAINY("雨天", "🌧️", 0.15),
    SNOWY("雪天", "❄️", -0.05),
    RAINBOW("彩虹", "🌈", 0.25),
    METEOR("流星", "✨", 0.3);
    
    companion object {
        fun fromOrdinal(ordinal: Int): Weather = entries.getOrElse(ordinal) { SUNNY }
    }
}
