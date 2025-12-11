package com.example.gymroutine.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gymroutine.data.model.Exercise
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.presentation.auth.LoginScreen
import com.example.gymroutine.presentation.auth.SignupScreen
import com.example.gymroutine.presentation.exercise.ExerciseDetailScreen
import com.example.gymroutine.presentation.exercise.ExerciseListScreen
import com.example.gymroutine.presentation.gym.GymRegisterScreen
import com.example.gymroutine.presentation.gym.GymSearchScreen
import com.example.gymroutine.presentation.home.HomeScreen
import com.example.gymroutine.presentation.routine.AIRoutineScreen
import com.example.gymroutine.presentation.routine.ExerciseSelectionScreen
import com.example.gymroutine.presentation.routine.RoutineCreateScreen
import com.example.gymroutine.presentation.routine.RoutineCreateViewModel
import com.example.gymroutine.presentation.routine.RoutineDetailScreen
import com.example.gymroutine.presentation.routine.RoutineListScreen
import com.example.gymroutine.presentation.settings.SettingsScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Main navigation graph for the app
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onSkipLogin = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Main screens
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGymSearch = {
                    navController.navigate(Screen.GymSearch.route)
                },
                onNavigateToExerciseList = {
                    navController.navigate(Screen.ExerciseList.route)
                },
                onNavigateToRoutineList = {
                    navController.navigate(Screen.RoutineList.route)
                },
                onRoutineSelected = { routine ->
                    val routineJson = URLEncoder.encode(Gson().toJson(routine), StandardCharsets.UTF_8.toString())
                    navController.navigate("routine_detail/$routineJson")
                }
            )
        }

        // Gym screens
        composable(Screen.GymSearch.route) {
            GymSearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onGymSelected = { gym ->
                    val gymJson = URLEncoder.encode(Gson().toJson(gym), StandardCharsets.UTF_8.toString())
                    navController.navigate("gym_register/$gymJson")
                }
            )
        }

        composable(
            route = "gym_register/{gymJson}",
            arguments = listOf(
                navArgument("gymJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gymJson = backStackEntry.arguments?.getString("gymJson")
            val gym = try {
                gymJson?.let {
                    val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Gson().fromJson(decoded, Gym::class.java)
                }
            } catch (e: Exception) {
                null
            }

            if (gym == null) {
                navController.popBackStack()
                return@composable
            }

            GymRegisterScreen(
                gym = gym,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistrationSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.GymSearch.route) { inclusive = true }
                    }
                }
            )
        }

        // Exercise screens
        composable(Screen.ExerciseList.route) {
            ExerciseListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onExerciseSelected = { exercise ->
                    val exerciseJson = URLEncoder.encode(Gson().toJson(exercise), StandardCharsets.UTF_8.toString())
                    navController.navigate("exercise_detail/$exerciseJson")
                }
            )
        }

        composable(
            route = "exercise_detail/{exerciseJson}",
            arguments = listOf(
                navArgument("exerciseJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exerciseJson = backStackEntry.arguments?.getString("exerciseJson")
            val exercise = try {
                exerciseJson?.let {
                    val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Gson().fromJson(decoded, Exercise::class.java)
                }
            } catch (e: Exception) {
                null
            }

            if (exercise == null) {
                navController.popBackStack()
                return@composable
            }

            ExerciseDetailScreen(
                exercise = exercise,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Routine screens
        composable(Screen.RoutineList.route) {
            RoutineListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.RoutineCreate.route)
                },
                onNavigateToAIRoutine = {
                    navController.navigate(Screen.AIRoutine.route)
                },
                onRoutineSelected = { routine ->
                    val routineJson = URLEncoder.encode(Gson().toJson(routine), StandardCharsets.UTF_8.toString())
                    navController.navigate("routine_detail/$routineJson")
                }
            )
        }

        composable(Screen.RoutineCreate.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screen.RoutineCreate.route)
            }
            val viewModel: RoutineCreateViewModel = hiltViewModel(parentEntry)

            RoutineCreateScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToExerciseSelection = {
                    navController.navigate(Screen.ExerciseSelection.route)
                },
                onRoutineCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ExerciseSelection.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Screen.RoutineCreate.route)
            }
            val viewModel: RoutineCreateViewModel = hiltViewModel(parentEntry)

            val allExercises by viewModel.allExercises.collectAsState()
            val selectedExercises by viewModel.selectedExercises.collectAsState()
            val selectedIds = selectedExercises.map { it.exercise.id }

            ExerciseSelectionScreen(
                allExercises = allExercises,
                selectedExerciseIds = selectedIds,
                onExerciseSelected = { exercise ->
                    viewModel.addExercise(exercise)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AIRoutine.route) {
            AIRoutineScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRoutineCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "routine_detail/{routineJson}",
            arguments = listOf(
                navArgument("routineJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routineJson = backStackEntry.arguments?.getString("routineJson")
            val routine = try {
                routineJson?.let {
                    val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Gson().fromJson(decoded, Routine::class.java)
                }
            } catch (e: Exception) {
                null
            }

            if (routine == null) {
                navController.popBackStack()
                return@composable
            }

            RoutineDetailScreen(
                routine = routine,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Settings screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToGymSearch = {
                    navController.navigate(Screen.GymSearch.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // MyPage screen
        composable(Screen.MyPage.route) {
            com.example.gymroutine.presentation.mypage.MyPageScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onNavigateToChangePassword = {
                    navController.navigate(Screen.ChangePassword.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Edit Profile screen
        composable(Screen.EditProfile.route) {
            com.example.gymroutine.presentation.mypage.EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Change Password screen
        composable(Screen.ChangePassword.route) {
            com.example.gymroutine.presentation.mypage.ChangePasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
