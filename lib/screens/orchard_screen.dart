import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';

class OrchardScreen extends ConsumerStatefulWidget {
  const OrchardScreen({super.key});

  @override
  ConsumerState<OrchardScreen> createState() => _OrchardScreenState();
}

class _OrchardScreenState extends ConsumerState<OrchardScreen>
    with TickerProviderStateMixin {
  late final AnimationController _sway;

  @override
  void initState() {
    super.initState();
    _sway = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 3000))
      ..repeat(reverse: true);
  }

  @override
  void dispose() {
    _sway.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final treesAsync = ref.watch(treesProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🍎 果园',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFFFF3E0)],
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
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🌳 购买果树',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFFBF360C),
                      ),
                    ),
                    const SizedBox(height: 10),
                    ...TreeConfigs.all.map((config) {
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.red.shade50,
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: Colors.red.shade100),
                          ),
                          child: Row(
                            children: [
                              AnimatedBuilder(
                                animation: _sway,
                                builder: (_, __) => Transform.rotate(
                                  angle: 0.08 * _sway.value,
                                  child: Text(config.icon,
                                      style: const TextStyle(fontSize: 32)),
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(config.name,
                                        style: const TextStyle(
                                            fontSize: 13,
                                            fontWeight: FontWeight.w700,
                                            color: Color(0xFFBF360C))),
                                    const SizedBox(height: 3),
                                    Text(
                                      '售价: ${config.sellPrice}💰 · 经验: ${config.exp}',
                                      style: const TextStyle(
                                          fontSize: 11, color: Colors.black54),
                                    ),
                                  ],
                                ),
                              ),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.red.shade400,
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
                                  final ok = await engine.plantTree(
                                      0, 0, config, player);
                                  if (ok) {
                                    ref.invalidate(playerProvider);
                                    ref.invalidate(treesProvider);
                                  }
                                },
                                child: const Text('种植',
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
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🌲 我的果园',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFFBF360C),
                      ),
                    ),
                    const SizedBox(height: 12),
                    treesAsync.when(
                      data: (trees) {
                        if (trees.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(24.0),
                              child: Text('还没有果树，先种一棵吧！',
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
                          ),
                          itemCount: trees.length,
                          itemBuilder: (ctx, i) {
                            final tree = trees[i];
                            final config = TreeConfigs.all.firstWhere(
                                (e) => e.treeId == tree.configId,
                                orElse: () => TreeConfigs.all.first);
                            final ready = tree.isReady;
                            return GestureDetector(
                              onTap: () async {
                                final engine =
                                    await ref.read(gameEngineProvider.future);
                                final player = playerAsync.valueOrNull;
                                if (player == null) return;
                                final r =
                                    await engine.harvestTree(tree, player);
                                if (r != null && context.mounted) {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    SnackBar(
                                      content: Text('收获 +${r.$1}💰'),
                                      backgroundColor: Colors.green,
                                    ),
                                  );
                                  ref.invalidate(playerProvider);
                                  ref.invalidate(treesProvider);
                                }
                              },
                              child: AnimatedBuilder(
                                animation: _sway,
                                builder: (_, __) {
                                  return Container(
                                    decoration: BoxDecoration(
                                      gradient: LinearGradient(
                                        colors: ready
                                            ? [
                                                Colors.amber.shade100,
                                                Colors.amber.shade200
                                              ]
                                            : [
                                                Colors.green.shade50,
                                                Colors.green.shade100
                                              ],
                                        begin: Alignment.topLeft,
                                        end: Alignment.bottomRight,
                                      ),
                                      borderRadius: BorderRadius.circular(14),
                                      border: Border.all(
                                        color: ready
                                            ? Colors.amber
                                            : Colors.green.shade200,
                                        width: ready ? 2 : 1,
                                      ),
                                    ),
                                    child: Column(
                                      mainAxisAlignment: MainAxisAlignment.center,
                                      children: [
                                        Transform.rotate(
                                          angle: 0.1 * _sway.value,
                                          child: Text(config.icon,
                                              style: const TextStyle(fontSize: 36)),
                                        ),
                                        const SizedBox(height: 4),
                                        Text(config.name,
                                            style: const TextStyle(
                                              fontSize: 11,
                                              fontWeight: FontWeight.w700,
                                              color: Color(0xFFBF360C),
                                            )),
                                        const SizedBox(height: 2),
                                        Container(
                                          padding: const EdgeInsets.symmetric(
                                              horizontal: 6, vertical: 2),
                                          decoration: BoxDecoration(
                                            color: (ready
                                                    ? Colors.amber
                                                    : Colors.green)
                                                .withValues(alpha: 0.2),
                                            borderRadius:
                                                BorderRadius.circular(8),
                                          ),
                                          child: Text(
                                            ready ? '✨ 可收获' : '🌱 成长中',
                                            style: TextStyle(
                                              fontSize: 9,
                                              color: ready
                                                  ? Colors.amber.shade800
                                                  : Colors.green,
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
