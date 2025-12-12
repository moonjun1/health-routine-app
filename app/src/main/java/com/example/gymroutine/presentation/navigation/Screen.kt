package com.example.gymroutine.presentation.navigation

/**
 * Compose Navigation을 위한 네비게이션 라우트
 */
sealed class Screen(val route: String) {
    // 인증 화면
    object Login : Screen("login")
    object Signup : Screen("signup")

    // 메인 화면
    object Home : Screen("home")
    object GymSearch : Screen("gym_search")
    object GymRegister : Screen("gym_register")
    object ExerciseList : Screen("exercise_list")
    object ExerciseDetail : Screen("exercise_detail/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_detail/$exerciseId"
    }
    object RoutineList : Screen("routine_list")
    object RoutineCreate : Screen("routine_create")
    object AIRoutine : Screen("ai_routine")
    object ExerciseSelection : Screen("exercise_selection")
    object RoutineDetail : Screen("routine_detail/{routineId}") {
        fun createRoute(routineId: String) = "routine_detail/$routineId"
    }
    object Settings : Screen("settings")
    object MyPage : Screen("mypage")
    object ChangePassword : Screen("change_password")
    object Calendar : Screen("calendar")
}
