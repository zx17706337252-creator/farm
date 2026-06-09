import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';
import '../models/game_models.dart';

class ShopScreen extends ConsumerStatefulWidget {
  const ShopScreen({super.key});

  @override
  ConsumerState<ShopScreen> createState() => _ShopScreenState();
}

class _ShopScreenState extends ConsumerState<ShopScreen> {
  int _tabIndex = 0;
  static const _tabs = [
    ('种子', '🌱', 0),
    ('动物', '🐣', 1),
    ('果树', '🌳', 2),
    ('鱼苗', '🐠', 3),
    ('宠物', '🐾', 4),
  ];

  @override
  Widget build(BuildContext context) {
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🏪 商店',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFFFF8E1), Color(0xFFFFECB3), Color(0xFFFFE082)],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
      ),
      body: SafeArea(
        child: Column(
          children: [
            // Tab bar
            Padding(
              padding: const EdgeInsets.all(16),
              child: GlassCard(
                padding: const EdgeInsets.all(4),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: _tabs.map((t) {
                    final selected = _tabIndex == t.$3;
                    return Expanded(
                      child: GestureDetector(
                        onTap: () => setState(() => _tabIndex = t.$3),
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 250),
                          padding: const EdgeInsets.symmetric(vertical: 10),
                          decoration: BoxDecoration(
                            color: selected
                                ? Colors.amber.shade400
                                : Colors.transparent,
                            borderRadius: BorderRadius.circular(12),
                            boxShadow: selected
                                ? [
                                    BoxShadow(
                                      color:
                                          Colors.amber.withOpacity(0.4),
                                      blurRadius: 8,
                                    )
                                  ]
                                : null,
                          ),
                          child: Column(
                            children: [
                              Text(t.$2, style: const TextStyle(fontSize: 18)),
                              const SizedBox(height: 2),
                              Text(
                                t.$1,
                                style: TextStyle(
                                  fontSize: 10,
                                  fontWeight: FontWeight.w700,
                                  color: selected
                                      ? Colors.white
                                      : Colors.amber.shade900,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
                child: _buildTabContent(playerAsync),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTabContent(AsyncValue<Player> playerAsync) {
    switch (_tabIndex) {
      case 0:
        return _ShopList(
          title: '🌱 种子商店',
          color: const Color(0xFF5DAF54),
          items: CropConfigs.all
              .map((c) => _ShopItem(
                    icon: c.icon,
                    name: c.name,
                    price: c.sellPrice * 2,
                    desc: '生长: ${c.growTimeSeconds}秒',
                    color: Colors.green,
                    onBuy: () async {
                      final engine = await ref.read(gameEngineProvider.future);
                      final player = playerAsync.valueOrNull;
                      if (player == null) return;
                      final lands = await ref.read(landsProvider.future);
                      if (lands.isEmpty) return;
                      final ok = await engine.plantCrop(lands.first, c, player);
                      if (ok) {
                        ref.invalidate(playerProvider);
                      }
                    },
                  ))
              .toList(),
        );
      case 1:
        return _ShopList(
          title: '🐣 动物商店',
          color: const Color(0xFFD98C4F),
          items: AnimalConfigs.all
              .map((a) => _ShopItem(
                    icon: a.icon,
                    name: a.name,
                    price: a.purchasePrice,
                    desc: '产出: ${a.productName}',
                    color: Colors.orange,
                    onBuy: () async {
                      final engine = await ref.read(gameEngineProvider.future);
                      final player = playerAsync.valueOrNull;
                      if (player == null) return;
                      final ok = await engine.buyAnimal(a, player);
                      if (ok) {
                        ref.invalidate(playerProvider);
                        ref.invalidate(animalsProvider);
                      }
                    },
                  ))
              .toList(),
        );
      case 2:
        return _ShopList(
          title: '🌳 果树商店',
          color: const Color(0xFFE57373),
          items: TreeConfigs.all
              .map((t) => _ShopItem(
                    icon: t.icon,
                    name: t.name,
                    price: t.sellPrice,
                    desc: '经验: ${t.exp}',
                    color: Colors.red,
                    onBuy: () async {
                      final engine = await ref.read(gameEngineProvider.future);
                      final player = playerAsync.valueOrNull;
                      if (player == null) return;
                      final ok = await engine.plantTree(0, 0, t, player);
                      if (ok) {
                        ref.invalidate(playerProvider);
                        ref.invalidate(treesProvider);
                      }
                    },
                  ))
              .toList(),
        );
      case 3:
        return _ShopList(
          title: '🐠 鱼苗商店',
          color: const Color(0xFF4FC3F7),
          items: FishConfigs.all
              .map((f) => _ShopItem(
                    icon: f.icon,
                    name: f.name,
                    price: f.sellPrice,
                    desc: '经验: ${f.exp}',
                    color: Colors.blue,
                    onBuy: () async {
                      final engine = await ref.read(gameEngineProvider.future);
                      final player = playerAsync.valueOrNull;
                      if (player == null) return;
                      final pond = Pond(id: 'demo', x: 0, y: 0, unlocked: true);
                      await engine.addFish(pond, f, player);
                      ref.invalidate(playerProvider);
                      ref.invalidate(fishProvider);
                    },
                  ))
              .toList(),
        );
      case 4:
        return _ShopList(
          title: '🐾 宠物商店',
          color: const Color(0xFFFF8A80),
          items: PetConfigs.all
              .map((p) => _ShopItem(
                    icon: p.icon,
                    name: p.name,
                    price: p.purchasePrice,
                    desc: p.description,
                    color: Colors.pink,
                    onBuy: () async {
                      final engine = await ref.read(gameEngineProvider.future);
                      final player = playerAsync.valueOrNull;
                      if (player == null) return;
                      final ok = await engine.buyPet(p, player);
                      if (ok) {
                        ref.invalidate(playerProvider);
                        ref.invalidate(petsProvider);
                      }
                    },
                  ))
              .toList(),
        );
    }
    return const SizedBox.shrink();
  }
}

class _ShopList extends StatelessWidget {
  final String title;
  final Color color;
  final List<_ShopItem> items;

  const _ShopList(
      {required this.title, required this.color, required this.items});

  @override
  Widget build(BuildContext context) {
    return GlassCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(title,
              style: TextStyle(
                  fontWeight: FontWeight.w800, fontSize: 15, color: color)),
          const SizedBox(height: 10),
          ...items,
        ],
      ),
    );
  }
}

class _ShopItem extends StatelessWidget {
  final String icon;
  final String name;
  final int price;
  final String desc;
  final Color color;
  final VoidCallback onBuy;

  const _ShopItem({
    required this.icon,
    required this.name,
    required this.price,
    required this.desc,
    required this.color,
    required this.onBuy,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Container(
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: color.withOpacity(0.08),
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: color.withOpacity(0.3)),
        ),
        child: Row(
          children: [
            Text(icon, style: const TextStyle(fontSize: 28)),
            const SizedBox(width: 10),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(name,
                      style: TextStyle(
                          fontWeight: FontWeight.w700,
                          fontSize: 13,
                          color: color)),
                  const SizedBox(height: 2),
                  Text(desc,
                      style: const TextStyle(fontSize: 10, color: Colors.black54)),
                ],
              ),
            ),
            ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                backgroundColor: color,
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
              onPressed: onBuy,
              icon: const Icon(Icons.add, size: 14),
              label: Text('$price💰', style: const TextStyle(fontSize: 12)),
            ),
          ],
        ),
      ),
    );
  }
}
