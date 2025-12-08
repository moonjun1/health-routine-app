package com.example.gymroutine.presentation.navigation

/**
 * Navigation routes for Compose Navigation
 */
sealed class Screen(val route: String) {
    // Auth screens
    object Login : Screen("login")
    object Signup : Screen("signup")

    // Main screens
    object Home : Screen("home")
    object GymSearch : Screen("gym_search")
    object GymRegister : Screen("gym_register")
    object ExerciseList : Screen("exercise_list")
    object ExerciseDetail : Screen("exercise_detail/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_detail/$exerciseId"
    }
    object RoutineList : Screen("routine_list")
    object RoutineCreate : Screen("routine_create")
    object ExerciseSelection : Screen("exercise_selection")
    object RoutineDetail : Screen("routine_detail/{routineId}") {
        fun createRoute(routineId: String) = "routine_detail/$routineId"
    }
    object Settings : Screen("settings")
}
