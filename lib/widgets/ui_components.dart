import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';

import '../theme/app_theme.dart';
import '../providers/game_providers.dart';
import '../models/quality.dart';

// ============================================
// 通用圆角卡片容器
// ============================================
class AppCard extends StatelessWidget {
  final Widget child;
  final EdgeInsets padding;
  final EdgeInsets margin;
  final Color? color;
  final double elevation;
  final double borderRadius;
  final VoidCallback? onTap;
  final Border? border;
  final List<Color>? gradientColors;

  const AppCard({
    super.key,
    required this.child,
    this.padding = const EdgeInsets.all(16),
    this.margin = const EdgeInsets.all(8),
    this.color,
    this.elevation = 3,
    this.borderRadius = 16,
    this.onTap,
    this.border,
    this.gradientColors,
  });

  @override
  Widget build(BuildContext context) {
    final decoration = BoxDecoration(
      color: gradientColors != null ? null : (color ?? AppTheme.surface),
      gradient: gradientColors != null
          ? LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: gradientColors!,
            )
          : null,
      borderRadius: BorderRadius.circular(borderRadius),
      border: border,
      boxShadow: [
        BoxShadow(
          color: Colors.black.withOpacity(0.1 * elevation / 3),
          blurRadius: 4 * elevation / 3,
          spreadRadius: 1,
          offset: Offset(0, 2 * elevation / 3),
        ),
      ],
    );

    Widget content = Container(
      decoration: decoration,
      padding: padding,
      child: child,
    );

    if (onTap != null) {
      return Padding(
        padding: margin,
        child: Material(
          color: Colors.transparent,
          child: InkWell(
            onTap: onTap,
            borderRadius: BorderRadius.circular(borderRadius),
            child: content,
          ),
        ),
      );
    }

    return Padding(padding: margin, child: content);
  }
}

// ============================================
// 渐变色卡片
// ============================================
class GradientCard extends StatelessWidget {
  final Widget child;
  final EdgeInsets padding;
  final EdgeInsets margin;
  final List<Color> colors;
  final double borderRadius;
  final VoidCallback? onTap;

  const GradientCard({
    super.key,
    required this.child,
    this.padding = const EdgeInsets.all(16),
    this.margin = const EdgeInsets.all(8),
    this.colors = const [AppTheme.primary, AppTheme.primaryLight],
    this.borderRadius = 16,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return AppCard(
      padding: padding,
      margin: margin,
      borderRadius: borderRadius,
      onTap: onTap,
      gradientColors: colors,
      child: child,
    );
  }
}

// ============================================
// 通用按钮
// ============================================
class AppButton extends StatelessWidget {
  final String text;
  final IconData? icon;
  final VoidCallback? onPressed;
  final Color? color;
  final Color textColor;
  final double borderRadius;
  final EdgeInsets padding;
  final bool outlined;
  final bool expanded;

  const AppButton({
    super.key,
    required this.text,
    this.icon,
    this.onPressed,
    this.color,
    this.textColor = Colors.white,
    this.borderRadius = 12,
    this.padding = const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
    this.outlined = false,
    this.expanded = true,
  });

  @override
  Widget build(BuildContext context) {
    Widget child = Row(
      mainAxisSize: expanded ? MainAxisSize.max : MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        if (icon != null) ...[
          Icon(icon, size: 20, color: outlined ? (color ?? AppTheme.primary) : textColor),
          const SizedBox(width: 8),
        ],
        Text(
          text,
          style: TextStyle(
            fontWeight: FontWeight.w600,
            color: outlined ? (color ?? AppTheme.primary) : textColor,
          ),
        ),
      ],
    );

    if (outlined) {
      return OutlinedButton(
        onPressed: onPressed,
        style: OutlinedButton.styleFrom(
          foregroundColor: color ?? AppTheme.primary,
          side: BorderSide(color: color ?? AppTheme.primary, width: 2),
          padding: padding,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(borderRadius),
          ),
        ),
        child: child,
      );
    }

    return ElevatedButton(
      onPressed: onPressed,
      style: ElevatedButton.styleFrom(
        backgroundColor: color ?? AppTheme.primary,
        foregroundColor: textColor,
        padding: padding,
        elevation: 2,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(borderRadius),
        ),
      ),
      child: child,
    );
  }
}

// ============================================
// 状态标签
// ============================================
class StatusChip extends StatelessWidget {
  final IconData? icon;
  final String emoji;
  final String text;
  final Color color;
  final double fontSize;

