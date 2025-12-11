package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.util.Resource

// 운동 선택 기능이 있는 루틴 생성 화면 (Phase 6)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineCreateScreen(
    viewModel: RoutineCreateViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseSelection: () -> Unit,
    onRoutineCreated: () -> Unit
) {
    val routineName by viewModel.routineName.collectAsState()
    val description by viewModel.description.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val createState by viewModel.createState.collectAsState()

    var showExerciseSettingsDialog by remember { mutableStateOf<SelectedExercise?>(null) }

    // 생성 상태 처리
    LaunchedEffect(createState) {
        when (createState) {
            is Resource.Success -> {
                onRoutineCreated()
                viewModel.resetCreateState()
            }
            is Resource.Error -> {
                // 오류는 아래 Snackbar에 표시됨
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("루틴 만들기") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                }
            )
        },
        snackbarHost = {
            if (createState is Resource.Error) {
                Snackbar {
                    Text((createState as Resource.Error).message)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 루틴 이름
                item {
                    OutlinedTextField(
                        value = routineName,
                        onValueChange = { viewModel.updateRoutineName(it) },
                        label = { Text("루틴 이름") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 설명
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text("설명 (선택사항)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }

                // 운동 목록 헤더
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "운동 목록 (${selectedExercises.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = onNavigateToExerciseSelection
                        ) {
                            Icon(Icons.Default.Add, "추가")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("운동 추가")
                        }
                    }
                }

                // 선택된 운동
                if (selectedExercises.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "운동을 추가해주세요",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(selectedExercises) { selectedExercise ->
                        SelectedExerciseItem(
                            selectedExercise = selectedExercise,
                            isFirst = selectedExercises.first() == selectedExercise,
                            isLast = selectedExercises.last() == selectedExercise,
                            onMoveUp = { viewModel.moveExerciseUp(selectedExercise.exercise.id) },
                            onMoveDown = { viewModel.moveExerciseDown(selectedExercise.exercise.id) },
                            onSettings = { showExerciseSettingsDialog = selectedExercise },
                            onRemove = { viewModel.removeExercise(selectedExercise.exercise.id) }
                        )
                    }
                }
            }

            // 생성 버튼
            Button(
                onClick = { viewModel.createRoutine() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = viewModel.canCreateRoutine() && createState !is Resource.Loading
            ) {
                if (createState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("루틴 생성")
                }
            }
        }
    }

    // 운동 설정 다이얼로그
    showExerciseSettingsDialog?.let { selectedExercise ->
        ExerciseSettingsDialog(
            selectedExercise = selectedExercise,
            onDismiss = { showExerciseSettingsDialog = null },
            onSave = { sets, reps, weight, restTime ->
                viewModel.updateExerciseSettings(
                    exerciseId = selectedExercise.exercise.id,
                    sets = sets,
                    reps = reps,
                    weight = weight,
                    restTime = restTime
                )
                showExerciseSettingsDialog = null
            }
        )
    }
}

// 선택된 운동 항목 컴포넌트
@Composable
fun SelectedExerciseItem(
    selectedExercise: SelectedExercise,
    isFirst: Boolean,
    isLast: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onSettings: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${selectedExercise.order + 1}. ${selectedExercise.exercise.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${selectedExercise.sets}세트 × ${selectedExercise.reps}회" +
                          (if (selectedExercise.weight > 0) " × ${selectedExercise.weight}kg" else "") +
                          " | 휴식 ${selectedExercise.restTime}초",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onMoveUp,
                    enabled = !isFirst
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, "위로 이동")
                }

                IconButton(
                    onClick = onMoveDown,
                    enabled = !isLast
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, "아래로 이동")
                }

                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, "설정")
                }

                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, "삭제")
                }
            }
        }
    }
}
