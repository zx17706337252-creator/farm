package com.farmlife.app.data.enums

enum class RegionType(
    val displayName: String,
    val unlockLevel: Int,
    val description: String
) {
    FARM_FIELD("农田区", 1, "种植作物的地方"),
    LIVESTOCK("畜牧区", 10, "饲养鸡、牛、羊、蜜蜂"),
    ORCHARD("果园区", 20, "种植苹果树、樱桃树、梨树"),
    PROCESSING("加工区", 30, "面包坊、乳品厂、果酱坊"),
    FISH_POND("鱼塘区", 40, "养鱼系统"),
    PET_MANOR("宠物庄园", 50, "宠物宿舍、训练场、医院"),
    COLLECTION_HALL("收藏馆", 60, "展示稀有物品与收藏");
}
