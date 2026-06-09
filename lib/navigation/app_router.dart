import 'package:flutter/material.dart';

import '../screens/world_map_screen.dart';
import '../screens/farmland_screen.dart';
import '../screens/livestock_screen.dart';
import '../screens/orchard_screen.dart';
import '../screens/factory_screen.dart';
import '../screens/fish_pond_screen.dart';
import '../screens/shop_screen.dart';
import '../screens/inventory_screen.dart';
import '../screens/collection_screen.dart';
import '../screens/pet_screen.dart';

class AppRoutes {
  static const String home = '/';
  static const String farmland = '/farmland';
  static const String livestock = '/livestock';
  static const String orchard = '/orchard';
  static const String factory = '/factory';
  static const String fishpond = '/fishpond';
  static const String shop = '/shop';
  static const String inventory = '/inventory';
  static const String collection = '/collection';
  static const String pet = '/pet';
}

class AppRouter {
  static Route<dynamic> generateRoute(RouteSettings settings) {
    WidgetBuilder builder;
    switch (settings.name) {
      case AppRoutes.home:
        builder = (_) => const WorldMapScreen();
        break;
      case AppRoutes.farmland:
        builder = (_) => const FarmlandScreen();
        break;
      case AppRoutes.livestock:
        builder = (_) => const LivestockScreen();
        break;
      case AppRoutes.orchard:
        builder = (_) => const OrchardScreen();
        break;
      case AppRoutes.factory:
        builder = (_) => const FactoryScreen();
        break;
      case AppRoutes.fishpond:
        builder = (_) => const FishPondScreen();
        break;
      case AppRoutes.shop:
        builder = (_) => const ShopScreen();
        break;
      case AppRoutes.inventory:
        builder = (_) => const InventoryScreen();
        break;
      case AppRoutes.collection:
        builder = (_) => const CollectionScreen();
        break;
      case AppRoutes.pet:
        builder = (_) => const PetScreen();
        break;
      default:
        builder = (_) => const WorldMapScreen();
    }

    return PageRouteBuilder(
      settings: settings,
      pageBuilder: (_, __, ___) => builder(_),
      transitionsBuilder: (context, animation, secondaryAnimation, child) {
        // 放大淡入 + 轻微旋转的过渡
        final curve = CurvedAnimation(
          parent: animation,
          curve: Curves.easeOutCubic,
        );
        final scale = Tween<double>(begin: 0.85, end: 1.0).animate(curve);
        final fade = Tween<double>(begin: 0.0, end: 1.0).animate(curve);
        return FadeTransition(
          opacity: fade,
          child: ScaleTransition(scale: scale, child: child),
        );
      },
      transitionDuration: const Duration(milliseconds: 420),
    );
  }
}
