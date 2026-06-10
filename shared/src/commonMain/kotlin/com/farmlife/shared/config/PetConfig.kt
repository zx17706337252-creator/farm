package com.farmlife.shared.config

/**
 * 宠物配置
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
    // 工作类型常量
    const val WORK_HARVEST = "HARVEST"
    const val WORK_COLLECT = "COLLECT"
    const val WORK_TRANSPORT = "TRANSPORT"
    const val WORK_FEED = "FEED"
    
    val ALL = listOf(
        // 基础宠物 (1-5)
        PetConfig(1, "小狗", "基础", WORK_HARVEST, 1.0, 5, 12, 1000, "🐕", "忠诚的伙伴，会帮你收获作物"),
        PetConfig(2, "小猫", "基础", WORK_COLLECT, 1.2, 3, 12, 1000, "🐱", "灵活的小猫，擅长收集物品"),
        PetConfig(3, "小鸡", "基础", WORK_COLLECT, 0.8, 10, 12, 500, "🐤", "勤快的小鸡，总是在找东西"),
        PetConfig(4, "小羊", "基础", WORK_FEED, 0.9, 5, 15, 2000, "🐑", "温柔的小羊，会帮你喂养动物"),
        PetConfig(5, "小猪", "基础", WORK_TRANSPORT, 1.1, 8, 15, 2000, "🐷", "活泼的小猪，运输小能手"),
        
        // 进阶宠物 (11-15)
        PetConfig(11, "牧羊犬", "进阶", WORK_HARVEST, 1.5, 15, 25, 10000, "🐕‍🦺", "专业的农场助手，效率极高"),
        PetConfig(12, "猎豹", "进阶", WORK_TRANSPORT, 2.0, 20, 30, 30000, "🐆", "速度惊人，运输速度翻倍"),
        PetConfig(13, "猫头鹰", "进阶", WORK_COLLECT, 1.8, 25, 28, 25000, "🦉", "夜间也能工作，收集范围大"),
        PetConfig(14, "马", "进阶", WORK_TRANSPORT, 1.8, 18, 26, 20000, "🐴", "忠诚可靠，运输量大"),
        PetConfig(15, "兔子精灵", "进阶", WORK_FEED, 1.6, 12, 32, 35000, "🐰", "魔法生物，喂养效率提升"),
        
        // 高级宠物 (21-25)
        PetConfig(21, "神犬", "高级", WORK_HARVEST, 2.5, 30, 45, 100000, "🐕", "神话级牧羊犬，收获全自动"),
        PetConfig(22, "天马", "高级", WORK_TRANSPORT, 3.0, 40, 50, 200000, "🦄", "翱翔天际，瞬移运输"),
        PetConfig(23, "精灵龙", "高级", WORK_HARVEST, 2.8, 35, 48, 180000, "🐉", "喷火烘干，快速成熟"),
        PetConfig(24, "玄鸟", "高级", WORK_COLLECT, 3.2, 50, 55, 250000, "🦅", "极速收集，范围全图"),
        PetConfig(25, "九尾狐", "高级", WORK_FEED, 2.6, 25, 52, 220000, "🦊", "妖魅之姿，喂养效率极高"),
        
        // 传说宠物 (31-35)
        PetConfig(31, "哮天犬", "传说", WORK_HARVEST, 4.0, 60, 65, 500000, "🐕", "天界神犬，收获范围覆盖全农场"),
        PetConfig(32, "金翅大鹏", "传说", WORK_TRANSPORT, 5.0, 80, 70, 800000, "🦅", "扶摇直上九万里，瞬息到达"),
        PetConfig(33, "麒麟", "传说", WORK_HARVEST, 4.5, 70, 68, 700000, "🦄", "祥瑞之兽，万物生长加速"),
        PetConfig(34, "白虎", "传说", WORK_FEED, 4.2, 50, 72, 900000, "🐯", "百兽之王，喂养效率提升300%"),
        PetConfig(35, "青龙", "传说", WORK_HARVEST, 5.0, 100, 75, 1000000, "🐉", "东方神兽，农场时间加速50%")
    )
    
    fun getById(id: Int): PetConfig? = ALL.find { it.petId == id }
}
