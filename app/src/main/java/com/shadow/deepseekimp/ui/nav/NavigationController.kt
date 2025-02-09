package com.app.trainr.ui.nav

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationController(
    val navHostController: NavHostController
) {
    fun navigate(route: Any) {
        navHostController.navigate(route) {
        }


    }

    fun navigatePopUp(route: Any, popUpRoute: Any) {
        navHostController.navigate(route) {
            popUpTo(popUpRoute) {
                inclusive = true
            }
        }
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }
}

@Composable
fun rememberNavigationController(
    navHostController: NavHostController = rememberNavController(),
): NavigationController {
    return NavigationController(
        navHostController = navHostController
    )
}