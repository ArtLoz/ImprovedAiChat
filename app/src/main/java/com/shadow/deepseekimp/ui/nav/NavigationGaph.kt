package com.app.trainr.ui.nav

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navigationController: NavigationController,
) {
    NavHost(
        modifier = modifier,
        navController = navigationController.navHostController,
        startDestination = NavScreens.SplashScreen
    ) {
    }
}