# 呆柳的农场 - Flutter 跨平台版 ✅

一个完整的农场模拟游戏，支持 Android 和 iOS 双平台。

## 📱 核心模块清单

| 模块 | 入口 | 说明 |
|------|------|------|
| 世界地图 | `world_map_screen.dart` | 可缩放平移的农场全景，9 个模块入口 + 呼吸光晕动画 |
| 农田 | `farmland_screen.dart` | 种植/收获系统，品质随机，作物成熟动画 |
| 牧场 | `livestock_screen.dart` | 购买动物，定时产出产品，可收获 |
| 果园 | `orchard_screen.dart` | 果树种植，定时结果 |
| 工坊 | `factory_screen.dart` | 原材料加工成高级产品，配方系统 |
| 鱼塘 | `fish_pond_screen.dart` | 鱼类养殖与收获 |
| 商店 | `shop_screen.dart` | 分类 Tab 商店（种子/动物/果树/鱼苗/宠物） |
| 仓库 | `inventory_screen.dart` | 物品总览 + 批量出售 |
| 图鉴 | `collection_screen.dart` | 收藏进度，品质标记，解锁状态 |
| 宠物 | `pet_screen.dart` | 宠物养成，工作/休息状态 |

## 🏗️ 架构设计

```
lib/
├── main.dart                      # 应用入口
├── navigation/app_router.dart     # 路由（带动画过渡）
├── theme/app_theme.dart           # 色彩系统 + 品质颜色
├── widgets/
│   ├── common_widgets.dart        # 农场通用组件（卡片/按钮/状态条/品质徽章/浮动奖励动画）
│   └── ui_components.dart         # 备用基础组件
├── models/
│   ├── game_models.dart           # Player/Crop/Animal/Pet/Tree/Fish/InventoryItem 等
│   ├── quality.dart               # 品质枚举（6 级）
│   └── season_weather.dart        # 季节 & 天气枚举
├── configs/game_configs.dart      # 作物/动物/果树/鱼类/宠物/工坊配方 配置表
├── systems/
│   ├── leveling_system.dart       # 玩家/动物/宠物/建筑等级曲线
│   ├── quality_system.dart        # 品质概率计算 & 季节天气倍率
│   └── time_system.dart           # 游戏时间、季节、天气
├── providers/game_providers.dart  # Riverpod + GameEngine（所有游戏逻辑）
└── repository/game_repository.dart # SharedPreferences 持久化
```

## 🚀 构建与安装

```bash
# 获取依赖
flutter pub get

# Android 构建
flutter build apk           # 正式 APK
flutter run                 # 连接设备后直接运行

# iOS 构建（需要 macOS + Xcode）
flutter build ios
flutter run
```

## 🎮 玩法说明

1. **初始金币**：100 金币，Lv.1
2. **作物种植**：在农田选择种子 → 点击地块种植 → 等待成熟 → 收获
3. **品质系统**：普通(灰) → 优良(绿) → 稀有(蓝) → 史诗(紫) → 传说(橙) → 神话(粉)，每级加成收益倍率
4. **出售**：收获的物品存入仓库，可在仓库中一键出售
5. **解锁**：随着等级提升，更多高级作物、动物、配方会解锁

## 🐛 已知修复

- ✅ `FutureProvider` 异步访问修复（`.future` 访问器）
- ✅ `Provider<Future<*>>` 旧范式重构为标准 `FutureProvider`
- ✅ `GameRepository` 懒加载单例，避免重复初始化
- ✅ 模块页面路由统一使用动画过渡（淡入+缩放）
- ✅ 空列表占位（如还没有动物/作物时显示友好提示）

## 📦 依赖版本

```yaml
environment:
  sdk: ">=3.0.0 <4.0.0"
  flutter: ">=3.10.0"

dependencies:
  flutter_riverpod: ^2.4.9     # 状态管理
  shared_preferences: ^2.2.2   # 本地存储
  google_fonts: ^6.1.0         # 字体
  intl: ^0.18.1                 # 国际化工具
  uuid: ^4.3.3                  # ID 生成
```

---

**祝你农场经营愉快！🌾🐄🍎**
