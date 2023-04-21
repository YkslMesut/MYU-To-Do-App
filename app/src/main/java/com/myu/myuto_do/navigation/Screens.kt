package com.myu.myuto_do.navigation

import androidx.navigation.NavController
import com.myu.myuto_do.util.Action
import com.myu.myuto_do.util.Constants.LIST_SCREEN

class Screens(navController: NavController) {

    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {
                inclusive = true
            }
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/${taskId}")
    }
}