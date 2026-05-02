package com.leomilano.diamond.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leomilano.diamond.ui.screens.addedit.DiamondFormScreen
import com.leomilano.diamond.ui.screens.dashboard.DashboardScreen
import com.leomilano.diamond.ui.screens.detail.DiamondDetailScreen
import com.leomilano.diamond.ui.screens.splash.SplashScreen

@Composable
fun DiamondNavHost(navController: NavHostController = rememberNavController()) {
    val enterTransition = fadeIn(animationSpec = tween(400)) +
        slideInHorizontally(
            animationSpec = tween(400),
            initialOffsetX = { it / 4 }
        )
    val exitTransition = fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { -it / 4 }
        )
    val popEnterTransition = fadeIn(animationSpec = tween(400)) +
        slideInHorizontally(
            animationSpec = tween(400),
            initialOffsetX = { -it / 4 }
        )
    val popExitTransition = fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { it / 4 }
        )

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(
            route = NavRoutes.Splash.route,
            enterTransition = { fadeIn(tween(600)) },
            exitTransition = { fadeOut(tween(600)) }
        ) {
            SplashScreen(
                onAnimationComplete = {
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.Dashboard.route,
            enterTransition = { fadeIn(tween(600)) },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) {
            DashboardScreen(
                onAddDiamond = {
                    navController.navigate(NavRoutes.AddDiamond.route)
                },
                onDiamondClick = { diamondId ->
                    navController.navigate(NavRoutes.DiamondDetail.createRoute(diamondId))
                }
            )
        }

        composable(
            route = NavRoutes.AddDiamond.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) {
            DiamondFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.EditDiamond.route,
            arguments = listOf(navArgument("diamondId") { type = NavType.LongType }),
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) { backStackEntry ->
            val diamondId = backStackEntry.arguments?.getLong("diamondId") ?: return@composable
            DiamondFormScreen(
                diamondId = diamondId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.DiamondDetail.route,
            arguments = listOf(navArgument("diamondId") { type = NavType.LongType }),
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) { backStackEntry ->
            val diamondId = backStackEntry.arguments?.getLong("diamondId") ?: return@composable
            DiamondDetailScreen(
                diamondId = diamondId,
                onNavigateBack = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(NavRoutes.EditDiamond.createRoute(diamondId))
                }
            )
        }
    }
}
