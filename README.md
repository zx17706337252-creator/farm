# Farm Life - 我的小农场 (V3.0)

一款单机本地存档的农场模拟经营 Android 应用。
基于 Jetpack Compose + Room + Kotlin Coroutines 构建。

## 项目结构

```
com.farmlife.app/
├─ FarmLifeApplication.kt          # 应用入口，初始化 Repository 与 Engine
├─ MainActivity.kt                 # 主 Activity，加载 Compose UI
├─ config/                         # 静态游戏配置数据
│  ├─ CropConfig.kt                # 50种作物配置（粮食/蔬菜/水果/经济/高级/花卉/特殊/传奇）
│  ├─ AnimalConfig.kt              # 20种动物配置（初级/中级/高级/特殊）
│  ├─ PetConfig.kt                 # 30种宠物配置（基础/进阶/高级/传说/神话）
│  └─ FactoryConfig.kt             # 15座加工建筑 + 配方
├─ data/
│  ├─ entity/                      # Room 实体（14张表）
│  │  ├─ PlayerEntity              # 玩家主体
│  │  ├─ PlayerStatisticsEntity    # 玩家统计
│  │  ├─ LandInstanceEntity        # 土地实例
│  │  ├─ CropInstanceEntity        # 作物实例
│  │  ├─ AnimalInstanceEntity      # 动物实例
│  │  ├─ PetInstanceEntity         # 宠物实例
│  │  ├─ PetMissionEntity          # 宠物任务（探险/工作）
│  │  ├─ InventoryItemEntity       # 仓库物品
│  │  ├─ FactoryInstanceEntity     # 工厂实例
│  │  ├─ ProductionQueueEntity     # 生产队列
│  │  ├─ CollectionRecordEntity    # 收藏记录
│  │  ├─ AchievementEntity         # 成就
│  │  ├─ FarmLogEntity             # 农场日志
│  │  └─ GameSettingsEntity        # 系统设置
│  ├─ dao/                         # Room DAO 层
│  └─ database/                    # FarmLifeDatabase
├─ domain/
│  ├─ logic/                       # 游戏核心逻辑
│  │  ├─ LevelingSystem            # 成长公式：玩家/土地/动物/宠物/建筑
│  │  ├─ QualitySystem             # 品质系统（普通→神话，6级）
│  │  ├─ TimeSystem                # 时间/季节/天气
│  │  └─ OfflineProgressionSystem  # 离线收益计算
│  ├─ model/                       # Domain 模型：Quality/Season/Region 等枚举
│  ├─ repository/                  # FarmLifeRepository - 统一数据入口
│  └─ engine/                      # FarmEngine - 游戏主引擎，驱动全部交互
└─ ui/
   ├─ screens/                     # 六大区域屏幕
   │  ├─ MainAppScreen             # 应用主框架 + 底部导航
   │  ├─ FarmlandScreen            # 农田区（土地网格+种植+收获）
   │  ├─ LivestockScreen           # 牧场区（动物+生产）
   │  ├─ FactoryScreen             # 加工区（工厂+配方+生产队列）
   │  ├─ ShopScreen                # 商店（种子/动物/宠物/建筑）
   │  ├─ InventoryScreen           # 仓库（库存管理+出售）
   │  └─ CollectionScreen          # 图鉴/收藏馆
   └─ theme/                       # FarmLife 主题配色
```

## 核心玩法
- **种植**：50种作物按现实时间生长，品质随机生成，暴击收获
- **养殖**：20种动物，亲密度系统，稀有掉落，独立等级成长
- **宠物**：30种宠物，自动工作（收菜/收蛋/运输/加工/探险），核心自动化机制
- **加工**：15座建筑+多条产业链（小麦→面粉→面包→三明治→高级套餐）
- **经济**：金币循环（种植→加工→出售→扩建→升级）
- **图鉴**：收藏驱动后期玩法，记录全部收获历史

## 构建
- 需要 Android Studio Giraffe 及以上
- JDK 17
- compileSdk 34, minSdk 24
- 使用 Gradle Wrapper 构建

## 存档
- 单机本地 Room 数据库，无需联网
- 自动保存（每次操作后）
- 离线收益上限 8 小时
