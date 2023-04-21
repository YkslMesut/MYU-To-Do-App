package com.myu.myuto_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.myu.myuto_do.navigation.SetupNavigation
import com.myu.myuto_do.ui.theme.MYUToDoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYUToDoTheme {
                navController = rememberNavController()
                SetupNavigation(navController = navController)
            }
        }
    }
}