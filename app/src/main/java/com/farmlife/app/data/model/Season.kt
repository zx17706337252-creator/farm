package com.farmlife.app.data.model

/** 季节枚举 - 每7天轮换 */
enum class Season(val displayName: String, val hexColor: String) {
    SPRING("春季", "#81C784"),
    SUMMER("夏季", "#FFD54F"),
    AUTUMN("秋季", "#FF8A65"),
    WINTER("冬季", "#B0BEC5");

    companion object {
        fun fromDay(day: Long): Season {
            val seasonIndex = ((day / 7) % 4).toInt()
            return values()[seasonIndex]
        }
    }
}

/** 天气枚举 */
enum class Weather(val displayName: String) {
    SUNNY("晴天"),
    CLOUDY("多云"),
    RAINY("雨天"),
    SNOWY("雪天"),
    RAINBOW("彩虹"),
    METEOR("流星雨")
}
