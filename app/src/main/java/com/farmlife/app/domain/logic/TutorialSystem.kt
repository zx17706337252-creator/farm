package com.farmlife.app.domain.logic

/**
 * 新手引导系统
 */
object TutorialSystem {
    // 引导步骤定义
    enum class Step(
        val id: Int,
        val title: String,
        val description: String,
        val triggerLevel: Int = 0,
        val triggerAction: String = ""
    ) {
        WELCOME(0, "🌾 欢迎来到农场！", "点击土地可以种植作物，收获后获得金币和经验", 0, "start"),
        PLANT(1, "🌱 种植教程", "选择一块空地，点击后选择种子进行种植", 0, "plant"),
        HARVEST(2, "✂️ 收获教程", "作物成熟后点击收获，获得金币和经验", 0, "harvest"),
        SHOP(3, "🏪 商店教程", "在商店可以购买种子、动物、宠物和建筑", 1, "shop"),
        ANIMAL(4, "🐄 养殖教程", "购买动物后会自动生产产品", 10, "unlock_animal"),
        PET(5, "🐕 宠物教程", "宠物可以自动帮忙收菜和收集产品", 12, "buy_pet"),
        FACTORY(6, "🏭 加工教程", "将原料加工成成品可以获得更高收益", 30, "unlock_factory"),
        ORCHARD(7, "🍎 果园教程", "果园种植果树，持续产出水果", 20, "unlock_orchard"),
        FISHPOND(8, "🐟 鱼塘教程", "鱼塘可以养鱼，获得稀有鱼类", 40, "unlock_fishpond"),
        COLLECTION(9, "📚 收藏馆", "收集所有作物、动物、宠物完成图鉴", 1, "collection"),
        PET_ESTATE(10, "🏠 宠物庄园", "建造宠物设施提升宠物能力", 50, "unlock_pet_estate"),
        END(99, "🎉 恭喜！", "你已经掌握了农场的基本玩法，开始你的农场之旅吧！", 0, "complete");

        fun isUnlocked(level: Int): Boolean = level >= triggerLevel
    }

    fun getNextStep(currentStep: Int, playerLevel: Int): Step? {
        val steps = values().filter { it.id > currentStep && it.isUnlocked(playerLevel) }
        return steps.minByOrNull { it.id }
    }

    fun getAllSteps(): List<Step> = values().toList()

    fun getStepById(id: Int): Step? = values().firstOrNull { it.id == id }

    fun isStepCompleted(completedSteps: String, stepId: Int): Boolean {
        return completedSteps.contains(",$stepId,")
    }

    fun markStepCompleted(completedSteps: String, stepId: Int): String {
        if (isStepCompleted(completedSteps, stepId)) return completedSteps
        return "$completedSteps,$stepId,"
    }
}
