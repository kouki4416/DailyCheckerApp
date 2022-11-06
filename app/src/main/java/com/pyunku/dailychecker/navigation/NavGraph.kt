package com.pyunku.dailychecker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pyunku.dailychecker.calendar.presentation.CalendarRoute
import com.pyunku.dailychecker.calendar.presentation.CalendarScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    ){
    NavHost(
        navController = navController ,
        startDestination = Screen.Calendar.route
    ){
        composable(route = Screen.Calendar.route){
            CalendarRoute()
        }
//        composable(route = Screen.Splash.route){
//            SplashScreen(navController = navController)
//        }
//
//        composable(route = Screen.Calendar.route){
//            CalendarScreen(navController = navController)
//        }
//
//        composable(route = Screen.Setting.route){
//            SettingScreen(navController = navController)
//        }
    }
}