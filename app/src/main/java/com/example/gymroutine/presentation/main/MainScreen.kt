package com.example.gymroutine.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gymroutine.presentation.navigation.Screen

/**
 * Main screen with bottom navigation
 */
@Composable
fun MainScreen(
    navController: NavHostController,
    onNavigateToGymSearch: () -> Unit,
    onNavigateToExerciseList: () -> Unit,
    onNavigateToRoutineList: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMyPage: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
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
                            when (item) {
                                BottomNavItem.HOME -> {
                                    if (currentRoute != Screen.Home.route) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                                BottomNavItem.EXERCISE -> onNavigateToExerciseList()
                                BottomNavItem.ROUTINE -> onNavigateToRoutineList()
                                BottomNavItem.MYPAGE -> onNavigateToMyPage()
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        // NavHost content will be displayed here with bottom padding
        // The actual NavHost is in NavGraph.kt
        // This is just the shell that provides the BottomNavigationBar
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
