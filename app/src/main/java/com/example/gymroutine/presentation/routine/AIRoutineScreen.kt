package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.AIRoutineResponse
import com.example.gymroutine.util.Resource

/**
 * AI-powered routine generation screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIRoutineScreen(
    viewModel: AIRoutineViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRoutineCreated: () -> Unit
) {
    val goal by viewModel.goal.collectAsState()
    val experienceLevel by viewModel.experienceLevel.collectAsState()
    val workoutsPerWeek by viewModel.workoutsPerWeek.collectAsState()
    val workoutDuration by viewModel.workoutDuration.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val additionalInfo by viewModel.additionalInfo.collectAsState()

    val userGyms by viewModel.userGyms.collectAsState()
    val selectedGym by viewModel.selectedGym.collectAsState()

    val generationState by viewModel.generationState.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    var showResultDialog by remember { mutableStateOf<AIRoutineResponse?>(null) }

    // Handle generation success
    LaunchedEffect(generationState) {
        if (generationState is Resource.Success) {
            showResultDialog = (generationState as Resource.Success<AIRoutineResponse>).data
        }
    }

    // Handle save success
    LaunchedEffect(saveState) {
        if (saveState is Resource.Success) {
            onRoutineCreated()
            viewModel.resetSaveState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 루틴 생성") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                }
            )
        },
        snackbarHost = {
            if (generationState is Resource.Error) {
                Snackbar {
                    Text((generationState as Resource.Error).message)
                }
            }
            if (saveState is Resource.Error) {
                Snackbar {
                    Text((saveState as Resource.Error).message)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "AI가 당신에게 맞는 완벽한 루틴을 만들어드립니다",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Gym selection
            item {
                GymSelectionSection(
                    userGyms = userGyms,
                    selectedGym = selectedGym,
                    onGymSelected = { viewModel.selectGym(it) }
                )
            }

            // Goal selection
            item {
                Text(
                    text = "운동 목표",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val goals = listOf("근력 증가", "체중 감량", "체력 향상", "근비대", "지구력")
                GoalSelectionChips(
                    goals = goals,
                    selectedGoal = goal,
                    onGoalSelected = { viewModel.updateGoal(it) }
                )
            }

            // Experience level
            item {
                Text(
                    text = "운동 경력",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val levels = listOf("초보자", "중급자", "고급자")
                ExperienceLevelChips(
                    levels = levels,
                    selectedLevel = experienceLevel,
                    onLevelSelected = { viewModel.updateExperienceLevel(it) }
                )
            }

            // Workouts per week
            item {
                Text(
                    text = "주당 운동 횟수: ${workoutsPerWeek}회",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = workoutsPerWeek.toFloat(),
                    onValueChange = { viewModel.updateWorkoutsPerWeek(it.toInt()) },
                    valueRange = 3f..6f,
                    steps = 2
                )
            }

            // Workout duration
            item {
                Text(
                    text = "운동 시간: ${workoutDuration}분",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = workoutDuration.toFloat(),
                    onValueChange = { viewModel.updateWorkoutDuration(it.toInt()) },
                    valueRange = 30f..120f,
                    steps = 8
                )
            }

            // Preferred categories
            item {
                Text(
                    text = "선호하는 운동 부위",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val categories = listOf("가슴", "등", "어깨", "팔", "하체", "코어")
                CategorySelectionChips(
                    categories = categories,
                    selectedCategories = selectedCategories,
                    onCategoryToggle = { viewModel.toggleCategory(it) }
                )
            }

            // Additional info
            item {
                Text(
                    text = "추가 정보 (선택사항)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { viewModel.updateAdditionalInfo(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("예: 허리 부상으로 인해 데드리프트 제외") },
                    minLines = 3
                )
            }

            // Generate button
            item {
                Button(
                    onClick = { viewModel.generateRoutine() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.canGenerateRoutine() && generationState !is Resource.Loading
                ) {
                    if (generationState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI가 루틴을 생성하는 중...")
                    } else {
                        Text("AI 루틴 생성")
                    }
                }
            }
        }
    }

    // Result dialog
    showResultDialog?.let { result ->
        AIRoutineResultDialog(
            result = result,
            onDismiss = {
                showResultDialog = null
                viewModel.resetGenerationState()
            },
            onSave = {
                viewModel.saveGeneratedRoutine(result)
                showResultDialog = null
            },
            isSaving = saveState is Resource.Loading
        )
    }
}

/**
 * Gym selection section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymSelectionSection(
    userGyms: List<com.example.gymroutine.data.model.Gym>,
    selectedGym: com.example.gymroutine.data.model.Gym?,
    onGymSelected: (com.example.gymroutine.data.model.Gym) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "헬스장 선택",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        if (userGyms.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "등록된 헬스장이 없습니다. 먼저 헬스장을 등록해주세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = selectedGym?.name ?: "헬스장을 선택하세요",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                selectedGym?.let { gym ->
                                    Text(
                                        text = "보유 기구: ${gym.equipments.size}개",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Icon(
                            Icons.Default.ArrowDropDown,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userGyms.forEach { gym ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = gym.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "보유 기구: ${gym.equipments.size}개",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                onGymSelected(gym)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    null,
                                    tint = if (selectedGym?.placeId == gym.placeId)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = if (selectedGym?.placeId == gym.placeId) {
                                {
                                    Icon(
                                        Icons.Default.Check,
                                        null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

/**
 * Goal selection chips
 */
@Composable
fun GoalSelectionChips(
    goals: List<String>,
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        goals.forEach { goal ->
            FilterChip(
                selected = selectedGoal == goal,
                onClick = { onGoalSelected(goal) },
                label = { Text(goal) }
            )
        }
    }
}

/**
 * Experience level chips
 */
@Composable
fun ExperienceLevelChips(
    levels: List<String>,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        levels.forEach { level ->
            FilterChip(
                selected = selectedLevel == level,
                onClick = { onLevelSelected(level) },
                label = { Text(level) }
            )
        }
    }
}

/**
 * Category selection chips
 */
@Composable
fun CategorySelectionChips(
    categories: List<String>,
    selectedCategories: List<String>,
    onCategoryToggle: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = { onCategoryToggle(category) },
                label = { Text(category) },
                leadingIcon = if (selectedCategories.contains(category)) {
                    { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                } else null
            )
        }
    }
}

/**
 * AI routine result dialog
 */
@Composable
fun AIRoutineResultDialog(
    result: AIRoutineResponse,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = result.routineName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = result.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Divider()
                }

                item {
                    Text(
                        text = "운동 목록 (${result.exercises.size}개)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(result.exercises) { exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "${result.exercises.indexOf(exercise) + 1}. ${exercise.exerciseName}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${exercise.sets}세트 × ${exercise.reps}회 | 휴식 ${exercise.restTime}초",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (exercise.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = exercise.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("루틴 저장")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) {
                Text("취소")
            }
        }
    )
}
