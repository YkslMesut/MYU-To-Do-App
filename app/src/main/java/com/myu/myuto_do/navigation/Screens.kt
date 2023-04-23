package com.myu.myuto_do.navigation

import androidx.navigation.NavController
import com.myu.myuto_do.util.Action
import com.myu.myuto_do.util.Constants.LIST_SCREEN
import com.myu.myuto_do.util.Constants.SPLASH_SCREEN

class Screens(navController: NavController) {

    val splash: () -> Unit = {
        navController.navigate("list/${Action.NO_ACTION.name}") {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {
                inclusive = true
            }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/${taskId}")
    }
}