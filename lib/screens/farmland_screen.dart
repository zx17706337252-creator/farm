import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../models/game_models.dart';
import '../providers/game_providers.dart';
import '../theme/app_theme.dart';
import '../widgets/common_widgets.dart';

class FarmlandScreen extends ConsumerStatefulWidget {
  const FarmlandScreen({super.key});

  @override
  ConsumerState<FarmlandScreen> createState() => _FarmlandScreenState();
}

class _FarmlandScreenState extends ConsumerState<FarmlandScreen>
    with TickerProviderStateMixin {
  late final AnimationController _growCtrl;
  int? _selectedCropConfigId;
  final List<FloatingReward> _floatingRewards = [];

  @override
  void initState() {
    super.initState();
    _growCtrl = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 800),
    )..repeat(reverse: true);
  }

  @override
  void dispose() {
    _growCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final playerAsync = ref.watch(playerProvider);
    final cropsAsync = ref.watch(cropsProvider);
    final landsAsync = ref.watch(landsProvider);

    return FarmScaffold(
      title: '🌾 农田',
      background: _buildBackground(),
      body: Stack(
        children: [
          SafeArea(
            child: SingleChildScrollView(
              padding: const EdgeInsets.fromLTRB(16, 80, 16, 24),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // 种子选择
                  GlassCard(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          '选择种子',
                          style: TextStyle(
                            fontWeight: FontWeight.w800,
                            fontSize: 15,
                            color: Color(0xFF2E5E1A),
                          ),
                        ),
                        const SizedBox(height: 10),
                        SizedBox(
                          height: 90,
                          child: ListView.separated(
                            scrollDirection: Axis.horizontal,
                            itemCount: CropConfigs.all.take(8).length,
                            separatorBuilder: (_, __) =>
                                const SizedBox(width: 10),
                            itemBuilder: (_, i) {
                              final config = CropConfigs.all[i];
                              final selected = _selectedCropConfigId == config.cropId;
                              return GestureDetector(
                                onTap: () {
                                  setState(() {
                                    _selectedCropConfigId = config.cropId;
                                  });
                                },
                                child: AnimatedContainer(
                                  duration: const Duration(milliseconds: 200),
                                  width: 74,
                                  decoration: BoxDecoration(
                                    color: selected
                                        ? const Color(0xFF5DAF54)
                                            .withValues(alpha: 0.15)
                                        : Colors.white,
                                    borderRadius: BorderRadius.circular(14),
                                    border: Border.all(
                                      color: selected
                                          ? const Color(0xFF5DAF54)
                                          : Colors.grey.shade200,
                                      width: selected ? 2.5 : 1.5,
                                    ),
                                    boxShadow: selected
                                        ? [
                                            BoxShadow(
                                              color: Colors.green
                                                  .withValues(alpha: 0.3),
                                              blurRadius: 10,
                                            )
                                          ]
                                        : null,
                                  ),
                                  padding: const EdgeInsets.all(8),
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Text(config.icon,
                                          style: const TextStyle(fontSize: 24)),
                                      const SizedBox(height: 4),
                                      Text(
                                        config.name,
                                        style: const TextStyle(
                                          fontSize: 11,
                                          fontWeight: FontWeight.w600,
                                          color: Color(0xFF2E5E1A),
                                        ),
                                      ),
                                      Text(
                                        '${config.sellPrice}💰',
                                        style: const TextStyle(
                                          fontSize: 10,
                                          color: Colors.grey,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              );
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 16),
                  // 田地网格
                  GlassCard(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            const Icon(Icons.grass, color: Color(0xFF5DAF54)),
                            const SizedBox(width: 6),
                            const Text(
                              '我的田地',
                              style: TextStyle(
                                fontWeight: FontWeight.w800,
                                fontSize: 15,
                                color: Color(0xFF2E5E1A),
                              ),
                            ),
                            const Spacer(),
                            ElevatedButton.icon(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: const Color(0xFF5DAF54),
                                foregroundColor: Colors.white,
                                elevation: 3,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(12),
                                ),
                              ),
                              icon: const Icon(Icons.speed, size: 16),
                              label: const Text('一键收获',
                                  style: TextStyle(fontSize: 12)),
                              onPressed: () async {
                                final engine =
                                    await ref.read(gameEngineProvider.future);
                                final crops = cropsAsync.valueOrNull ?? [];
                                final player = playerAsync.valueOrNull;
                                if (player == null) return;
                                for (final c in crops) {
                                  if (!c.isReady) continue;
                                  final r = await engine.harvestCrop(c, player);
                                  if (r != null && context.mounted) {
                                    _showFloatingReward('+${r.$1}💰');
                                  }
                                }
                                ref.invalidate(playerProvider);
                                ref.invalidate(cropsProvider);
                              },
                            ),
                          ],
                        ),
                        const SizedBox(height: 12),
                        landsAsync.when(
                          data: (lands) {
                            final crops = cropsAsync.valueOrNull ?? [];
                            return GridView.builder(
                              shrinkWrap: true,
                              physics: const NeverScrollableScrollPhysics(),
                              gridDelegate:
                                  const SliverGridDelegateWithFixedCrossAxisCount(
                                crossAxisCount: 5,
                                mainAxisSpacing: 8,
                                crossAxisSpacing: 8,
                              ),
                              itemCount: (lands.length < 20 ? 20 : lands.length),
                              itemBuilder: (ctx, i) {
                                if (i < lands.length) {
                                  final land = lands[i];
                                  final cropOnLand = crops.firstWhere(
                                      (c) => c.landId == land.id,
                                      orElse: () => Crop(
                                            id: '',
                                            cropId: 0,
                                            landId: '',
                                            plantTime: 0,
                                            finishTime: 0,
                                          ));
                                  return _LandTile(
                                    land: land,
                                    crop: cropOnLand.id.isNotEmpty
                                        ? cropOnLand
                                        : null,
                                    growCtrl: _growCtrl,
                                    onTap: () async {
                                      final engine =
                                          await ref.read(gameEngineProvider.future);
                                      final player = playerAsync.valueOrNull;
                                      if (player == null) return;
                                      if (_selectedCropConfigId == null) {
                                        _toast('请先选择种子');
                                        return;
                                      }
                                      final cfg = CropConfigs.all.firstWhere(
                                          (e) =>
                                              e.cropId == _selectedCropConfigId);
                                      if (player.gold < cfg.sellPrice * 2) {
                                        _toast('金币不足');
                                        return;
                                      }
                                      await engine.plantCrop(land, cfg, player);
                                      ref.invalidate(playerProvider);
                                      ref.invalidate(cropsProvider);
                                      _showFloatingReward('-${cfg.sellPrice * 2}💰');
                                    },
                                  );
                                }
                                return Container(
                                  decoration: BoxDecoration(
                                    color: Colors.grey.shade200,
                                    borderRadius: BorderRadius.circular(10),
                                  ),
                                  child:
                                      const Icon(Icons.lock, color: Colors.grey),
                                );
                              },
                            );
                          },
                          loading: () => const Center(
                              child: Padding(
                            padding: EdgeInsets.all(32.0),
                            child: CircularProgressIndicator(),
                          )),
                          error: (e, _) => Text('加载失败: $e'),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 16),
                  GlassCard(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: const [
                        Text('💡 小提示',
                            style: TextStyle(
                                fontWeight: FontWeight.w800,
                                fontSize: 14,
                                color: Color(0xFF2E5E1A))),
                        SizedBox(height: 8),
                        Text(
                          '1. 先选择上方的种子\n2. 点击田地种植\n3. 等待作物成熟后点击收获\n4. 高品质作物可以在图鉴解锁收藏',
                          style: TextStyle(fontSize: 12, color: Colors.black54, height: 1.6),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
          ..._floatingRewards,
        ],
      ),
    );
  }

  Widget _buildBackground() {
    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          colors: [Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFFFF9C4)],
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          stops: [0.0, 0.5, 1.0],
        ),
      ),
    );
  }

  void _showFloatingReward(String text) {
    if (!mounted) return;
    final renderBox = context.findRenderObject() as RenderBox?;
    final offset = renderBox?.localToGlobal(Offset.zero) ?? Offset.zero;
    setState(() {
      _floatingRewards.add(
        FloatingReward(
          text: text,
          color: const Color(0xFFD4A017),
          start: Offset(MediaQuery.of(context).size.width / 2 - 40,
              offset.dy + MediaQuery.of(context).size.height / 2),
          onComplete: () {
            if (mounted) {
              setState(() {
                _floatingRewards.removeAt(0);
              });
            }
          },
        ),
      );
    });
  }

  void _toast(String text) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(text),
        duration: const Duration(seconds: 1),
        backgroundColor: Colors.brown,
      ),
    );
  }
}