  const StatusChip({
    super.key,
    this.icon,
    this.emoji = '',
    required this.text,
    this.color = AppTheme.primary,
    this.fontSize = 14,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: color.withOpacity(0.15),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: color.withOpacity(0.3)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (emoji.isNotEmpty)
            Text(emoji, style: TextStyle(fontSize: fontSize))
          else if (icon != null)
            Icon(icon, size: fontSize, color: color),
          const SizedBox(width: 4),
          Text(
            text,
            style: TextStyle(fontSize: fontSize, fontWeight: FontWeight.w600, color: color),
          ),
        ],
      ),
    );
  }
}

// ============================================
// 品质指示器
// ============================================
class QualityIndicator extends StatelessWidget {
  final int quality;
  final double size;
  final bool showText;

  const QualityIndicator({
    super.key,
    required this.quality,
    this.size = 12,
    this.showText = true,
  });

  @override
  Widget build(BuildContext context) {
    final color = AppTheme.getQualityColor(quality);
    final text = AppTheme.getQualityText(quality);

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
      decoration: BoxDecoration(
        color: color.withOpacity(0.15),
        borderRadius: BorderRadius.circular(6),
        border: Border.all(color: color.withOpacity(0.5)),
      ),
      child: showText
          ? Text(text, style: TextStyle(fontSize: size, fontWeight: FontWeight.w600, color: color))
          : Icon(Icons.star, size: size, color: color),
    );
  }
}

// ============================================
// 图标卡片
// ============================================
class IconCard extends StatelessWidget {
  final String emoji;
  final double size;
  final Color? bgColor;
  final double borderWidth;
  final Color borderColor;

  const IconCard({
    super.key,
    required this.emoji,
    this.size = 40,
    this.bgColor,
    this.borderWidth = 1.5,
    this.borderColor = AppTheme.gold,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size * 1.8,
      height: size * 1.8,
      decoration: BoxDecoration(
        color: bgColor ?? AppTheme.surfaceMuted,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: borderColor, width: borderWidth),
      ),
      child: Center(
        child: Text(emoji, style: TextStyle(fontSize: size)),
      ),
    );
  }
}

// ============================================
// 分隔线
// ============================================
class SectionDivider extends StatelessWidget {
  final String? title;
  final Color color;
  final EdgeInsets padding;

  const SectionDivider({
    super.key,
    this.title,
    this.color = const Color(0xFFE0E0E0),
    this.padding = const EdgeInsets.symmetric(vertical: 16, horizontal: 24),
  });

  @override
  Widget build(BuildContext context) {
    if (title != null) {
      return Padding(
        padding: padding,
        child: Row(
          children: [
            Expanded(child: Container(height: 1, color: color)),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 12),
              child: Text(
                title!,
                style: TextStyle(fontWeight: FontWeight.w600, color: AppTheme.textSecondary),
              ),
            ),
            Expanded(child: Container(height: 1, color: color)),
          ],
        ),
      );
    }
    return Padding(padding: padding, child: Container(height: 1, color: color));
  }
}

// ============================================
// 金币/资源显示
// ============================================
class ResourceDisplay extends StatelessWidget {
  final IconData? icon;
  final String emoji;
  final int amount;
  final Color color;

  const ResourceDisplay({
    super.key,
    this.icon,
    this.emoji = '',
    required this.amount,
    this.color = AppTheme.gold,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: color.withOpacity(0.15),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(emoji.isNotEmpty ? emoji : '', style: const TextStyle(fontSize: 16)),
          if (emoji.isEmpty && icon != null) Icon(icon!, size: 16, color: color),
          const SizedBox(width: 4),
          Text(
            '$amount',
            style: TextStyle(fontWeight: FontWeight.bold, color: color),
          ),
        ],
      ),
    );
  }
}

// ============================================
// 进度条
// ============================================
class ProgressBar extends StatelessWidget {
  final double progress;
  final Color color;
  final Color backgroundColor;
  final double height;
  final double borderRadius;
  final String? label;

  const ProgressBar({
    super.key,
    required this.progress,
    this.color = AppTheme.primary,
    this.backgroundColor = AppTheme.surfaceMuted,
    this.height = 8,
    this.borderRadius = 6,
    this.label,
  });

