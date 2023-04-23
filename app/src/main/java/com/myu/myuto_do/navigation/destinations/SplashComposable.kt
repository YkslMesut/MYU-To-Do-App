package com.myu.myuto_do.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.myu.myuto_do.ui.screens.splash.SplashScreen
import com.myu.myuto_do.util.Constants

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
) {
    composable(
        route = Constants.SPLASH_SCREEN,
    ) {
        SplashScreen(
            navigateToListScreen = navigateToListScreen
        )
    }
}