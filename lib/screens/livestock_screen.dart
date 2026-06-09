import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../theme/app_theme.dart';
import '../widgets/common_widgets.dart';

class LivestockScreen extends ConsumerStatefulWidget {
  const LivestockScreen({super.key});

  @override
  ConsumerState<LivestockScreen> createState() => _LivestockScreenState();
}

class _LivestockScreenState extends ConsumerState<LivestockScreen>
    with TickerProviderStateMixin {
  late final AnimationController _wiggle;

  @override
  void initState() {
    super.initState();
    _wiggle = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1500),
    )..repeat(reverse: true);
  }

  @override
  void dispose() {
    _wiggle.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final animalsAsync = ref.watch(animalsProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🐄 牧场',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCCBC)],
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
              // 可购买的动物
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🐾 购买动物',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF5D4037),
                      ),
                    ),
                    const SizedBox(height: 10),
                    SizedBox(
                      height: 130,
                      child: ListView.separated(
                        scrollDirection: Axis.horizontal,
                        itemCount: AnimalConfigs.all.take(6).length,
                        separatorBuilder: (_, __) => const SizedBox(width: 10),
                        itemBuilder: (_, i) {
                          final config = AnimalConfigs.all[i];
                          return Container(
                            width: 110,
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              gradient: LinearGradient(
                                colors: [
                                  Colors.orange.shade50,
                                  Colors.orange.shade100
                                ],
                                begin: Alignment.topLeft,
                                end: Alignment.bottomRight,
                              ),
                              borderRadius: BorderRadius.circular(16),
                              border: Border.all(color: Colors.orange.shade200),
                            ),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                AnimatedBuilder(
                                  animation: _wiggle,
                                  builder: (_, __) {
                                    return Transform.rotate(
                                      angle: 0.12 * _wiggle.value,
                                      child: Text(config.icon,
                                          style: const TextStyle(fontSize: 36)),
                                    );
                                  },
                                ),
                                const SizedBox(height: 6),
                                Text(config.name,
                                    style: const TextStyle(
                                        fontSize: 12,
                                        fontWeight: FontWeight.w700,
                                        color: Color(0xFF5D4037))),
                                const SizedBox(height: 3),
                                Text('产出: ${config.productName}',
                                    style: const TextStyle(
                                        fontSize: 9, color: Colors.brown)),
                                const SizedBox(height: 6),
                                SizedBox(
                                  height: 26,
                                  child: ElevatedButton.icon(
                                    style: ElevatedButton.styleFrom(
                                      backgroundColor:
                                          const Color(0xFFD98C4F),
                                      foregroundColor: Colors.white,
                                      elevation: 2,
                                      padding: const EdgeInsets.symmetric(
                                          horizontal: 6),
                                      shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                    ),
                                    onPressed: () async {
                                      final engine =
                                          await ref.read(gameEngineProvider.future);
                                      final player = playerAsync.valueOrNull;
                                      if (player == null) return;
                                      final ok = await engine.buyAnimal(config, player);
                                      if (!ok && context.mounted) {
                                        ScaffoldMessenger.of(context).showSnackBar(
                                          const SnackBar(
                                              content: Text('金币不足或等级不够')),
                                        );
                                      } else {
                                        ref.invalidate(playerProvider);
                                        ref.invalidate(animalsProvider);
                                      }
                                    },
                                    icon: const Icon(Icons.add, size: 12),
                                    label: Text('${config.purchasePrice}💰',
                                        style: const TextStyle(fontSize: 11)),
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 16),
              // 我的动物
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🏠 我的动物',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF5D4037),
                      ),
                    ),
                    const SizedBox(height: 10),
                    animalsAsync.when(
                      data: (animals) {
                        if (animals.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(24.0),
                              child: Text('还没有动物，先购买一只吧！',
                                  style: TextStyle(color: Colors.black54)),
                            ),
                          );
                        }
                        return GridView.builder(
                          shrinkWrap: true,
                          physics: const NeverScrollableScrollPhysics(),
                          gridDelegate:
                              const SliverGridDelegateWithFixedCrossAxisCount(
                            crossAxisCount: 3,
                            mainAxisSpacing: 10,
                            crossAxisSpacing: 10,
                            childAspectRatio: 0.9,
                          ),
                          itemCount: animals.length,
                          itemBuilder: (ctx, i) {
                            final a = animals[i];
                            final config = AnimalConfigs.all.firstWhere(
                                (e) => e.animalId == a.animalId,
                                orElse: () => AnimalConfigs.all.first);
                            return GestureDetector(
                              onTap: () async {
                                final engine =
                                    await ref.read(gameEngineProvider.future);
                                final player = playerAsync.valueOrNull;
                                if (player == null) return;
                                final r = await engine.collectAnimalProduct(a, player);
                                if (r != null && context.mounted) {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    SnackBar(
                                      content: Text('获得 ${r.$1} 金币'),
                                      backgroundColor: Colors.green,
                                    ),
                                  );
                                  ref.invalidate(playerProvider);
                                  ref.invalidate(animalsProvider);
                                }
                              },
                              child: AnimatedBuilder(
                                animation: _wiggle,
                                builder: (_, __) {
                                  return Container(
                                    padding: const EdgeInsets.all(8),
                                    decoration: BoxDecoration(
                                      gradient: LinearGradient(
                                        colors: [
                                          Colors.amber.shade50,
                                          Colors.amber.shade100
                                        ],
                                      ),
                                      borderRadius: BorderRadius.circular(14),
                                      border: Border.all(
                                          color: Colors.amber.shade300),
                                    ),
                                    child: Column(
                                      mainAxisAlignment: MainAxisAlignment.center,
                                      children: [
                                        Transform.scale(
                                          scale: 0.95 + 0.1 * _wiggle.value,
                                          child: Text(config.icon,
                                              style:
                                                  const TextStyle(fontSize: 32)),
                                        ),
                                        const SizedBox(height: 4),
                                        Text(config.name,
                                            style: const TextStyle(
                                                fontSize: 11,
                                                fontWeight: FontWeight.w700,
                                                color: Color(0xFF5D4037))),
                                        const SizedBox(height: 2),
                                        Text('Lv.${a.level}',
                                            style: const TextStyle(
                                                fontSize: 10,
                                                color: Colors.brown)),
                                        const SizedBox(height: 4),
                                        Container(
                                          padding: const EdgeInsets.symmetric(
                                              horizontal: 6, vertical: 2),
                                          decoration: BoxDecoration(
                                            color: Colors.green.shade100,
                                            borderRadius:
                                                BorderRadius.circular(8),
                                          ),
                                          child: const Text(
                                            '点击收集',
                                            style: TextStyle(
                                              fontSize: 9,
                                              color: Colors.green,
                                              fontWeight: FontWeight.w700,
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  );
                                },
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
}