  @override
  Widget build(BuildContext context) {
    final value = progress.clamp(0.0, 1.0);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (label != null)
          Padding(
            padding: const EdgeInsets.only(bottom: 4),
            child: Text(label!, style: TextStyle(fontSize: 12, color: AppTheme.textSecondary)),
          ),
        Container(
          height: height,
          decoration: BoxDecoration(color: backgroundColor, borderRadius: BorderRadius.circular(borderRadius)),
          child: Stack(
            children: [
              AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                width: MediaQuery.of(context).size.width * value,
                decoration: BoxDecoration(
                  gradient: LinearGradient(colors: [color, color.withOpacity(0.7)]),
                  borderRadius: BorderRadius.circular(borderRadius),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

// ============================================
// 模块按钮（用于世界地图）
// ============================================
class ModuleButton extends StatelessWidget {
  final String emoji;
  final String label;
  final VoidCallback onTap;
  final bool unlocked;
  final String? unlockHint;
  final Color color;

  const ModuleButton({
    super.key,
    required this.emoji,
    required this.label,
    required this.onTap,
    this.unlocked = true,
    this.unlockHint,
    this.color = AppTheme.primary,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: unlocked ? onTap : () {
        if (unlockHint != null) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(unlockHint!), duration: const Duration(seconds: 2)),
          );
        }
      },
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        curve: Curves.easeOut,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: unlocked
                ? [color.withOpacity(0.9), color.withOpacity(0.7)]
                : [Colors.grey.shade300, Colors.grey.shade400],
          ),
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: (unlocked ? color : Colors.grey).withOpacity(0.3),
              blurRadius: 12,
              offset: const Offset(0, 6),
            ),
          ],
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TweenAnimationBuilder(
              tween: Tween<double>(begin: 0.8, end: 1.0),
              duration: const Duration(milliseconds: 600),
              builder: (_, double scale, __) {
                return Transform.scale(
                  scale: scale,
                  child: Text(emoji, style: const TextStyle(fontSize: 48)),
                );
              },
            ),
            const SizedBox(height: 8),
            Text(
              label,
              style: GoogleFonts.notoSansSc(
                fontSize: 14,
                fontWeight: FontWeight.bold,
                color: unlocked ? Colors.white : Colors.white.withOpacity(0.7),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ============================================
// 物品列表项
// ============================================
class InventoryListItem extends StatelessWidget {
  final String emoji;
  final String name;
  final int quantity;
  final int quality;
  final int sellPrice;
  final VoidCallback? onSell;
  final VoidCallback? onTap;

  const InventoryListItem({
    super.key,
    required this.emoji,
    required this.name,
    required this.quantity,
    this.quality = 0,
    this.sellPrice = 0,
    this.onSell,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return AppCard(
      onTap: onTap,
      child: Row(
        children: [
          IconCard(emoji: emoji, size: 32),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(name, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600, color: AppTheme.textPrimary)),
                const SizedBox(height: 4),
                Row(
                  children: [
                    QualityIndicator(quality: quality, size: 10),
                    const SizedBox(width: 8),
                    Text('数量: $quantity', style: TextStyle(fontSize: 12, color: AppTheme.textSecondary)),
                  ],
                ),
              ],
            ),
          ),
          if (onSell != null)
            AppButton(
              text: '出售',
              icon: Icons.attach_money,
              color: AppTheme.gold,
              onPressed: onSell,
              expanded: false,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            ),
        ],
      ),
    );
  }
}

// ============================================
// 商店物品
// ============================================
class ShopItem extends StatelessWidget {
  final String emoji;
  final String name;
  final int price;
  final String? description;
  final int unlockLevel;
  final int currentLevel;
  final VoidCallback? onBuy;

  const ShopItem({
    super.key,
    required this.emoji,
    required this.name,
    required this.price,
    this.description,
    this.unlockLevel = 1,
    this.currentLevel = 1,
    this.onBuy,
  });

  bool get _unlocked => currentLevel >= unlockLevel;

  @override
  Widget build(BuildContext context) {
    return AppCard(
      border: !_unlocked ? Border.all(color: Colors.grey.shade300) : null,
      child: Opacity(
        opacity: _unlocked ? 1.0 : 0.5,
        child: Row(
          children: [
            IconCard(emoji: emoji, size: 36),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(name, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600, color: AppTheme.textPrimary)),
                  if (description != null) ...[
                    const SizedBox(height: 4),
                    Text(description!, style: TextStyle(fontSize: 12, color: AppTheme.textSecondary)),
                  ],
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      Text('💰 $price', style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w600, color: AppTheme.gold)),
                      if (!_unlocked) ...[
                        const SizedBox(width: 12),
                        Text('Lv.$unlockLevel 解锁', style: const TextStyle(fontSize: 12, color: Colors.redAccent)),
                      ],
                    ],
                  ),
                ],
              ),
            ),
            if (_unlocked && onBuy != null)
              AppButton(
                text: '购买',
                icon: Icons.add_shopping_cart,
                onPressed: onBuy,
                expanded: false,
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              ),
          ],
        ),
      ),
    );
  }
}
