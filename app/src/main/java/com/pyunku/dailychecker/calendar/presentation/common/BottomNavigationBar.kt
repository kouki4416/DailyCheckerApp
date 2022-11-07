package com.pyunku.dailychecker.calendar.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.pyunku.dailychecker.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?
){
    NavigationBar {
        Screen.bottomNavigationScreens.forEach{ destination ->
            val selected = currentDestination?.hierarchy?.any{ it.route == destination.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route){
                        this.launchSingleTop = true
                        this.restoreState = true
                    }
                },
                icon = {
                   Icon(painter = painterResource(destination.icon), contentDescription = null)
                },
                label = {}
            )
        }
    }
}
