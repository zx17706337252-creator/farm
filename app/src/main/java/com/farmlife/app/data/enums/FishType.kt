package com.farmlife.app.data.enums

enum class FishType(
    val displayName: String,
    val emoji: String,
    val growthTimeSec: Int,
    val baseSellPrice: Int,
    val unlockLevel: Int
) {
    CARP("鲤鱼", "🐟", 600, 20, 40),
    GOLDFISH("金鱼", "🐠", 1200, 50, 43),
    TROUT("鳟鱼", "🐡", 900, 35, 45),
    SALMON("三文鱼", "🍣", 1500, 65, 48),
    TUNA("金枪鱼", "🐟", 2100, 90, 52),
    EEL("鳗鱼", "🐍", 1800, 75, 55),
    OCTOPUS("章鱼", "🐙", 2700, 120, 60),
    LOBSTER("龙虾", "🦞", 3600, 180, 65),
    CRAB("螃蟹", "🦀", 3000, 150, 68),
    SHARK("鲨鱼", "🦈", 7200, 500, 80);
}
