import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';

class FactoryScreen extends ConsumerStatefulWidget {
  const FactoryScreen({super.key});

  @override
  ConsumerState<FactoryScreen> createState() => _FactoryScreenState();
}

class _FactoryScreenState extends ConsumerState<FactoryScreen> {
  @override
  Widget build(BuildContext context) {
    final queuesAsync = ref.watch(queuesProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🏭 工坊',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFEDE7F6), Color(0xFFD1C4E9), Color(0xFFB39DDB)],
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
              // 建造工坊
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: const [
                    Text(
                      '🏗️ 建造工坊',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF4527A0),
                      ),
                    ),
                    SizedBox(height: 8),
                    Text(
                      '共 ${10} 种工坊可用，加工原材料为成品',
                      style: TextStyle(fontSize: 12, color: Colors.black54),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 16),
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '⚙️ 配方',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF4527A0),
                      ),
                    ),
                    const SizedBox(height: 10),
                    ...FactoryConfigs.recipes.map((recipe) {
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.purple.shade50,
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: Colors.purple.shade100),
                          ),
                          child: Row(
                            children: [
                              Text(recipe.icon,
                                  style: const TextStyle(fontSize: 28)),
                              const SizedBox(width: 10),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(recipe.name,
                                        style: const TextStyle(
                                            fontWeight: FontWeight.w700,
                                            fontSize: 13,
                                            color: Color(0xFF4527A0))),
                                    const SizedBox(height: 2),
                                    Text(
                                      '产出: ${recipe.outputSellPrice}💰 · 经验: ${recipe.outputExp}',
                                      style: const TextStyle(
                                          fontSize: 10, color: Colors.black54),
                                    ),
                                  ],
                                ),
                              ),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.deepPurple,
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
                                  // 用一个空工厂触发
                                  final factory = FactoryConfigs.all.first;
                                  final fakeFactory = Factory(
                                    id: 'demo_factory',
                                    factoryType: factory.factoryId,
                                  );
                                  final ok = await engine.startProduction(
                                      fakeFactory, recipe, player);
                                  if (ok) {
                                    ref.invalidate(queuesProvider);
                                  } else if (context.mounted) {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                          content: Text('原材料不足'),
                                          backgroundColor: Colors.red),
                                    );
                                  }
                                },
                                child: const Text('加工',
                                    style: TextStyle(fontSize: 12)),
                              ),
                            ],
                          ),
                        ),
                      );
                    }),
                  ],
                ),
              ),
              const SizedBox(height: 16),
              // 加工队列
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '📦 加工队列',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF4527A0),
                      ),
                    ),
                    const SizedBox(height: 10),
                    queuesAsync.when(
                      data: (queues) {
                        if (queues.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(24.0),
                              child: Text('暂无加工任务',
                                  style: TextStyle(color: Colors.black54)),
                            ),
                          );
                        }
                        return Column(
                          children: queues.map((q) {
                            final recipe = FactoryConfigs.getRecipeById(q.recipeId);
                            final ready = q.isReady;
                            return Padding(
                              padding: const EdgeInsets.only(bottom: 6),
                              child: Container(
                                padding: const EdgeInsets.all(10),
                                decoration: BoxDecoration(
                                  color: ready
                                      ? Colors.green.shade50
                                      : Colors.grey.shade50,
                                  borderRadius: BorderRadius.circular(12),
                                  border: Border.all(
                                      color: ready
                                          ? Colors.green
                                          : Colors.grey.shade200),
                                ),
                                child: Row(
                                  children: [
                                    Text(recipe?.icon ?? '📦',
                                        style: const TextStyle(fontSize: 22)),
                                    const SizedBox(width: 10),
                                    Expanded(
                                      child: Text(recipe?.name ?? '加工',
                                          style: const TextStyle(
                                              fontWeight: FontWeight.w600,
                                              fontSize: 13)),
                                    ),
                                    if (ready)
                                      ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          backgroundColor: Colors.green,
                                          foregroundColor: Colors.white,
                                        ),
                                        onPressed: () async {
                                          final engine =
                                              await ref.read(gameEngineProvider.future);
                                          final player =
                                              playerAsync.valueOrNull;
                                          if (player == null) return;
                                          await engine.collectProduction(q, player);
                                          ref.invalidate(playerProvider);
                                          ref.invalidate(queuesProvider);
                                        },
                                        child: const Text('领取'),
                                      )
                                    else
                                      const Text('生产中...',
                                          style: TextStyle(
                                              fontSize: 11,
                                              color: Colors.black54)),
                                  ],
                                ),
                              ),
                            );
                          }).toList(),
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
