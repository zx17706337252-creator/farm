package com.farmlife.app.config

/**
 * 宠物配置 - 30种宠物
 */
data class PetConfig(
    val petId: Int,
    val name: String,
    val category: String,
    val primaryWorkType: String,
    val baseSpeed: Double,
    val baseRange: Int,
    val unlockLevel: Int,
    val purchasePrice: Int,
    val icon: String,
    val description: String
)

object PetConfigs {
    val ALL: List<PetConfig> = listOf(
        // === 基础宠物 ===
        PetConfig(1, "柴犬", "BASIC", "HARVEST", 1.0, 5, 1, 1000, "🐕", "自动收菜，勤劳的小助手"),
        PetConfig(2, "橘猫", "BASIC", "COLLECT", 1.1, 4, 5, 1500, "🐱", "自动收集鸡蛋，灵活敏捷"),
        PetConfig(3, "布偶猫", "BASIC", "COLLECT", 1.0, 4, 8, 2000, "😺", "自动收集牛奶，温柔细致"),
        PetConfig(4, "金毛犬", "BASIC", "CARRY", 1.2, 8, 10, 2500, "🦮", "搬运仓库，力大无穷"),
        PetConfig(5, "边境牧羊犬", "BASIC", "FEED", 1.1, 10, 12, 3000, "🐶", "管理养殖区，聪明伶俐"),

        // === 进阶宠物 ===
        PetConfig(11, "柯基", "ADVANCED", "HARVEST", 1.3, 6, 15, 4000, "🐕‍🦺", "加快收获速度，短腿大能量"),
        PetConfig(12, "哈士奇", "ADVANCED", "EXPLORE", 1.4, 15, 18, 5000, "🐺", "探险专家，精力充沛"),
        PetConfig(13, "德牧", "ADVANCED", "PROCESS", 1.3, 12, 20, 6000, "🐕", "区域管理，严肃认真"),
        PetConfig(14, "狸花猫", "ADVANCED", "COLLECT", 1.2, 8, 22, 5500, "🐈", "寻找稀有掉落，野性敏锐"),
        PetConfig(15, "缅因猫", "ADVANCED", "CARRY", 1.3, 10, 25, 7000, "🐈‍⬛", "仓库管理，沉稳可靠"),

        // === 高级宠物 ===
        PetConfig(21, "白狐", "HIGH", "HARVEST", 1.5, 12, 30, 15000, "🦊", "幸运加成，带来好运"),
        PetConfig(22, "雪狐", "HIGH", "PROCESS", 1.5, 12, 32, 18000, "❄️", "品质加成，冰雪聪明"),
        PetConfig(23, "猫头鹰", "HIGH", "EXPLORE", 1.4, 20, 35, 20000, "🦉", "夜间探险，夜视超群"),
        PetConfig(24, "乌鸦", "HIGH", "COLLECT", 1.5, 15, 38, 22000, "🐦", "稀有材料发现，眼光独到"),
        PetConfig(25, "猎鹰", "HIGH", "CARRY", 1.8, 25, 40, 25000, "🦅", "快速运输，风驰电掣"),

        // === 传说宠物 ===
        PetConfig(31, "凤凰", "LEGEND", "PROCESS", 2.0, 30, 45, 80000, "🔥", "全局生产加速，浴火重生"),
        PetConfig(32, "青龙", "LEGEND", "HARVEST", 2.0, 35, 48, 100000, "🐉", "成长加速，东方守护者"),
        PetConfig(33, "白虎", "LEGEND", "HARVEST", 2.0, 35, 50, 120000, "🐅", "幸运提升，西方战神"),
        PetConfig(34, "玄武", "LEGEND", "CARRY", 2.0, 40, 52, 150000, "🐢", "仓库容量提升，北方守护神"),
        PetConfig(35, "麒麟", "LEGEND", "FEED", 2.2, 30, 55, 180000, "🦄", "综合辅助，祥瑞之兽"),

        // === 神话宠物 ===
        PetConfig(41, "星灵猫", "MYTH", "HARVEST", 2.5, 50, 60, 300000, "⭐", "全图自动收获，星辰之灵"),
        PetConfig(42, "月神狐", "MYTH", "PLANT", 2.5, 50, 62, 350000, "🌙", "全图自动播种，月光化身"),
        PetConfig(43, "森林精灵", "MYTH", "WATER", 2.5, 50, 65, 400000, "🧚", "自动浇水，自然之力"),
        PetConfig(44, "机械犬", "MYTH", "CARRY", 3.0, 60, 68, 450000, "🤖", "自动运输，钢铁意志"),
        PetConfig(45, "机械猫", "MYTH", "COLLECT", 3.0, 60, 70, 480000, "🤖", "自动仓储，精密计算"),
        PetConfig(46, "云雀", "MYTH", "EXPLORE", 2.8, 80, 72, 500000, "☁️", "天气控制，云端飞翔"),
        PetConfig(47, "小龙", "MYTH", "PROCESS", 3.0, 50, 75, 600000, "🐲", "自动加工，幼年神龙"),
        PetConfig(48, "独角兽", "MYTH", "HARVEST", 3.0, 60, 78, 700000, "🦄", "传奇品质提升，圣洁生灵"),
        PetConfig(49, "光辉凤凰", "MYTH", "PROCESS", 3.2, 70, 80, 800000, "🌟", "生产效率提升，涅槃重生"),
        PetConfig(50, "世界树精灵", "MYTH", "PROCESS", 4.0, 100, 90, 2000000, "🌳", "全农场管理，世界树化身")
    )

    fun getById(id: Int): PetConfig? = ALL.firstOrNull { it.petId == id }

    fun getByUnlockLevel(level: Int): List<PetConfig> = ALL.filter { it.unlockLevel <= level }
}
