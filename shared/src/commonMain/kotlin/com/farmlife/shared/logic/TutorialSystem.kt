package com.farmlife.shared.logic

/**
 * 教程系统
 */
object TutorialSystem {
    
    /**
     * 教程步骤
     */
    data class Step(
        val id: Int,
        val title: String,
        val content: String,
        val region: String? = null,
        val action: String? = null,
        val order: Int
    )
    
    val STEPS = listOf(
        Step(
            id = 1,
            title = "欢迎来到呆柳的农场！",
            content = "这是一个属于你的农场世界。通过点击地图上的区域，你可以进入不同的功能模块。",
            action = "VIEW_MAP",
            order = 1
        ),
        Step(
            id = 2,
            title = "开始种植",
            content = "进入农田区域，点击空土地选择一个作物来种植。作物成熟后记得收获哦！",
            region = "FARMLAND",
            action = "PLANT_CROP",
            order = 2
        ),
        Step(
            id = 3,
            title = "收获作物",
            content = "作物成熟后会显示金光闪闪的效果。点击已成熟的作物进行收获，获得金币和经验！",
            region = "FARMLAND",
            action = "HARVEST_CROP",
            order = 3
        ),
        Step(
            id = 4,
            title = "扩建农场",
            content = "随着等级提升，你可以扩建更多土地来种植更多作物。在农田界面点击扩建按钮。",
            region = "FARMLAND",
            action = "EXPAND_FARM",
            order = 4
        ),
        Step(
            id = 5,
            title = "解锁牧场",
            content = "达到10级后可以解锁牧场。牧场可以养殖动物，动物会产出各种产品。",
            region = "LIVESTOCK",
            action = "UNLOCK_LIVESTOCK",
            order = 5
        ),
        Step(
            id = 6,
            title = "解锁宠物庄园",
            content = "宠物可以帮你自动收获、收集物品和喂养动物。达到12级后记得购买宠物！",
            region = "PET_ESTATE",
            action = "UNLOCK_PETS",
            order = 6
        ),
        Step(
            id = 7,
            title = "工坊加工",
            content = "工坊可以将初级产品加工成更值钱的商品。建立你的加工产业链吧！",
            region = "FACTORY",
            action = "UNLOCK_FACTORY",
            order = 7
        ),
        Step(
            id = 8,
            title = "完成图鉴",
            content = "收集各种物品来解锁图鉴。图鉴完成度越高，你的收藏分数就越高！",
            region = "MUSEUM",
            action = "COLLECT_ITEMS",
            order = 8
        )
    )
    
    /**
     * 获取下一步教程
     */
    fun getNextStep(completedIds: Set<Int>): Step? {
        return STEPS.filterNot { it.id in completedIds }
            .minByOrNull { it.order }
    }
    
    /**
     * 检查是否应该显示教程
     */
    fun shouldShowTutorial(completedIds: Set<Int>): Boolean {
        return getNextStep(completedIds) != null
    }
}
