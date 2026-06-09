package com.farmlife.app.data.model

/** 作物分类 */
enum class CropCategory(val displayName: String) {
    GRAIN("基础粮食"),
    VEGETABLE("蔬菜"),
    FRUIT("水果"),
    CASH("经济作物"),
    ADVANCED("高级作物"),
    FLOWER("花卉"),
    SPECIAL("特殊作物"),
    LEGENDARY("传奇作物")
}

/** 动物分类 */
enum class AnimalCategory(val displayName: String) {
    BASIC("初级动物"),
    MID("中级动物"),
    ADVANCED("高级动物"),
    SPECIAL("特殊动物")
}

/** 宠物分类 */
enum class PetCategory(val displayName: String) {
    BASIC("基础宠物"),
    ADVANCED("进阶宠物"),
    HIGH("高级宠物"),
    LEGEND("传说宠物"),
    MYTH("神话宠物")
}

/** 宠物工作类型 */
enum class PetWorkType(val displayName: String) {
    HARVEST("收获"),
    PLANT("播种"),
    WATER("浇水"),
    FERTILIZE("施肥"),
    CARRY("搬运"),
    FEED("喂养"),
    COLLECT("收集"),
    EXPLORE("探险"),
    PROCESS("加工")
}

/** 建筑分类 */
enum class BuildingCategory(val displayName: String) {
    TIER1("一级加工"),
    TIER2("二级加工"),
    TIER3("三级加工"),
    STORAGE("仓储"),
    ANIMAL("动物建筑"),
    DECORATION("装饰"),
    SPECIAL("特殊建筑")
}
