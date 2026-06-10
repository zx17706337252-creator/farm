package com.farmlife.shared.config

/**
 * 作物配置
 */
data class CropConfig(
    val cropId: Int,
    val name: String,
    val category: String,
    val growTimeSeconds: Int,
    val sellPrice: Int,
    val exp: Int,
    val unlockLevel: Int,
    val icon: String
)

object CropConfigs {
    val ALL = listOf(
        // 基础作物 (1-5)
        CropConfig(1, "小麦", "谷物", 60, 10, 5, 1, "🌾"),
        CropConfig(2, "玉米", "谷物", 90, 15, 8, 1, "🌽"),
        CropConfig(3, "稻谷", "谷物", 120, 20, 10, 1, "🍚"),
        CropConfig(4, "燕麦", "谷物", 180, 30, 15, 2, "🌿"),
        CropConfig(5, "大麦", "谷物", 150, 25, 12, 2, "🌾"),
        
        // 蔬菜 (11-20)
        CropConfig(11, "土豆", "蔬菜", 240, 40, 20, 3, "🥔"),
        CropConfig(12, "番茄", "蔬菜", 300, 50, 25, 3, "🍅"),
        CropConfig(13, "胡萝卜", "蔬菜", 360, 60, 30, 4, "🥕"),
        CropConfig(14, "白菜", "蔬菜", 480, 80, 40, 5, "🥬"),
        CropConfig(15, "黄瓜", "蔬菜", 420, 70, 35, 4, "🥒"),
        
        // 水果 (21-30)
        CropConfig(21, "草莓", "水果", 600, 100, 50, 6, "🍓"),
        CropConfig(22, "葡萄", "水果", 900, 150, 75, 8, "🍇"),
        CropConfig(23, "西瓜", "水果", 1200, 200, 100, 10, "🍉"),
        CropConfig(24, "苹果", "水果", 1800, 300, 150, 12, "🍎"),
        CropConfig(25, "橙子", "水果", 2400, 400, 200, 15, "🍊"),
        
        // 高级 (31-40)
        CropConfig(31, "人参", "药材", 3600, 800, 400, 20, "🌿"),
        CropConfig(32, "灵芝", "药材", 7200, 1500, 750, 25, "🍄"),
        CropConfig(33, "藏红花", "香料", 5400, 2000, 1000, 30, "🌸"),
        CropConfig(34, "松露", "珍品", 10800, 5000, 2500, 40, "🫛"),
        CropConfig(35, "月光草", "魔法", 14400, 10000, 5000, 50, "🌙"),
        
        // 传奇 (41-50)
        CropConfig(41, "龙血树", "传说", 28800, 30000, 15000, 60, "🌳"),
        CropConfig(42, "智慧果", "传说", 36000, 50000, 25000, 70, "🍎")
    )
    
    fun getById(id: Int): CropConfig? = ALL.find { it.cropId == id }
}
