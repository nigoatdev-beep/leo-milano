package com.leomilano.diamond.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Splash : NavRoutes("splash")
    data object Dashboard : NavRoutes("dashboard")
    data object AddDiamond : NavRoutes("add_diamond")
    data object EditDiamond : NavRoutes("edit_diamond/{diamondId}") {
        fun createRoute(diamondId: Long) = "edit_diamond/$diamondId"
    }
    data object DiamondDetail : NavRoutes("detail/{diamondId}") {
        fun createRoute(diamondId: Long) = "detail/$diamondId"
    }
}
