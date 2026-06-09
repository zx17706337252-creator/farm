import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../theme/app_theme.dart';

// ========== 通用页面骨架 ==========
class FarmScaffold extends StatelessWidget {
  final Widget body;
  final String title;
  final List<Widget>? actions;
  final Widget? floatingActionButton;
  final Color? backgroundColor;
  final Widget? bottomBar;
  final bool showBackButton;
  final Widget? background;

  const FarmScaffold({
    super.key,
    required this.body,
    required this.title,
    this.actions,
    this.floatingActionButton,
    this.backgroundColor,
    this.bottomBar,
    this.showBackButton = true,
    this.background,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: backgroundColor ?? AppTheme.background,
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.transparent,
        title: Text(
          title,
          style: const TextStyle(
            color: Color(0xFF2E5E1A),
            fontWeight: FontWeight.w700,
            letterSpacing: 1.2,
            shadows: [
              Shadow(color: Colors.white54, blurRadius: 4),
            ],
          ),
        ),
        centerTitle: true,
        leading: showBackButton
            ? Padding(
                padding: const EdgeInsets.all(8),
                child: IconButton.filled(
                  style: IconButton.styleFrom(
                    backgroundColor: Colors.white.withValues(alpha: 0.85),
                    foregroundColor: AppTheme.primary,
                    elevation: 4,
                    shadowColor: Colors.black12,
                  ),
                  icon: const Icon(Icons.arrow_back_ios_new, size: 18),
                  onPressed: () => Navigator.of(context).maybePop(),
                ),
              )
            : null,
        actions: actions,
      ),
      body: Stack(
        children: [
          if (background != null) background!,
          SafeArea(top: false, child: body),
        ],
      ),
      floatingActionButton: floatingActionButton,
      bottomNavigationBar: bottomBar,
    );
  }
}

// ========== 圆角卡片 ==========
class GlassCard extends StatelessWidget {
  final Widget child;
  final double? width;
  final double? height;
  final EdgeInsetsGeometry? padding;
  final EdgeInsetsGeometry? margin;
  final double? radius;
  final Color? color;
  final List<BoxShadow>? shadow;
  final VoidCallback? onTap;

  const GlassCard({
    super.key,
    required this.child,
    this.width,
    this.height,
    this.padding,
    this.margin,
    this.radius,
    this.color,
    this.shadow,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    Widget container = AnimatedContainer(
      duration: const Duration(milliseconds: 200),
      width: width,
      height: height,
      margin: margin,
      padding: padding ?? const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: color ?? Colors.white.withValues(alpha: 0.92),
        borderRadius: BorderRadius.circular(radius ?? 20),
        border: Border.all(color: Colors.white70, width: 1.5),
        boxShadow: shadow ??
            [
              BoxShadow(
                color: Colors.greenAccent.withValues(alpha: 0.12),
                blurRadius: 16,
                offset: const Offset(0, 6),
              ),
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.06),
                blurRadius: 6,
                offset: const Offset(0, 2),
              ),
            ],
      ),
      child: child,
    );

    return onTap != null
        ? Material(
            color: Colors.transparent,
            borderRadius: BorderRadius.circular(radius ?? 20),
            child: InkWell(
              borderRadius: BorderRadius.circular(radius ?? 20),
              onTap: onTap,
              child: container,
            ),
          )
        : container;
  }
}

// ========== 状态条（金币/等级/经验）==========
class StatusChip extends StatelessWidget {
  final String icon;
  final String label;
  final Color? color;
  final Color? bgColor;

  const StatusChip({
    super.key,
    required this.icon,
    required this.label,
    this.color,
    this.bgColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: bgColor ?? Colors.white.withValues(alpha: 0.9),
        borderRadius: BorderRadius.circular(24),
        border: Border.all(
          color: (color ?? AppTheme.gold).withValues(alpha: 0.4),
        ),
        boxShadow: [
          BoxShadow(
            color: (color ?? AppTheme.gold).withValues(alpha: 0.15),
            blurRadius: 8,
            offset: const Offset(0, 2),
          )
        ],
      ),
      child: Row(mainAxisSize: MainAxisSize.min, children: [
        Text(icon, style: const TextStyle(fontSize: 14)),
        const SizedBox(width: 6),
        Text(
          label,
          style: TextStyle(
            fontWeight: FontWeight.w700,
            color: color ?? AppTheme.brown,
            fontSize: 13,
          ),
        ),
      ]),
    );
  }
}

