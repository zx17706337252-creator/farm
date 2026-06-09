import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';

class InventoryScreen extends ConsumerStatefulWidget {
  const InventoryScreen({super.key});

  @override
  ConsumerState<InventoryScreen> createState() => _InventoryScreenState();
}

class _InventoryScreenState extends ConsumerState<InventoryScreen> {
  @override
  Widget build(BuildContext context) {
    final inventoryAsync = ref.watch(inventoryProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '📦 仓库',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFFFF8E1), Color(0xFFD7CCC8), Color(0xFFBCAAA4)],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.fromLTRB(16, 20, 16, 24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Row(
                children: [
                  Expanded(
                    child: GlassCard(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 14, vertical: 12),
                      child: Column(
                        children: [
                          const Text('物品总数',
                              style: TextStyle(
                                  fontSize: 11, color: Colors.black54)),
                          const SizedBox(height: 4),
                          Text(
                            '${inventoryAsync.valueOrNull?.length ?? 0}',
                            style: const TextStyle(
                              fontSize: 22,
                              fontWeight: FontWeight.w800,
                              color: Color(0xFF5D4037),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: GlassCard(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 14, vertical: 12),
                      child: Column(
                        children: [
                          const Text('金币',
                              style: TextStyle(
                                  fontSize: 11, color: Colors.black54)),
                          const SizedBox(height: 4),
                          Text(
                            '${playerAsync.valueOrNull?.gold ?? 0}💰',
                            style: const TextStyle(
                              fontSize: 22,
                              fontWeight: FontWeight.w800,
                              color: Color(0xFFD4A017),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          '📦 仓库物品',
                          style: TextStyle(
                            fontWeight: FontWeight.w800,
                            fontSize: 15,
                            color: Color(0xFF5D4037),
                          ),
                        ),
                        ElevatedButton.icon(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.brown.shade400,
                            foregroundColor: Colors.white,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                          onPressed: () async {
                            final engine =
                                await ref.read(gameEngineProvider.future);
                            final player = playerAsync.valueOrNull;
                            if (player == null) return;
                            final total =
                                await engine.sellAllInventory(player);
                            if (context.mounted && total > 0) {
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(
                                  content: Text('出售获得 $total 金币'),
                                  backgroundColor: Colors.green,
                                ),
                              );
                            }
                            ref.invalidate(playerProvider);
                            ref.invalidate(inventoryProvider);
                          },
                          icon: const Icon(Icons.sell, size: 14),
                          label: const Text('全部出售',
                              style: TextStyle(fontSize: 12)),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    inventoryAsync.when(
                      data: (items) {
                        if (items.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(32.0),
                              child: Text('仓库空空如也',
                                  style: TextStyle(color: Colors.black54)),
                            ),
                          );
                        }
                        return GridView.builder(
                          shrinkWrap: true,
                          physics: const NeverScrollableScrollPhysics(),
                          gridDelegate:
                              const SliverGridDelegateWithFixedCrossAxisCount(
                            crossAxisCount: 4,
                            mainAxisSpacing: 10,
                            crossAxisSpacing: 10,
                            childAspectRatio: 0.8,
                          ),
                          itemCount: items.length,
                          itemBuilder: (ctx, i) {
                            final item = items[i];
                            final icon = _iconFor(item.itemType, item.configId);
                            return Container(
                              padding: const EdgeInsets.all(6),
                              decoration: BoxDecoration(
                                color: Colors.brown.shade50,
                                borderRadius: BorderRadius.circular(12),
                                border: Border.all(
                                    color: Colors.brown.shade200,
                                    width: 1.5),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Text(icon,
                                      style: const TextStyle(fontSize: 26)),
                                  const SizedBox(height: 4),
                                  FittedBox(
                                    fit: BoxFit.scaleDown,
                                    child: Text(
                                      '×${item.quantity}',
                                      style: const TextStyle(
                                        fontSize: 11,
                                        fontWeight: FontWeight.w700,
                                        color: Color(0xFF5D4037),
                                      ),
                                    ),
                                  ),
                                  const SizedBox(height: 2),
                                  QualityBadge(
                                      quality: item.quality, compact: true),
                                ],
                              ),
                            );
                          },
                        );
                      },
                      loading: () => const Center(child: CircularProgressIndicator()),
                      error: (e, _) => Text('加载失败: $e'),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _iconFor(String type, int configId) {
    switch (type) {
      case 'CROP':
        return CropConfigs.all
                .firstWhere((e) => e.cropId == configId,
                    orElse: () => CropConfigs.all.first)
                .icon;
      case 'ANIMAL':
        return AnimalConfigs.all
                .firstWhere((e) => e.animalId == configId,
                    orElse: () => AnimalConfigs.all.first)
                .icon;
      case 'PROCESSED':
        return FactoryConfigs.recipes
                .firstWhere((e) => e.recipeId == configId,
                    orElse: () => FactoryConfigs.recipes.first)
                .icon;
      case 'TREE':
        return TreeConfigs.all
                .firstWhere((e) => e.treeId == configId,
                    orElse: () => TreeConfigs.all.first)
                .icon;
      case 'FISH':
        return FishConfigs.all
                .firstWhere((e) => e.fishId == configId,
                    orElse: () => FishConfigs.all.first)
                .icon;
      default:
        return '📦';
    }
  }
}
