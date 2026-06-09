import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../configs/game_configs.dart';
import '../providers/game_providers.dart';
import '../widgets/common_widgets.dart';

class CollectionScreen extends ConsumerStatefulWidget {
  const CollectionScreen({super.key});

  @override
  ConsumerState<CollectionScreen> createState() => _CollectionScreenState();
}

class _CollectionScreenState extends ConsumerState<CollectionScreen> {
  int _tabIndex = 0;
  static const _tabs = [
    ('作物', '🌱'),
    ('动物', '🐄'),
    ('果树', '🌳'),
    ('鱼类', '🐠'),
    ('配方', '⚗️'),
    ('宠物', '🐾'),
  ];

  @override
  Widget build(BuildContext context) {
    final collectionsAsync = ref.watch(collectionsProvider);

    return FarmScaffold(
      title: '📖 图鉴',
      background: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [
              Colors.purple.shade50,
              Colors.purple.shade100,
              Colors.pink.shade50,
            ],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
      ),
      body: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(16),
              child: GlassCard(
                padding: const EdgeInsets.symmetric(vertical: 4),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: _tabs.asMap().entries.map((e) {
                    final selected = _tabIndex == e.key;
                    return Expanded(
                      child: GestureDetector(
                        onTap: () => setState(() => _tabIndex = e.key),
                        child: AnimatedContainer(
                          duration: const Duration(milliseconds: 220),
                          padding: const EdgeInsets.symmetric(vertical: 10),
                          decoration: BoxDecoration(
                            color: selected
                                ? Colors.purple.shade400
                                : Colors.transparent,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: Column(
                            children: [
                              Text(e.value.$2,
                                  style: const TextStyle(fontSize: 18)),
                              const SizedBox(height: 2),
                              Text(
                                e.value.$1,
                                style: TextStyle(
                                  fontSize: 10,
                                  fontWeight: FontWeight.w700,
                                  color: selected
                                      ? Colors.white
                                      : Colors.purple.shade900,
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
                child: _buildContent(collectionsAsync),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildContent(AsyncValue<List<CollectionRecord>> collectionsAsync) {
    final allCollections = collectionsAsync.valueOrNull ?? [];
    switch (_tabIndex) {
      case 0:
        return _CollectionGrid(
          title: '🌱 作物图鉴',
          color: Colors.green,
          items: CropConfigs.all
              .map((c) => _CollectionEntry(
                    icon: c.icon,
                    name: c.name,
                    type: 'CROP',
                    configId: c.cropId,
                    collections: allCollections,
                    color: Colors.green,
                  ))
              .toList(),
        );
      case 1:
        return _CollectionGrid(
          title: '🐄 动物图鉴',
          color: Colors.orange,
          items: AnimalConfigs.all
              .map((a) => _CollectionEntry(
                    icon: a.icon,
                    name: a.name,
                    type: 'ANIMAL',
                    configId: a.animalId,
                    collections: allCollections,
                    color: Colors.orange,
                  ))
              .toList(),
        );
      case 2:
        return _CollectionGrid(
          title: '🌳 果树图鉴',
          color: Colors.red,
          items: TreeConfigs.all
              .map((t) => _CollectionEntry(
                    icon: t.icon,
                    name: t.name,
                    type: 'TREE',
                    configId: t.treeId,
                    collections: allCollections,
                    color: Colors.red,
                  ))
              .toList(),
        );
      case 3:
        return _CollectionGrid(
          title: '🐠 鱼类图鉴',
          color: Colors.blue,
          items: FishConfigs.all
              .map((f) => _CollectionEntry(
                    icon: f.icon,
                    name: f.name,
                    type: 'FISH',
                    configId: f.fishId,
                    collections: allCollections,
                    color: Colors.blue,
                  ))
              .toList(),
        );
      case 4:
        return _CollectionGrid(
          title: '⚗️ 配方图鉴',
          color: Colors.deepPurple,
          items: FactoryConfigs.recipes
              .map((r) => _CollectionEntry(
                    icon: r.icon,
                    name: r.name,
                    type: 'PROCESSED',
                    configId: r.recipeId,
                    collections: allCollections,
                    color: Colors.deepPurple,
                  ))
              .toList(),
        );
      case 5:
        return _CollectionGrid(
          title: '🐾 宠物图鉴',
          color: Colors.pink,
          items: PetConfigs.all
              .map((p) => _CollectionEntry(
                    icon: p.icon,
                    name: p.name,
                    type: 'PET',
                    configId: p.petId,
                    collections: allCollections,
                    color: Colors.pink,
                  ))
              .toList(),
        );
    }
    return const SizedBox.shrink();
  }
}

class _CollectionGrid extends StatelessWidget {
  final String title;
  final Color color;
  final List<_CollectionEntry> items;
  const _CollectionGrid(
      {required this.title, required this.color, required this.items});

  @override
  Widget build(BuildContext context) {
    final collected = items.where((e) => e.isCollected).length;
    return GlassCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(title,
                  style: TextStyle(
                      fontWeight: FontWeight.w800, fontSize: 15, color: color)),
              Text('$collected/${items.length}',
                  style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w700,
                      color: color)),
            ],
          ),
          const SizedBox(height: 10),
          FarmProgressBar(
            progress: items.isEmpty ? 0 : collected / items.length,
            color: color,
          ),
          const SizedBox(height: 12),
          GridView.builder(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 4,
              mainAxisSpacing: 8,
              crossAxisSpacing: 8,
              childAspectRatio: 0.8,
            ),
            itemCount: items.length,
            itemBuilder: (_, i) => items[i],
          ),
        ],
      ),
    );
  }
}

class _CollectionEntry extends StatelessWidget {
  final String icon;
  final String name;
  final String type;
  final int configId;
  final List<CollectionRecord> collections;
  final Color color;

  const _CollectionEntry({
    required this.icon,
    required this.name,
    required this.type,
    required this.configId,
    required this.collections,
    required this.color,
  });

  bool get isCollected => collections.any(
      (c) => c.collectionType == type && c.configId == configId);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(4),
      decoration: BoxDecoration(
        color: isCollected ? color.withValues(alpha: 0.12) : Colors.grey.shade100,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
            color: isCollected ? color.withValues(alpha: 0.5) : Colors.grey.shade300),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            isCollected ? icon : '❓',
            style: const TextStyle(fontSize: 24),
          ),
          const SizedBox(height: 3),
          Text(
            isCollected ? name : '???',
            style: TextStyle(
                fontSize: 10,
                color: isCollected ? color : Colors.grey,
                fontWeight: FontWeight.w700),
            overflow: TextOverflow.ellipsis,
          ),
        ],
      ),
    );
  }
}
