package com.pyunku.dailychecker.navigation

import com.pyunku.dailychecker.R

sealed class Screen(val route: String, val screenName: String, val icon: Int) {

    object Splash : Screen(
        "splash_screen",
        "Splash",
        icon = R.drawable.ic_circle
    )

    object Calendar :
        Screen("calendar_screen", "Calendar", R.drawable.ic_baseline_calendar_month_24)

    object Setting :
        Screen("setting_screen", "Setting", R.drawable.ic_baseline_settings_24)

    object OssLicenses :
        Screen("oss_licence_activity", "OssLicence", 0)

    companion object {
        val bottomNavigationScreens = listOf(
            Calendar,
            Setting
        )
    }
}
//
//enum class TopLevelDestination(
//    val selectedIcon: Icon,
//    val unselectedIcon: Icon,
//) {
//    CALENDAR(
//        selectedIcon = Icon.DrawableResourceIcon(Icons.MenuCalendar),
//        unselectedIcon = Icon.DrawableResourceIcon(Icons.MenuCalendar)
//    ),
//
//    SETTING(
//        selectedIcon = Icon.DrawableResourceIcon(Icons.MenuSetting),
//        unselectedIcon = Icon.DrawableResourceIcon(Icons.MenuSetting)
//    )
//}