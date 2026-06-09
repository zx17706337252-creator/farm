package com.farmlife.app.data.model

/** 区域枚举 - 七大核心区域 */
enum class Region(val displayName: String, val unlockLevel: Int) {
    FARMLAND("农田区", 1),
    LIVESTOCK("畜牧区", 10),
    ORCHARD("果园区", 20),
    PROCESSING("加工区", 30),
    FISHPOND("鱼塘区", 40),
    PET_ESTATE("宠物庄园", 50),
    MUSEUM("收藏馆", 60)
}
