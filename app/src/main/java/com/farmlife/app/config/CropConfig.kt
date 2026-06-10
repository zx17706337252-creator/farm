package com.farmlife.app.config

/**
 * 作物配置 - 游戏启动时加载的静态数据
 * 共50种作物：基础粮食(5) + 蔬菜(10) + 水果(10) + 经济作物(5) + 高级作物(5) + 花卉(5) + 特殊(5) + 传奇(5)
 */
data class CropConfig(
    val cropId: Int,
    val name: String,
    val category: String,
    val growTimeSeconds: Int,
    val seedPrice: Int,      // 种子购买价格
    val sellPrice: Int,      // 作物售出价格
    val exp: Int,
    val unlockLevel: Int,
    val icon: String
)

object CropConfigs {
    val ALL: List<CropConfig> = listOf(
        // === 基础粮食 === (种子价格约为售价的20%)
        CropConfig(1, "小麦", "GRAIN", 30, 1, 5, 1, 1, "🌾"),
        CropConfig(2, "玉米", "GRAIN", 120, 4, 20, 3, 1, "🌽"),
        CropConfig(3, "水稻", "GRAIN", 180, 6, 30, 4, 3, "🍚"),
        CropConfig(4, "高粱", "GRAIN", 240, 9, 45, 6, 5, "🌾"),
        CropConfig(5, "燕麦", "GRAIN", 300, 11, 55, 7, 7, "🌾"),

        // === 蔬菜 === (种子价格约为售价的20%)
        CropConfig(11, "胡萝卜", "VEGETABLE", 60, 2, 10, 2, 1, "🥕"),
        CropConfig(12, "土豆", "VEGETABLE", 300, 8, 40, 5, 2, "🥔"),
        CropConfig(13, "番茄", "VEGETABLE", 900, 24, 120, 15, 5, "🍅"),
        CropConfig(14, "黄瓜", "VEGETABLE", 480, 12, 60, 8, 6, "🥒"),
        CropConfig(15, "南瓜", "VEGETABLE", 1200, 36, 180, 20, 8, "🎃"),
        CropConfig(16, "洋葱", "VEGETABLE", 360, 10, 50, 6, 4, "🧅"),
        CropConfig(17, "白萝卜", "VEGETABLE", 420, 11, 55, 7, 5, "🥬"),
        CropConfig(18, "生菜", "VEGETABLE", 240, 7, 35, 5, 3, "🥬"),
        CropConfig(19, "卷心菜", "VEGETABLE", 600, 16, 80, 10, 7, "🥬"),
        CropConfig(20, "辣椒", "VEGETABLE", 720, 20, 100, 12, 9, "🌶️"),

        // === 水果 === (种子价格约为售价的20%)
        CropConfig(21, "草莓", "FRUIT", 600, 16, 80, 10, 4, "🍓"),
        CropConfig(22, "蓝莓", "FRUIT", 1800, 60, 300, 25, 8, "🫐"),
        CropConfig(23, "西瓜", "FRUIT", 3600, 100, 500, 40, 12, "🍉"),
        CropConfig(24, "哈密瓜", "FRUIT", 4200, 120, 600, 50, 14, "🍈"),
        CropConfig(25, "葡萄", "FRUIT", 7200, 240, 1200, 100, 15, "🍇"),
        CropConfig(26, "苹果", "FRUIT", 5400, 160, 800, 70, 18, "🍎"),
        CropConfig(27, "梨", "FRUIT", 5400, 164, 820, 72, 19, "🍐"),
        CropConfig(28, "樱桃", "FRUIT", 4800, 140, 700, 60, 20, "🍒"),
        CropConfig(29, "桃子", "FRUIT", 6000, 180, 900, 80, 22, "🍑"),
        CropConfig(30, "柠檬", "FRUIT", 6600, 190, 950, 85, 25, "🍋"),

        // === 经济作物 === (种子价格约为售价的20%)
        CropConfig(31, "咖啡豆", "CASH", 3600, 120, 600, 50, 10, "☕"),
        CropConfig(32, "茶叶", "CASH", 4800, 170, 850, 70, 15, "🍵"),
        CropConfig(33, "可可豆", "CASH", 5400, 190, 950, 80, 20, "🍫"),
        CropConfig(34, "甘蔗", "CASH", 3000, 100, 500, 40, 12, "🎋"),
        CropConfig(35, "棉花", "CASH", 4200, 140, 700, 55, 18, "☁️"),

        // === 高级作物 === (种子价格约为售价的20%)
        CropConfig(41, "松露菌", "ADVANCED", 14400, 600, 3000, 200, 30, "🍄"),
        CropConfig(42, "人参", "ADVANCED", 28800, 1000, 5000, 300, 35, "🌿"),
        CropConfig(43, "灵芝", "ADVANCED", 36000, 1400, 7000, 400, 40, "🍄"),
        CropConfig(44, "藏红花", "ADVANCED", 21600, 900, 4500, 280, 38, "🌸"),
        CropConfig(45, "香草荚", "ADVANCED", 18000, 760, 3800, 250, 32, "🌱"),

        // === 花卉 === (种子价格约为售价的20%)
        CropConfig(51, "玫瑰", "FLOWER", 1800, 70, 350, 30, 12, "🌹"),
        CropConfig(52, "郁金香", "FLOWER", 2400, 90, 450, 35, 14, "🌷"),
        CropConfig(53, "百合", "FLOWER", 3000, 110, 550, 45, 16, "🌼"),
        CropConfig(54, "向日葵", "FLOWER", 3600, 130, 650, 50, 18, "🌻"),
        CropConfig(55, "薰衣草", "FLOWER", 4200, 150, 750, 60, 20, "💜"),

        // === 特殊作物 === (种子价格约为售价的20%)
        CropConfig(61, "金麦穗", "SPECIAL", 7200, 400, 2000, 150, 25, "🌟"),
        CropConfig(62, "彩虹莓", "SPECIAL", 10800, 700, 3500, 220, 40, "🌈"),
        CropConfig(63, "月光花", "SPECIAL", 14400, 900, 4500, 280, 45, "🌙"),
        CropConfig(64, "星辰果", "SPECIAL", 18000, 1100, 5500, 350, 50, "⭐"),
        CropConfig(65, "翡翠瓜", "SPECIAL", 12600, 800, 4000, 260, 42, "💎"),

        // === 传奇作物 === (种子价格约为售价的20%)
        CropConfig(71, "凤凰果", "LEGENDARY", 43200, 3000, 15000, 800, 55, "🔥"),
        CropConfig(72, "龙血藤", "LEGENDARY", 54000, 4000, 20000, 1000, 60, "🐉"),
        CropConfig(73, "生命花", "LEGENDARY", 64800, 5000, 25000, 1200, 65, "💚"),
        CropConfig(74, "天空树果实", "LEGENDARY", 72000, 6000, 30000, 1500, 70, "🌳"),
        CropConfig(75, "永恒之种", "LEGENDARY", 86400, 10000, 50000, 2000, 80, "✨")
    )

    fun getById(id: Int): CropConfig? = ALL.firstOrNull { it.cropId == id }

    fun getByCategory(category: String): List<CropConfig> = ALL.filter { it.category == category }

    fun getByUnlockLevel(level: Int): List<CropConfig> = ALL.filter { it.unlockLevel <= level }
}