// ========== 彩色按钮 ==========
class GradientButton extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;
  final IconData? icon;
  final Color startColor;
  final Color endColor;
  final double? width;
  final double? height;

  const GradientButton({
    super.key,
    required this.text,
    this.onPressed,
    this.icon,
    this.startColor = AppTheme.primary,
    this.endColor = const Color(0xFF7BC96F),
    this.width,
    this.height,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: width,
      height: height ?? 48,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 180),
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [startColor, endColor],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
          borderRadius: BorderRadius.circular(14),
          boxShadow: [
            BoxShadow(
              color: startColor.withValues(alpha: 0.4),
              blurRadius: 12,
              offset: const Offset(0, 4),
            )
          ],
        ),
        child: Material(
          color: Colors.transparent,
          child: InkWell(
            borderRadius: BorderRadius.circular(14),
            onTap: onPressed,
            child: Center(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  if (icon != null) Icon(icon, color: Colors.white, size: 18),
                  if (icon != null) const SizedBox(width: 6),
                  Text(
                    text,
                    style: const TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.w700,
                      letterSpacing: 1.1,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// ========== 浮动数字动画（收获时弹出 +N 金币）==========
class FloatingReward extends StatefulWidget {
  final String text;
  final Color color;
  final Offset start;
  final VoidCallback? onComplete;

  const FloatingReward({
    super.key,
    required this.text,
    required this.color,
    required this.start,
    this.onComplete,
  });

  @override
  State<FloatingReward> createState() => _FloatingRewardState();
}

class _FloatingRewardState extends State<FloatingReward>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  late final Animation<double> _opacity;
  late final Animation<double> _translate;
  late final Animation<double> _scale;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1200),
    );
    _opacity = Tween<double>(begin: 1.0, end: 0.0).animate(
      CurvedAnimation(parent: _controller, curve: const Interval(0.5, 1.0)),
    );
    _translate = Tween<double>(begin: 0, end: -60).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeOutCubic),
    );
    _scale = Tween<double>(begin: 0.5, end: 1.2).animate(
      CurvedAnimation(parent: _controller, curve: const Interval(0, 0.3)),
    );
    _controller.forward().whenComplete(() {
      if (mounted) widget.onComplete?.call();
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Positioned(
      left: widget.start.dx,
      top: widget.start.dy,
      child: AnimatedBuilder(
        animation: _controller,
        builder: (_, __) => Transform.translate(
          offset: Offset(0, _translate.value),
          child: Transform.scale(
            scale: _scale.value,
            child: Opacity(
              opacity: _opacity.value,
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                  border: Border.all(color: widget.color, width: 2),
                  boxShadow: [
                    BoxShadow(color: widget.color.withValues(alpha: 0.4), blurRadius: 8)
                  ],
                ),
                child: Text(
                  widget.text,
                  style: TextStyle(
                    fontWeight: FontWeight.w800,
                    color: widget.color,
                    fontSize: 14,
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// ========== 呼吸动画图标（用于地图上的模块按钮）==========
class BreathingIcon extends StatefulWidget {
  final IconData icon;
  final double size;
  final Color color;
  final String label;
  final Color badgeColor;
  final VoidCallback? onTap;
  final String emojiFallback;

  const BreathingIcon({
    super.key,
    required this.icon,
    this.size = 64,
    required this.color,
    required this.label,
    required this.badgeColor,
    this.onTap,
    required this.emojiFallback,
  });

  @override
  State<BreathingIcon> createState() => _BreathingIconState();
}

class _BreathingIconState extends State<BreathingIcon>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  late final Animation<double> _scale;
  late final Animation<double> _fade;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 2600),
    )..repeat(reverse: true);
    _scale = Tween<double>(begin: 0.95, end: 1.08).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeInOut),
    );
    _fade = Tween<double>(begin: 0.5, end: 1.0).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _controller,
      builder: (_, __) {
        return GestureDetector(
          onTap: widget.onTap,
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            Stack(
              alignment: Alignment.center,
              children: [
                // 外层光晕
                Container(
                  width: widget.size * 1.5,
                  height: widget.size * 1.5,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: widget.color.withValues(alpha: 0.15 * _fade.value),
                  ),
                ),
                // 中层光圈
                Container(
                  width: widget.size * 1.2,
                  height: widget.size * 1.2,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: widget.color.withValues(alpha: 0.25 * _fade.value),
                  ),
                ),
                // 主体
                Transform.scale(
                  scale: _scale.value,
                  child: Container(
                    width: widget.size,
                    height: widget.size,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      gradient: RadialGradient(
                        colors: [Colors.white, widget.badgeColor],
                        center: Alignment.topLeft,
                        radius: 1.0,
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: widget.color.withValues(alpha: 0.5),
                          blurRadius: 12,
                          offset: const Offset(0, 4),
                        ),
                      ],
                      border: Border.all(color: Colors.white, width: 3),
                    ),
                    child: Center(
                      child: Text(
                        widget.emojiFallback,
                        style: TextStyle(fontSize: widget.size * 0.5),
                      ),
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
                    color: Colors.black.withValues(alpha: 0.1),
                    blurRadius: 4,
                  )
                ],
              ),
              child: Text(
                widget.label,
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w700,
                  color: widget.color,
                ),
              ),
            ),
          ]),
        );
      },
    );
  }
}

