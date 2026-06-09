import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';

class FishPondScreen extends ConsumerStatefulWidget {
  const FishPondScreen({super.key});

  @override
  ConsumerState<FishPondScreen> createState() => _FishPondScreenState();
}

class _FishPondScreenState extends ConsumerState<FishPondScreen>
    with TickerProviderStateMixin {
  late final AnimationController _wave;

  @override
  void initState() {
    super.initState();
    _wave = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 2000))
      ..repeat();
  }

  @override
  void dispose() {
    _wave.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final fishAsync = ref.watch(fishProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🐟 鱼塘',
      background: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [
              Colors.blue.shade100,
              Colors.blue.shade200,
              Colors.cyan.shade100
            ],
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
              // 购买鱼苗
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🐠 购买鱼苗',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF01579B),
                      ),
                    ),
                    const SizedBox(height: 10),
                    SizedBox(
                      height: 130,
                      child: ListView.separated(
                        scrollDirection: Axis.horizontal,
                        itemCount: FishConfigs.all.length,
                        separatorBuilder: (_, __) => const SizedBox(width: 10),
                        itemBuilder: (_, i) {
                          final config = FishConfigs.all[i];
                          return Container(
                            width: 110,
                            padding: const EdgeInsets.all(10),
                            decoration: BoxDecoration(
                              gradient: LinearGradient(
                                colors: [
                                  Colors.blue.shade50,
                                  Colors.blue.shade100
                                ],
                              ),
                              borderRadius: BorderRadius.circular(14),
                              border: Border.all(color: Colors.blue.shade200),
                            ),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                AnimatedBuilder(
                                  animation: _wave,
                                  builder: (_, __) => Transform.translate(
                                    offset: Offset(6 * _wave.value, 0),
                                    child: Text(config.icon,
                                        style: const TextStyle(fontSize: 32)),
                                  ),
                                ),
                                const SizedBox(height: 6),
                                Text(config.name,
                                    style: const TextStyle(
                                        fontSize: 12,
                                        fontWeight: FontWeight.w700,
                                        color: Color(0xFF01579B))),
                                const SizedBox(height: 6),
                                SizedBox(
                                  height: 26,
                                  child: ElevatedButton.icon(
                                    style: ElevatedButton.styleFrom(
                                      backgroundColor: Colors.lightBlue,
                                      foregroundColor: Colors.white,
                                      padding: const EdgeInsets.symmetric(horizontal: 6),
                                      shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                    ),
                                    onPressed: () async {
                                      final engine =
                                          await ref.read(gameEngineProvider.future);
                                      final player = playerAsync.valueOrNull;
                                      if (player == null) return;
                                      final pond = Pond(id: 'demo', x: 0, y: 0, unlocked: true);
                                      await engine.addFish(pond, config, player);
                                      ref.invalidate(playerProvider);
                                      ref.invalidate(fishProvider);
                                    },
                                    icon: const Icon(Icons.add, size: 12),
                                    label: Text('${config.sellPrice}💰',
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
              // 我的鱼
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🫧 我的鱼塘',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFF01579B),
                      ),
                    ),
                    const SizedBox(height: 10),
                    fishAsync.when(
                      data: (fishList) {
                        if (fishList.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(24.0),
                              child: Text('鱼塘空空如也，先放点鱼苗吧！',
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
                            childAspectRatio: 0.95,
                          ),
                          itemCount: fishList.length,
                          itemBuilder: (ctx, i) {
                            final fish = fishList[i];
                            final config = FishConfigs.all.firstWhere(
                                (e) => e.fishId == fish.configId,
                                orElse: () => FishConfigs.all.first);
                            final ready = fish.isReady;
                            return GestureDetector(
                              onTap: ready
                                  ? () async {
                                      final engine =
                                          await ref.read(gameEngineProvider.future);
                                      final player = playerAsync.valueOrNull;
                                      if (player == null) return;
                                      final r =
                                          await engine.collectFish(fish, player);
                                      if (r != null && context.mounted) {
                                        ScaffoldMessenger.of(context).showSnackBar(
                                          SnackBar(
                                            content: Text('捕获 +${r.$1}💰'),
                                            backgroundColor: Colors.green,
                                          ),
                                        );
                                        ref.invalidate(playerProvider);
                                        ref.invalidate(fishProvider);
                                      }
                                    }
                                  : null,
                              child: AnimatedBuilder(
                                animation: _wave,
                                builder: (_, __) => Container(
                                  decoration: BoxDecoration(
                                    gradient: LinearGradient(
                                      colors: ready
                                          ? [
                                              Colors.cyan.shade100,
                                              Colors.cyan.shade200
                                            ]
                                          : [
                                              Colors.blue.shade50,
                                              Colors.blue.shade100
                                            ],
                                    ),
                                    borderRadius: BorderRadius.circular(14),
                                    border: Border.all(
                                      color: ready
                                          ? Colors.cyan
                                          : Colors.blue.shade200,
                                      width: ready ? 2 : 1,
                                    ),
                                  ),
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Transform.translate(
                                        offset: Offset(
                                            8 * _wave.value * (i % 2 == 0 ? 1 : -1), 0),
                                        child: Text(config.icon,
                                            style: const TextStyle(fontSize: 32)),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(config.name,
                                          style: const TextStyle(
                                              fontSize: 11,
                                              fontWeight: FontWeight.w700,
                                              color: Color(0xFF01579B))),
                                      const SizedBox(height: 3),
                                      if (ready)
                                        Container(
                                          padding: const EdgeInsets.symmetric(
                                              horizontal: 6, vertical: 2),
                                          decoration: BoxDecoration(
                                            color: Colors.cyan.shade100,
                                            borderRadius: BorderRadius.circular(8),
                                          ),
                                          child: const Text(
                                            '✨ 可捕获',
                                            style: TextStyle(
                                                fontSize: 9,
                                                color: Colors.cyan,
                                                fontWeight: FontWeight.w700),
                                          ),
                                        )
                                      else
                                        const Text('成长中',
                                            style: TextStyle(
                                                fontSize: 9,
                                                color: Colors.black54)),
                                    ],
                                  ),
                                ),
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
