import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../navigation/app_router.dart';
import '../theme/app_theme.dart';
import '../widgets/common_widgets.dart';
import '../providers/game_providers.dart';

class WorldMapScreen extends ConsumerStatefulWidget {
  const WorldMapScreen({super.key});

  @override
  ConsumerState<WorldMapScreen> createState() => _WorldMapScreenState();
}

class _WorldMapScreenState extends ConsumerState<WorldMapScreen>
    with SingleTickerProviderStateMixin {
  // 缩放/平移变换
  final TransformationController _transformController =
      TransformationController();

  // 浮动奖励动画
  final List<Widget> _floatingRewards = [];

  // 季节动画
  late final AnimationController _seasonController;
  late final Animation<double> _seasonAnim;

  // 当前季节（演示用随机切换）
  final int _seasonIndex = 0;

  @override
  void initState() {
    super.initState();
    _seasonController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 2800),
    )..repeat(reverse: true);
    _seasonAnim = Tween<double>(begin: 0, end: 1).animate(
      CurvedAnimation(parent: _seasonController, curve: Curves.easeInOut),
    );
    // 初始缩放到合适
    Future.delayed(Duration.zero, () {
      if (!mounted) return;
      _transformController.value = Matrix4.identity()..scale(0.95);
    });
  }

  @override
  void dispose() {
    _transformController.dispose();
    _seasonController.dispose();
    super.dispose();
  }

  void _navigate(String route) {
    Navigator.of(context).pushNamed(route);
  }

  // ============= 模块按钮定义 ============
  List<_ModuleSpot> get _modules => [
        _ModuleSpot(
          label: '农田',
          emoji: '🌾',
          color: const Color(0xFF5DAF54),
          gradient: const [Color(0xFFA8E063), Color(0xFF56AB2F)],
          offset: const Offset(-160, -140),
          route: AppRoutes.farmland,
          size: 82,
        ),
        _ModuleSpot(
          label: '牧场',
          emoji: '🐄',
          color: const Color(0xFFD98C4F),
          gradient: const [Color(0xFFFFD194), Color(0xFFEF8F58)],
          offset: const Offset(130, -180),
          route: AppRoutes.livestock,
          size: 86,
        ),
        _ModuleSpot(
          label: '果园',
          emoji: '🍎',
          color: const Color(0xFFE57373),
          gradient: const [Color(0xFFFFB199), Color(0xFFFB5D7A)],
          offset: const Offset(-200, 60),
          route: AppRoutes.orchard,
          size: 78,
        ),
        _ModuleSpot(
          label: '工坊',
          emoji: '🏭',
          color: const Color(0xFF7B68EE),
          gradient: const [Color(0xFFBDB2FF), Color(0xFF7B68EE)],
          offset: const Offset(210, 40),
          route: AppRoutes.factory,
          size: 80,
        ),
        _ModuleSpot(
          label: '鱼塘',
          emoji: '🐟',
          color: const Color(0xFF4FC3F7),
          gradient: const [Color(0xFF81D4FA), Color(0xFF29B6F6)],
          offset: const Offset(-40, 220),
          route: AppRoutes.fishpond,
          size: 82,
        ),
        _ModuleSpot(
          label: '商店',
          emoji: '🏪',
          color: const Color(0xFFFFB74D),
          gradient: const [Color(0xFFFFE082), Color(0xFFFFA726)],
          offset: const Offset(180, 200),
          route: AppRoutes.shop,
          size: 74,
        ),
        _ModuleSpot(
          label: '仓库',
          emoji: '📦',
          color: const Color(0xFF8D6E63),
          gradient: const [Color(0xFFD7CCC8), Color(0xFF8D6E63)],
          offset: const Offset(-220, -20),
          route: AppRoutes.inventory,
          size: 72,
        ),
        _ModuleSpot(
          label: '图鉴',
          emoji: '📖',
          color: const Color(0xFFBA68C8),
          gradient: const [Color(0xFFE1BEE7), Color(0xFFBA68C8)],
          offset: const Offset(260, -40),
          route: AppRoutes.collection,
          size: 72,
        ),
        _ModuleSpot(
          label: '宠物',
          emoji: '🐾',
          color: const Color(0xFFFF8A80),
          gradient: const [Color(0xFFFFCDD2), Color(0xFFFF8A80)],
          offset: const Offset(0, -10),
          route: AppRoutes.pet,
          size: 92,
        ),
      ];

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final playerAsync = ref.watch(playerProvider);

    return FarmScaffold(
      title: '呆柳的农场',
      showBackButton: false,
      backgroundColor: const Color(0xFFE8F5E9),
      background: _buildAnimatedBackground(size),
      body: Stack(
        children: [
          // ============ 可交互的地图 ============
          InteractiveViewer(
            transformationController: _transformController,
            minScale: 0.6,
            maxScale: 1.8,
            boundaryMargin: const EdgeInsets.all(80),
            constrained: false,
            child: SizedBox(
              width: size.width,
              height: size.height,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  // 中心农场图标（最大）
                  Positioned(
                    top: size.height * 0.4,
                    left: size.width * 0.5 - 60,
                    child: _buildFarmAnchor(size),
                  ),
                  // 散布的模块按钮
                  for (final m in _modules)
                    Positioned(
                      top: size.height * 0.5 + m.offset.dy - m.size / 2,
                      left: size.width * 0.5 + m.offset.dx - m.size / 2,
                      child: _AnimatedModuleSpot(
                        module: m,
                        onTap: () => _navigate(m.route),
                      ),
                    ),
                  // 装饰：云
                  _buildCloud(size.width * 0.15, size.height * 0.12, 0.6),
                  _buildCloud(size.width * 0.72, size.height * 0.18, 0.9),
                  _buildCloud(size.width * 0.45, size.height * 0.08, 0.75),
                  // 装饰：草地与小花朵
                  _buildDecoration(size.width * 0.2, size.height * 0.75, '🌼'),
                  _buildDecoration(size.width * 0.65, size.height * 0.78, '🌸'),
                  _buildDecoration(size.width * 0.1, size.height * 0.55, '🌲'),
                  _buildDecoration(size.width * 0.85, size.height * 0.55, '🌳'),
                  _buildDecoration(size.width * 0.35, size.height * 0.82, '🦋'),
                  _buildDecoration(size.width * 0.78, size.height * 0.82, '🐝'),
                ],
              ),
            ),
          ),

          // ============ 顶部玩家信息栏 ============
          Positioned(
            top: 90,
            left: 14,
            right: 14,
            child: playerAsync.when(
              data: (player) => GlassCard(
                padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
                child: Row(
                  children: [
                    // 头像
                    Container(
                      width: 44,
                      height: 44,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        gradient: const LinearGradient(
                          colors: [Color(0xFFA8E063), Color(0xFF56AB2F)],
                        ),
                        border: Border.all(color: Colors.white, width: 2),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.green.withValues(alpha: 0.3),
                            blurRadius: 8,
                          )
                        ],
                      ),
                      child: const Center(
                        child: Text(
                          '👨‍🌾',
                          style: TextStyle(fontSize: 24),
                        ),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            '${player.name} · Lv.${player.level}',
                            style: const TextStyle(
                              fontWeight: FontWeight.w800,
                              fontSize: 14,
                              color: Color(0xFF2E5E1A),
                            ),
                          ),
                          const SizedBox(height: 4),
                          FarmProgressBar(
                            progress: (player.totalExp % 100) / 100.0,
                            color: AppTheme.gold,
                            height: 5,
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 12),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Text(
                          '${player.gold} 💰',
                          style: const TextStyle(
                            fontWeight: FontWeight.w800,
                            color: Color(0xFFB8860B),
                            fontSize: 14,
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          '收藏 ${player.collectionScore} 📚',
                          style: const TextStyle(
                            fontSize: 11,
                            color: Colors.black54,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              loading: () => const GlassCard(
                child: Text('加载中...', style: TextStyle(color: Colors.black45)),
              ),
              error: (_, __) => GlassCard(
                child: const Text('欢迎来到农场！',
                    style: TextStyle(color: Colors.black45)),
              ),
            ),
          ),

          // ============ 底部提示 ============
          Positioned(
            bottom: 18,
            left: 0,
            right: 0,
            child: Center(
              child: Container(
                padding:
                    const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                decoration: BoxDecoration(
                  color: Colors.white.withValues(alpha: 0.85),
                  borderRadius: BorderRadius.circular(24),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.green.withValues(alpha: 0.15),
                      blurRadius: 10,
                      offset: const Offset(0, 4),
                    )
                  ],
                ),
                child: const Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.touch_app, color: Colors.green, size: 14),
                    SizedBox(width: 6),
                    Text(
                      '可缩放/平移 · 点击图标进入',
                      style: TextStyle(
                        fontSize: 12,
                        color: Color(0xFF2E5E1A),
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),

          // 浮动奖励
          ..._floatingRewards,
        ],
      ),
    );
  }

  // ============ 背景动画（随季节变化的渐变） ============
  Widget _buildAnimatedBackground(Size size) {
    final seasons = [
      [const Color(0xFFC8E6C9), const Color(0xFFE8F5E9), const Color(0xFFFFF9C4)],
      [const Color(0xFFFFE0B2), const Color(0xFFFFF8E1), const Color(0xFFFFF3E0)],
      [const Color(0xFFFFCCBC), const Color(0xFFFBE9E7), const Color(0xFFD7CCC8)],
      [const Color(0xFFB3E5FC), const Color(0xFFE1F5FE), const Color(0xFFBBDEFB)],
    ];
    final palette = seasons[_seasonIndex % seasons.length];
    return AnimatedBuilder(
      animation: _seasonAnim,
      builder: (_, __) {
        return Container(
          width: size.width,
          height: size.height,
          decoration: BoxDecoration(
            gradient: LinearGradient(
              colors: palette,
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              stops: const [0.0, 0.55, 1.0],
            ),
          ),
          child: Stack(
            children: [
              // 底部的绿色草地
              Positioned(
                bottom: 0,
                left: 0,
                right: 0,
                child: Container(
                  height: size.height * 0.4,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      colors: [
                        const Color(0xFF81C784).withValues(alpha: 0.0),
                        const Color(0xFF66BB6A),
                      ],
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                    ),
                  ),
                ),
              ),
              // 太阳/月亮
              Positioned(
                top: 140,
                right: 30,
                child: AnimatedBuilder(
                  animation: _seasonAnim,
                  builder: (_, __) {
                    return Transform.translate(
                      offset: Offset(0, 8 * _seasonAnim.value),
                      child: Container(
                        width: 54,
                        height: 54,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          gradient: const RadialGradient(
                            colors: [Color(0xFFFFEB3B), Color(0xFFFFC107)],
                          ),
                          boxShadow: [
                            BoxShadow(
                              color: const Color(0xFFFFEB3B)
                                  .withValues(alpha: 0.5 + 0.3 * _seasonAnim.value),
                              blurRadius: 30,
                              spreadRadius: 8,
                            )
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  // ============ 中心农场图标 ============
  Widget _buildFarmAnchor(Size size) {
    return AnimatedBuilder(
      animation: _seasonAnim,
      builder: (_, __) {
        return Transform.scale(
          scale: 0.95 + 0.05 * _seasonAnim.value,
          child: Container(
            width: 120,
            height: 120,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              gradient: const RadialGradient(
                colors: [Color(0xFFFFF8E1), Color(0xFFA5D6A7)],
                center: Alignment.topLeft,
              ),
              boxShadow: [
                BoxShadow(
                  color: Colors.green.withValues(alpha: 0.3),
                  blurRadius: 24,
                  offset: const Offset(0, 8),
                )
              ],
              border: Border.all(color: Colors.white, width: 4),
            ),
            child: const Center(
              child: Text('🏡', style: TextStyle(fontSize: 54)),
            ),
          ),
        );
      },
    );
  }

  Widget _buildCloud(double x, double y, double scale) {
    return Positioned(
      top: y,
      left: x,
      child: AnimatedBuilder(
        animation: _seasonAnim,
        builder: (_, __) {
          return Transform.translate(
            offset: Offset(20 * _seasonAnim.value, 0),
            child: Opacity(
              opacity: 0.85,
              child: Transform.scale(
                scale: scale,
                child: const Text('☁️', style: TextStyle(fontSize: 42)),
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildDecoration(double x, double y, String emoji) {
    return Positioned(
      top: y,
      left: x,
      child: AnimatedBuilder(
        animation: _seasonAnim,
        builder: (_, __) {
          return Transform.scale(
            scale: 0.9 + 0.1 * _seasonAnim.value,
            child: Text(emoji, style: const TextStyle(fontSize: 28)),
          );
        },
      ),
    );
  }
}

// ============ 数据类 ============
class _ModuleSpot {
  final String label;
  final String emoji;
  final Color color;
  final List<Color> gradient;
  final Offset offset;
  final String route;
  final double size;
  _ModuleSpot({
    required this.label,
    required this.emoji,
    required this.color,
    required this.gradient,
    required this.offset,
    required this.route,
    required this.size,
  });
}

// ============ 带呼吸动画的模块按钮 ============
class _AnimatedModuleSpot extends StatefulWidget {
  final _ModuleSpot module;
  final VoidCallback onTap;
  const _AnimatedModuleSpot({required this.module, required this.onTap});

  @override
  State<_AnimatedModuleSpot> createState() => _AnimatedModuleSpotState();
}

class _AnimatedModuleSpotState extends State<_AnimatedModuleSpot>
    with SingleTickerProviderStateMixin {
  late final AnimationController _ctrl;
  late final Animation<double> _scale;
  late final Animation<double> _halo;

  @override
  void initState() {
    super.initState();
    _ctrl = AnimationController(
      vsync: this,
      duration: Duration(milliseconds: 2200 + (widget.module.label.length * 100)),
    )..repeat(reverse: true);
    _scale = Tween<double>(begin: 0.94, end: 1.08).animate(
      CurvedAnimation(parent: _ctrl, curve: Curves.easeInOut),
    );
    _halo = Tween<double>(begin: 0.5, end: 1.0).animate(
      CurvedAnimation(parent: _ctrl, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _ctrl,
      builder: (_, __) {
        return GestureDetector(
          onTap: widget.onTap,
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            Stack(
              alignment: Alignment.center,
              children: [
                // 外层光晕
                Container(
                  width: widget.module.size * 1.5,
                  height: widget.module.size * 1.5,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: widget.module.color
                        .withValues(alpha: 0.12 * _halo.value),
                  ),
                ),
                Container(
                  width: widget.module.size * 1.22,
                  height: widget.module.size * 1.22,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: widget.module.color
                        .withValues(alpha: 0.22 * _halo.value),
                  ),
                ),
                // 主体
                Transform.scale(
                  scale: _scale.value,
                  child: Container(
                    width: widget.module.size,
                    height: widget.module.size,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      gradient: LinearGradient(
                        colors: widget.module.gradient,
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: widget.module.color.withValues(alpha: 0.45),
                          blurRadius: 14,
                          offset: const Offset(0, 6),
                        ),
                      ],
                      border: Border.all(color: Colors.white, width: 3),
                    ),
                    child: Center(
                      child: Text(widget.module.emoji,
                          style: TextStyle(fontSize: widget.module.size * 0.45)),
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.08),
                    blurRadius: 4,
                    offset: const Offset(0, 2),
                  )
                ],
              ),
              child: Text(
                widget.module.label,
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w700,
                  color: widget.module.color,
                ),
              ),
            ),
          ]),
        );
      },
    );
  }
}
