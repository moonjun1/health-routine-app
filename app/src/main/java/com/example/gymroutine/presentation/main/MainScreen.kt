package com.example.gymroutine.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gymroutine.presentation.navigation.NavGraph
import com.example.gymroutine.presentation.navigation.Screen

/**
 * Main screen with bottom navigation
 */
@Composable
fun MainScreen(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom navigation should only be visible on main screens
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.ExerciseList.route,
        Screen.RoutineList.route,
        Screen.MyPage.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomNavItem.values().forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * Bottom navigation items
 */
enum class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    HOME(
        route = Screen.Home.route,
        title = "홈",
        icon = Icons.Default.Home
    ),
    EXERCISE(
        route = Screen.ExerciseList.route,
        title = "운동",
        icon = Icons.Default.List
    ),
    ROUTINE(
        route = Screen.RoutineList.route,
        title = "루틴",
        icon = Icons.Default.List
    ),
    MYPAGE(
        route = Screen.MyPage.route,
        title = "마이",
        icon = Icons.Default.Person
    )
}
