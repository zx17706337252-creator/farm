package com.farmlife.shared.logic

import com.farmlife.shared.model.Season
import com.farmlife.shared.model.Weather

/**
 * 季节天气系统
 */
object SeasonWeatherSystem {
    
    /**
     * 根据当前时间计算季节
     */
    fun calculateSeason(timeMs: Long = System.currentTimeMillis()): Season {
        val dayOfYear = ((timeMs / (24 * 60 * 60 * 1000)) % 365).toInt()
        return when (dayOfYear) {
            in 0..89 -> Season.SPRING    // 春
            in 90..179 -> Season.SUMMER   // 夏
            in 180..269 -> Season.AUTUMN  // 秋
            else -> Season.WINTER         // 冬
        }
    }
    
    /**
     * 根据时间生成随机天气
     */
    fun generateWeather(timeMs: Long = System.currentTimeMillis()): Weather {
        val hash = (timeMs / (24 * 60 * 60 * 1000)).toInt() // 每天固定天气
        val dayOfYear = hash % 365
        
        // 季节加权
        val season = calculateSeason(timeMs)
        return when (season) {
            Season.SPRING -> when (dayOfYear % 10) {
                0, 1 -> Weather.RAINY
                2 -> Weather.RAINBOW
                else -> Weather.SUNNY
            }
            Season.SUMMER -> when (dayOfYear % 10) {
                0, 1 -> Weather.SUNNY
                2 -> Weather.METEOR
                3, 4 -> Weather.CLOUDY
                else -> Weather.SUNNY
            }
            Season.AUTUMN -> when (dayOfYear % 10) {
                0, 1 -> Weather.CLOUDY
                2, 3 -> Weather.RAINY
                4 -> Weather.RAINBOW
                else -> Weather.SUNNY
            }
            Season.WINTER -> when (dayOfYear % 10) {
                0, 1, 2 -> Weather.SNOWY
                3 -> Weather.CLOUDY
                else -> Weather.SUNNY
            }
        }
    }
    
    /**
     * 获取季节生长倍率
     */
    fun seasonGrowthMultiplier(season: Season): Double {
        return when (season) {
            Season.SPRING -> 1.0  // 正常
            Season.SUMMER -> 1.3  // 加速30%
            Season.AUTUMN -> 0.9  // 减速10%
            Season.WINTER -> 0.6  // 减速40%
        }
    }
    
    /**
     * 获取天气品质加成
     */
    fun weatherQualityBonus(weather: Weather): Double {
        return weather.qualityBonus
    }
    
    /**
     * 获取天气对生产的影响
     */
    fun weatherProductionBonus(weather: Weather): Double {
        return when (weather) {
            Weather.SUNNY -> 1.1
            Weather.CLOUDY -> 1.0
            Weather.RAINY -> 1.2
            Weather.SNOWY -> 0.8
            Weather.RAINBOW -> 1.3
            Weather.METEOR -> 1.5
        }
    }
}
