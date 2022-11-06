package com.pyunku.dailychecker.navigation

sealed class Screen(val route: String, val screenName: String){

    object Splash : Screen("splash_screen", "Splash")
    object Calendar : Screen("calendar_screen", "Calendar")
    object Setting : Screen("setting_screen", "Setting")

    companion object{
        val bottomNavigationScreens = listOf(
            Calendar,
            Setting
        )
    }
}