// ============ 田地格 ============
class _LandTile extends StatelessWidget {
  final Land land;
  final Crop? crop;
  final AnimationController growCtrl;
  final VoidCallback onTap;

  const _LandTile({
    required this.land,
    required this.crop,
    required this.growCtrl,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final config = crop != null
        ? CropConfigs.all.firstWhere((c) => c.cropId == crop!.cropId,
            orElse: () => CropConfigs.all.first)
        : null;
    final isReady = crop?.isReady ?? false;
    return GestureDetector(
      onTap: onTap,
      child: AnimatedBuilder(
        animation: growCtrl,
        builder: (_, __) {
          final growScale = crop == null
              ? 1.0
              : isReady
                  ? 1.0 + 0.08 * growCtrl.value
                  : 0.6 + 0.15 * growCtrl.value;
          return Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: isReady
                    ? [const Color(0xFFF9A825), const Color(0xFFFFC107)]
                    : [const Color(0xFF8D6E63), const Color(0xFFA1887F)],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.circular(10),
              border: Border.all(
                  color: isReady
                      ? const Color(0xFFFFB300)
                      : Colors.brown.shade200,
                  width: isReady ? 2 : 1),
              boxShadow: isReady
                  ? [
                      BoxShadow(
                        color: Colors.amber.withValues(alpha: 0.5),
                        blurRadius: 8,
                        spreadRadius: 1,
                      )
                    ]
                  : null,
            ),
            child: Center(
              child: crop == null
                  ? Icon(Icons.add, color: Colors.brown.shade200, size: 22)
                  : Transform.scale(
                      scale: growScale,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(config?.icon ?? '🌱',
                              style: const TextStyle(fontSize: 22)),
                          if (!isReady)
                            const Padding(
                              padding: EdgeInsets.only(top: 2),
                              child: Text('🌱',
                                  style: TextStyle(fontSize: 8, color: Colors.white70)),
                            ),
                          if (isReady)
                            const Padding(
                              padding: EdgeInsets.only(top: 2),
                              child: Text('✨', style: TextStyle(fontSize: 8)),
                            ),
                        ],
                      ),
                    ),
            ),
          );
        },
      ),
    );
  }
}
