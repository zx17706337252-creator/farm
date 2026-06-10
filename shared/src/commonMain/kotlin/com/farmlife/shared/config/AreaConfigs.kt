package com.farmlife.shared.config

/**
 * 果树配置
 */
data class TreeConfig(
    val treeId: Int,
    val name: String,
    val growTimeHours: Int,
    val harvestTimeHours: Int,
    val sellPrice: Int,
    val exp: Int,
    val unlockLevel: Int,
    val icon: String
)

/**
 * 鱼类配置
 */
data class FishConfig(
    val fishId: Int,
    val name: String,
    val growTimeMinutes: Int,
    val sellPrice: Int,
    val exp: Int,
    val unlockLevel: Int,
    val icon: String
)

/**
 * 宠物设施配置
 */
data class PetFacilityConfig(
    val facilityId: Int,
    val name: String,
    val description: String,
    val cost: Int,
    val unlockLevel: Int,
    val icon: String
)

object TreeConfigs {
    val ALL = listOf(
        TreeConfig(1, "苹果树", 24, 12, 300, 150, 20, "🍎"),
        TreeConfig(2, "橙子树", 36, 18, 500, 250, 25, "🍊"),
        TreeConfig(3, "葡萄藤", 48, 24, 800, 400, 30, "🍇"),
        TreeConfig(4, "樱桃树", 72, 36, 1200, 600, 35, "🍒"),
        TreeConfig(5, "柠檬树", 96, 48, 1800, 900, 40, "🍋"),
        TreeConfig(6, "桃子树", 120, 60, 2500, 1250, 45, "🍑"),
        TreeConfig(7, "梨树", 144, 72, 3500, 1750, 50, "🍐"),
        TreeConfig(8, "椰子树", 168, 84, 5000, 2500, 55, "🥥"),
        TreeConfig(9, "榴莲树", 216, 108, 8000, 4000, 60, "🍈"),
        TreeConfig(10, "人参果树", 288, 144, 15000, 7500, 70, "🌳")
    )
    fun getById(id: Int): TreeConfig? = ALL.find { it.treeId == id }
}

object FishConfigs {
    val ALL = listOf(
        FishConfig(1, "草鱼", 30, 50, 25, 40, "🐟"),
        FishConfig(2, "鲤鱼", 60, 100, 50, 50, "🐠"),
        FishConfig(3, "金鱼", 120, 200, 100, 55, "🐡"),
        FishConfig(4, "龙鱼", 240, 500, 250, 60, "🐉"),
        FishConfig(5, "锦鲤", 480, 1000, 500, 65, "🐠"),
        FishConfig(6, "海豚", 720, 2000, 1000, 70, "🐬"),
        FishConfig(7, "鲸鱼", 960, 5000, 2500, 75, "🐋"),
        FishConfig(8, "海龙", 1440, 10000, 5000, 80, "🐲")
    )
    fun getById(id: Int): FishConfig? = ALL.find { it.fishId == id }
}

object PetFacilityConfigs {
    val ALL = listOf(
        PetFacilityConfig(1, "宠物窝", "宠物休息的地方", 5000, 15, "🏠"),
        PetFacilityConfig(2, "宠物训练场", "提升宠物能力", 15000, 30, "🎪"),
        PetFacilityConfig(3, "宠物医院", "治疗受伤宠物", 25000, 45, "🏥"),
        PetFacilityConfig(4, "宠物乐园", "宠物自由活动", 50000, 60, "🎠")
    )
    fun getById(id: Int): PetFacilityConfig? = ALL.find { it.facilityId == id }
}
