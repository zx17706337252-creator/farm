package com.farmlife.app.config

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

object TreeConfigs {
    val ALL: List<TreeConfig> = listOf(
        TreeConfig(1, "苹果树", 24, 8, 500, 40, 20, "🍎"),
        TreeConfig(2, "樱桃树", 36, 6, 800, 60, 22, "🍒"),
        TreeConfig(3, "梨树", 48, 8, 600, 50, 21, "🍐"),
        TreeConfig(4, "桃树", 30, 7, 700, 55, 23, "🍑"),
        TreeConfig(5, "橘子树", 36, 6, 650, 52, 24, "🍊"),
        TreeConfig(6, "柠檬树", 42, 8, 750, 58, 25, "🍋"),
        TreeConfig(7, "芒果树", 60, 10, 1200, 80, 28, "🥭"),
        TreeConfig(8, "椰子树", 72, 12, 1500, 100, 30, "🥥"),
        TreeConfig(9, "蓝莓树", 30, 6, 400, 35, 20, "🫐"),
        TreeConfig(10, "葡萄树", 48, 8, 900, 65, 26, "🍇")
    )

    fun getById(id: Int): TreeConfig? = ALL.firstOrNull { it.treeId == id }
}

/**
 * 鱼塘配置
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

object FishConfigs {
    val ALL: List<FishConfig> = listOf(
        FishConfig(1, "鲫鱼", 30, 50, 5, 40, "🐟"),
        FishConfig(2, "鲤鱼", 45, 80, 8, 42, "🐠"),
        FishConfig(3, "草鱼", 60, 120, 12, 44, "🐡"),
        FishConfig(4, "鲈鱼", 90, 200, 18, 46, "🐟"),
        FishConfig(5, "金鱼", 120, 300, 25, 48, "🐠"),
        FishConfig(6, "锦鲤", 180, 500, 40, 50, "🐟"),
        FishConfig(7, "龙鱼", 300, 1000, 80, 55, "🐉"),
        FishConfig(8, "金鱼王", 480, 2000, 150, 60, "👑")
    )

    fun getById(id: Int): FishConfig? = ALL.firstOrNull { it.fishId == id }
}

/**
 * 宠物设施配置
 */
data class PetFacilityConfig(
    val facilityId: Int,
    val name: String,
    val description: String,
    val unlockLevel: Int,
    val buildCost: Int,
    val icon: String
)

object PetFacilityConfigs {
    val ALL: List<PetFacilityConfig> = listOf(
        PetFacilityConfig(1, "宠物宿舍", "宠物休息恢复体力", 50, 5000, "🏠"),
        PetFacilityConfig(2, "训练场", "宠物训练提升等级", 52, 8000, "🎾"),
        PetFacilityConfig(3, "宠物医院", "治疗疲劳宠物", 54, 6000, "🏥"),
        PetFacilityConfig(4, "游乐场", "游玩提升亲密度", 56, 10000, "🎠")
    )

    fun getById(id: Int): PetFacilityConfig? = ALL.firstOrNull { it.facilityId == id }
}
