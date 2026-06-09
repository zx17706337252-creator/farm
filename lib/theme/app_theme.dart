import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class AppTheme {
  // 颜色系统
  static const Color primary = Color(0xFF5DAF54); // 农场绿
  static const Color primaryLight = Color(0xFF8BC78A);
  static const Color primaryDark = Color(0xFF388E3C);
  
  static const Color brown = Color(0xFF8B5E3C); // 土褐色
  static const Color brownLight = Color(0xFFA9825C);
  
  static const Color gold = Color(0xFFF2B705); // 金黄色
  static const Color goldLight = Color(0xFFFFD54F);
  
  static const Color pink = Color(0xFFF06292); // 粉色
  static const Color pinkLight = Color(0xFFF8BBD0);
  
  static const Color blue = Color(0xFF4FC3F7); // 蓝色（鱼塘）
  static const Color orange = Color(0xFFFF8A65); // 橙色（果园）
  
  // 背景色
  static const Color background = Color(0xFFFFF5E1); // 奶油色
  static const Color surface = Color(0xFFFFFFFF);
  static const Color surfaceMuted = Color(0xFFFAF1DB);
  
  // 文字色
  static const Color textPrimary = Color(0xFF4E342E);
  static const Color textSecondary = Color(0xFF78909C);
  static const Color textMuted = Color(0xFF9E9E9E);
  
  // 品质颜色
  static const Color qualityCommon = Color(0xFF9E9E9E);
  static const Color qualityGood = Color(0xFF4CAF50);
  static const Color qualityRare = Color(0xFF2196F3);
  static const Color qualityEpic = Color(0xFF9C27B0);
  static const Color qualityLegendary = Color(0xFFFF9800);
  static const Color qualityMythical = Color(0xFFE91E63);
  
  // 构建主题
  static ThemeData buildTheme() {
    final base = ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      colorScheme: ColorScheme.light(
        primary: primary,
        onPrimary: Colors.white,
        secondary: brown,
        onSecondary: Colors.white,
        tertiary: gold,
        surface: surface,
        background: background,
        error: Colors.red.shade400,
      ),
      scaffoldBackgroundColor: background,
      appBarTheme: const AppBarTheme(
        backgroundColor: surface,
        foregroundColor: textPrimary,
        elevation: 2,
        centerTitle: true,
      ),
      cardTheme: CardThemeData(
        color: surface,
        elevation: 3,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
        ),
        margin: const EdgeInsets.all(8),
      ),
      buttonTheme: ButtonThemeData(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        height: 44,
      ),
      floatingActionButtonTheme: const FloatingActionButtonThemeData(
        backgroundColor: primary,
        foregroundColor: Colors.white,
        shape: CircleBorder(),
        elevation: 4,
      ),
      snackBarTheme: SnackBarThemeData(
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        backgroundColor: Colors.black87,
      ),
      dialogTheme: DialogTheme(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        backgroundColor: surface,
        titleTextStyle: GoogleFonts.notoSansSc(
          fontSize: 18,
          fontWeight: FontWeight.bold,
          color: textPrimary,
        ),
      ),
      bottomNavigationBarTheme: const BottomNavigationBarThemeData(
        backgroundColor: surface,
        selectedItemColor: primary,
        unselectedItemColor: textMuted,
        type: BottomNavigationBarType.fixed,
        elevation: 8,
      ),
      dividerTheme: const DividerThemeData(
        color: Color(0xFFE0E0E0),
        space: 1,
        thickness: 1,
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: surfaceMuted,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: primary, width: 2),
        ),
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      ),
      textTheme: TextTheme(
        displayLarge: GoogleFonts.notoSansSc(
          fontSize: 32,
          fontWeight: FontWeight.bold,
          color: textPrimary,
        ),
        displayMedium: GoogleFonts.notoSansSc(
          fontSize: 24,
          fontWeight: FontWeight.bold,
          color: textPrimary,
        ),
        titleLarge: GoogleFonts.notoSansSc(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: textPrimary,
        ),
        titleMedium: GoogleFonts.notoSansSc(
          fontSize: 16,
          fontWeight: FontWeight.w600,
          color: textPrimary,
        ),
        bodyLarge: GoogleFonts.notoSansSc(
          fontSize: 16,
          color: textPrimary,
        ),
        bodyMedium: GoogleFonts.notoSansSc(
          fontSize: 14,
          color: textPrimary,
        ),
        labelLarge: GoogleFonts.notoSansSc(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: textPrimary,
        ),
        labelMedium: GoogleFonts.notoSansSc(
          fontSize: 12,
          color: textSecondary,
        ),
        labelSmall: GoogleFonts.notoSansSc(
          fontSize: 10,
          color: textMuted,
        ),
      ),
    );
    
    return base;
  }
  
  // 获取品质颜色
  static Color getQualityColor(int qualityIndex) {
    return switch (qualityIndex) {
      0 => qualityCommon,
      1 => qualityGood,
      2 => qualityRare,
      3 => qualityEpic,
      4 => qualityLegendary,
      5 => qualityMythical,
      _ => qualityCommon,
    };
  }
  
  // 获取品质文字
  static String getQualityText(int qualityIndex) {
    return switch (qualityIndex) {
      0 => '普通',
      1 => '优良',
      2 => '稀有',
      3 => '史诗',
      4 => '传说',
      5 => '神话',
      _ => '普通',
    };
  }
}
