package com.shadow.deepseekimp.ui.nav

import androidx.compose.runtime.Composable
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