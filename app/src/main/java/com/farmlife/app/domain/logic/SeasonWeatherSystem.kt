package com.farmlife.app.domain.logic

import com.farmlife.app.data.model.Quality
import com.farmlife.app.data.model.Season
import com.farmlife.app.data.model.Weather

/**
 * 季节与天气对产量的影响系统
 */
object SeasonWeatherSystem {

    /**
     * 季节对作物成长速度的影响
     * 春季: 1.1x (适宜生长)
     * 夏季: 1.0x (正常)
     * 秋季: 1.2x (丰收季)
     * 冬季: 0.5x (生长缓慢)
     */
    fun seasonGrowthMultiplier(season: Season): Double {
        return when (season) {
            Season.SPRING -> 1.1
            Season.SUMMER -> 1.0
            Season.AUTUMN -> 1.2
            Season.WINTER -> 0.5
        }
    }

    /**
     * 天气对品质的影响
     * 晴天: 正常
     * 多云: +2%品质概率
     * 雨天: +5%品质概率
     * 雪天: 冬季时+3%品质概率
     * 彩虹: +10%品质概率
     * 流星雨: +15%品质概率
     */
    fun weatherQualityBonus(weather: Weather, season: Season): Double {
        return when (weather) {
            Weather.SUNNY -> 0.0
            Weather.CLOUDY -> 0.02
            Weather.RAINY -> 0.05
            Weather.SNOWY -> if (season == Season.WINTER) 0.03 else 0.0
            Weather.RAINBOW -> 0.10
            Weather.METEOR -> 0.15
        }
    }

    /**
     * 天气对动物生产的影响
     */
    fun weatherAnimalBonus(weather: Weather): Double {
        return when (weather) {
            Weather.RAINY -> 0.9   // 雨天动物心情差
            Weather.SNOWY -> 0.85  // 雪天活动减少
            else -> 1.0
        }
    }

    /**
     * 季节对动物产量的影响
     */
    fun seasonAnimalMultiplier(season: Season): Double {
        return when (season) {
            Season.WINTER -> 0.9   // 冬季产量略减
            else -> 1.0
        }
    }

    /**
     * 综合计算作物品质抽取
     */
    fun rollCropQuality(season: Season, weather: Weather, luckBonus: Double = 0.0): Quality {
        val weatherBonus = weatherQualityBonus(weather, season)
        return Quality.roll(weatherBonus + luckBonus)
    }

    /**
     * 计算动物产出效率
     */
    fun calculateAnimalEfficiency(season: Season, weather: Weather, baseEfficiency: Double): Double {
        return baseEfficiency * seasonAnimalMultiplier(season) * weatherAnimalBonus(weather)
    }

    /**
     * 获取季节描述
     */
    fun getSeasonDescription(season: Season): String {
        return when (season) {
            Season.SPRING -> "万物复苏的春天，作物生长加速 +10%"
            Season.SUMMER -> "阳光充沛的夏天，一切正常生长"
            Season.AUTUMN -> "硕果累累的秋天，作物生长加速 +20%"
            Season.WINTER -> "白雪皑皑的冬天，作物生长减缓 50%"
        }
    }

    /**
     * 获取天气描述
     */
    fun getWeatherDescription(weather: Weather): String {
        return when (weather) {
            Weather.SUNNY -> "☀️ 阳光明媚"
            Weather.CLOUDY -> "☁️ 多云天气"
            Weather.RAINY -> "🌧️ 下雨了，品质概率 +5%"
            Weather.SNOWY -> "❄️ 下雪了"
            Weather.RAINBOW -> "🌈 彩虹出现！品质概率 +10%"
            Weather.METEOR -> "🌠 流星雨！品质概率 +15%"
        }
    }
}
