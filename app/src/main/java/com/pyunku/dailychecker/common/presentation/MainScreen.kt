package com.pyunku.dailychecker

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.pyunku.dailychecker.navigation.Screen
import com.pyunku.dailychecker.navigation.SetupNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            Column {
                BottomBar(navController = navController)
                AdMobBar()
            }
        }
    ) {
        SetupNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = Screen.bottomNavigationScreens
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            AddItem(screen = screen,
                currentDestination = currentDestination,
                navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        selected = selected,
        icon = {
            Icon(
                painter = painterResource(screen.icon),
                contentDescription = "Navigation Icon",
                tint = if (selected) Color.White else Color.DarkGray.copy(alpha = 0.5f)
            )
        },
        label = {
            Text(text = screen.screenName)
        },
        onClick = {
            navController.navigate(screen.route)
        },
        //unselectedContentColor = LocalContentColor.current.copy(alpha = 0f)
    )
}

@Composable
fun AdMobBar() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
        AdView(it).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = "ca-app-pub-7719172067804321/1743343072"
            //Test
            //adUnitId = "ca-app-pub-3940256099942544/6300978111"
            loadAd(AdRequest.Builder().build())
        }
    })
}