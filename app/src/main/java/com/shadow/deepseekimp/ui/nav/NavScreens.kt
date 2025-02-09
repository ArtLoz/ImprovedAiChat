package com.app.trainr.ui.nav

import kotlinx.serialization.Serializable

sealed class NavScreens {

    @Serializable
    data object SplashScreen : NavScreens()

    @Serializable
    data object OnboardingScreen : NavScreens()

    @Serializable
    data object MainScreen: NavScreens()

    @Serializable
    data object AuthScreen: NavScreens()
}