// ========== 进度条 ==========
class FarmProgressBar extends StatelessWidget {
  final double progress; // 0.0 ~ 1.0
  final Color color;
  final double? width;
  final double height;

  const FarmProgressBar({
    super.key,
    required this.progress,
    required this.color,
    this.width,
    this.height = 6,
  });

  @override
  Widget build(BuildContext context) {
    final p = progress.clamp(0.0, 1.0);
    return Container(
      width: width,
      height: height,
      decoration: BoxDecoration(
        color: Colors.grey.shade200,
        borderRadius: BorderRadius.circular(height / 2),
      ),
      child: FractionallySizedBox(
        alignment: Alignment.centerLeft,
        widthFactor: p,
        child: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(colors: [color, color.withValues(alpha: 0.7)]),
            borderRadius: BorderRadius.circular(height / 2),
            boxShadow: [
              BoxShadow(color: color.withValues(alpha: 0.4), blurRadius: 4),
            ],
          ),
        ),
      ),
    );
  }
}

// ========== 标签徽章（用于品质）==========
class QualityBadge extends StatelessWidget {
  final int quality;
  final bool compact;

  const QualityBadge({super.key, required this.quality, this.compact = false});

  @override
  Widget build(BuildContext context) {
    final color = AppTheme.getQualityColor(quality);
    final text = AppTheme.getQualityText(quality);
    return Container(
      padding: EdgeInsets.symmetric(
        horizontal: compact ? 6 : 10,
        vertical: compact ? 2 : 5,
      ),
      decoration: BoxDecoration(
        gradient: LinearGradient(colors: [color, color.withValues(alpha: 0.7)]),
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(color: color.withValues(alpha: 0.3), blurRadius: 4)
        ],
      ),
      child: Text(
        text,
        style: TextStyle(
          color: Colors.white,
          fontSize: compact ? 10 : 12,
          fontWeight: FontWeight.w700,
        ),
      ),
    );
  }
}

// ========== provider 辅助：在 Widget 树中显示 toast ==========
final toastProvider =
    StateNotifierProvider<ToastNotifier, List<ToastItem>>((ref) {
  return ToastNotifier();
});

class ToastItem {
  final String text;
  final IconData icon;
  final Color color;
  ToastItem({required this.text, required this.icon, required this.color});
}

class ToastNotifier extends StateNotifier<List<ToastItem>> {
  ToastNotifier() : super(const []);
  void show(ToastItem item) {
    state = [...state, item];
    Future.delayed(const Duration(milliseconds: 2000), () {
      if (mounted) {
        state = state.where((e) => e != item).toList();
      }
    });
  }
}

class ToastStack extends ConsumerWidget {
  final Widget child;
  const ToastStack({super.key, required this.child});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final toasts = ref.watch(toastProvider);
    return Stack(
      alignment: Alignment.topCenter,
      children: [
        child,
        Positioned(
          top: 80,
          child: Column(
            children: toasts
                .map((t) => Padding(
                      padding: const EdgeInsets.symmetric(vertical: 4),
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 16, vertical: 10),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(30),
                          border: Border.all(color: t.color, width: 2),
                          boxShadow: [
                            BoxShadow(
                              color: t.color.withValues(alpha: 0.3),
                              blurRadius: 10,
                              offset: const Offset(0, 4),
                            )
                          ],
                        ),
                        child: Row(mainAxisSize: MainAxisSize.min, children: [
                          Icon(t.icon, color: t.color, size: 16),
                          const SizedBox(width: 6),
                          Text(t.text,
                              style: TextStyle(
                                  color: t.color,
                                  fontWeight: FontWeight.w700,
                                  fontSize: 13)),
                        ]),
                      ),
                    ))
                .toList(),
          ),
        ),
      ],
    );
  }
}
