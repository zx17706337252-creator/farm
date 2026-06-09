import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';

import 'navigation/app_router.dart';
import 'theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    const ProviderScope(
      child: FarmLifeApp(),
    ),
  );
}

class FarmLifeApp extends StatelessWidget {
  const FarmLifeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '呆柳的农场',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.buildTheme(),
      initialRoute: AppRoutes.home,
      onGenerateRoute: AppRouter.generateRoute,
      locale: const Locale('zh', 'CN'),
      supportedLocales: const [
        Locale('zh', 'CN'),
      ],
      builder: (context, child) {
        return DefaultTextStyle(
          style: GoogleFonts.notoSansSc()
              .copyWith(color: const Color(0xFF3E2723)),
          child: child ?? const SizedBox.shrink(),
        );
      },
    );
  }
}
