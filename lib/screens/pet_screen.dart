import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';
import '../models/game_models.dart';

class PetScreen extends ConsumerStatefulWidget {
  const PetScreen({super.key});

  @override
  ConsumerState<PetScreen> createState() => _PetScreenState();
}

class _PetScreenState extends ConsumerState<PetScreen>
    with TickerProviderStateMixin {
  late final AnimationController _heart;

  @override
  void initState() {
    super.initState();
    _heart = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 1000))
      ..repeat(reverse: true);
  }

  @override
  void dispose() {
    _heart.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final petsAsync = ref.watch(petsProvider);
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '🐾 宠物',
      background: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFF8BBD0)],
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
                      '🏠 我的宠物',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFFC62828),
                      ),
                    ),
                    const SizedBox(height: 10),
                    petsAsync.when(
                      data: (pets) {
                        if (pets.isEmpty) {
                          return const Center(
                            child: Padding(
                              padding: EdgeInsets.all(24.0),
                              child: Text('还没有宠物，快去商店买一只吧！',
                                  style: TextStyle(color: Colors.black54)),
                            ),
                          );
                        }
                        return Column(
                          children: pets.map((pet) {
                            final config = PetConfigs.all.firstWhere(
                                (p) => p.petId == pet.petId,
                                orElse: () => PetConfigs.all.first);
                            return Padding(
                              padding: const EdgeInsets.only(bottom: 10),
                              child: Container(
                                padding: const EdgeInsets.all(12),
                                decoration: BoxDecoration(
                                  gradient: LinearGradient(
                                    colors: [
                                      Colors.pink.shade50,
                                      Colors.pink.shade100
                                    ],
                                    begin: Alignment.topLeft,
                                    end: Alignment.bottomRight,
                                  ),
                                  borderRadius: BorderRadius.circular(16),
                                  border: Border.all(
                                      color: Colors.pink.shade200, width: 2),
                                ),
                                child: Row(
                                  children: [
                                    AnimatedBuilder(
                                      animation: _heart,
                                      builder: (_, __) => Transform.scale(
                                        scale: 0.95 + 0.1 * _heart.value,
                                        child: Text(config.icon,
                                            style:
                                                const TextStyle(fontSize: 42)),
                                      ),
                                    ),
                                    const SizedBox(width: 12),
                                    Expanded(
                                      child: Column(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        children: [
                                          Text(config.name,
                                              style: const TextStyle(
                                                  fontSize: 14,
                                                  fontWeight: FontWeight.w800,
                                                  color: Color(0xFFC62828))),
                                          const SizedBox(height: 4),
                                          Text(config.description,
                                              style: const TextStyle(
                                                  fontSize: 10,
                                                  color: Colors.black54)),
                                          const SizedBox(height: 6),
                                          Row(
                                            children: [
                                              _stat('Lv.${pet.level}',
                                                  Icons.star, Colors.amber),
                                              const SizedBox(width: 6),
                                              _stat('亲密度 ${pet.friendship}',
                                                  Icons.favorite, Colors.pink),
                                              const SizedBox(width: 6),
                                              _stat(
                                                  pet.isActive ? '工作中' : '休息',
                                                  Icons.work,
                                                  pet.isActive
                                                      ? Colors.green
                                                      : Colors.grey),
                                            ],
                                          ),
                                        ],
                                      ),
                                    ),
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
              const SizedBox(height: 16),
              GlassCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '🎁 宠物商店',
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: Color(0xFFC62828),
                      ),
                    ),
                    const SizedBox(height: 10),
                    ...PetConfigs.all.take(8).map((config) {
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.pink.shade50,
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: Colors.pink.shade100),
                          ),
                          child: Row(
                            children: [
                              AnimatedBuilder(
                                animation: _heart,
                                builder: (_, __) => Transform.scale(
                                  scale: 0.95 + 0.1 * _heart.value,
                                  child: Text(config.icon,
                                      style: const TextStyle(fontSize: 28)),
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
                                            color: Color(0xFFC62828))),
                                    const SizedBox(height: 2),
                                    Text(config.description,
                                        style: const TextStyle(
                                            fontSize: 10, color: Colors.black54),
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis),
                                  ],
                                ),
                              ),
                              ElevatedButton.icon(
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.pink.shade400,
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
                                  final ok =
                                      await engine.buyPet(config, player);
                                  if (ok) {
                                    ref.invalidate(playerProvider);
                                    ref.invalidate(petsProvider);
                                  }
                                },
                                icon: const Icon(Icons.add, size: 14),
                                label: Text('${config.purchasePrice}💰',
                                    style: const TextStyle(fontSize: 12)),
                              ),
                            ],
                          ),
                        ),
                      );
                    }),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _stat(String text, IconData icon, Color color) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
      decoration: BoxDecoration(
        color: color.withOpacity(0.15),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(mainAxisSize: MainAxisSize.min, children: [
        Icon(icon, color: color, size: 10),
        const SizedBox(width: 3),
        Text(text,
            style: TextStyle(
                fontSize: 9, color: color, fontWeight: FontWeight.w700)),
      ]),
    );
  }
}
