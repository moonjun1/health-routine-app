package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymroutine.data.model.Routine

/**
 * Routine detail screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routine: Routine,
    onNavigateBack: () -> Unit,
    onEditRoutine: () -> Unit = {},
    onExerciseClick: (String) -> Unit = {},
    onRecordWorkout: (date: Long, duration: Int, notes: String) -> Unit = { _, _, _ -> }
) {
    var showQuickRecordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routine.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onEditRoutine) {
                        Icon(Icons.Default.Edit, "수정")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuickRecordDialog = true }
            ) {
                Icon(Icons.Default.Add, "운동 기록하기")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description card
            if (routine.description.isNotEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "설명",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                routine.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Exercise list
            item {
                Text(
                    "운동 목록 (${routine.exercises.size}개)",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (routine.exercises.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Text(
                                "아직 추가된 운동이 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(routine.exercises) { exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onExerciseClick(exercise.exerciseId) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    exercise.exerciseName,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${exercise.sets}세트 × ${exercise.reps}회",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (exercise.weight > 0) {
                                    Text(
                                        "무게: ${exercise.weight}kg",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = "자세히 보기",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Quick record dialog
        if (showQuickRecordDialog) {
            QuickRecordDialog(
                routineName = routine.name,
                onDismiss = { showQuickRecordDialog = false },
                onConfirm = { date, duration, notes ->
                    onRecordWorkout(date, duration, notes)
                    showQuickRecordDialog = false
                }
            )
        }
    }
}
