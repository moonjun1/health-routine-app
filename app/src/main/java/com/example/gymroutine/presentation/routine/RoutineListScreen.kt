package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.util.Resource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Routine list screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    viewModel: RoutineListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToAIRoutine: () -> Unit = {},
    onRoutineSelected: (Routine) -> Unit
) {
    val routinesState by viewModel.routinesState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<Routine?>(null) }
    var showQuickRecordDialog by remember { mutableStateOf(false) }
    var routineToRecord by remember { mutableStateOf<Routine?>(null) }

    // Handle delete state
    LaunchedEffect(deleteState) {
        when (deleteState) {
            is Resource.Success -> {
                viewModel.resetDeleteState()
            }
            is Resource.Error -> {
                // Show error and reset
                viewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("내 루틴") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate
            ) {
                Icon(Icons.Default.Add, "루틴 추가")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = routinesState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    if (state.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    "아직 생성된 루틴이 없습니다",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Button(onClick = onNavigateToAIRoutine) {
                                    Icon(Icons.Default.Star, null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("AI로 루틴 생성")
                                }
                                OutlinedButton(onClick = onNavigateToCreate) {
                                    Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("직접 루틴 만들기")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNavigateToAIRoutine() }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Star,
                                                "AI",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    "AI로 맞춤 루틴 생성",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                )
                                                Text(
                                                    "당신의 목표에 맞는 최적의 루틴을 AI가 만들어드립니다",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            items(state.data) { routine ->
                                RoutineListItem(
                                    routine = routine,
                                    onClick = { onRoutineSelected(routine) },
                                    onDelete = {
                                        routineToDelete = routine
                                        showDeleteDialog = true
                                    },
                                    onQuickRecord = {
                                        routineToRecord = routine
                                        showQuickRecordDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadRoutines() }) {
                                Text("다시 시도")
                            }
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("루틴 목록을 불러오는 중...")
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && routineToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("루틴 삭제") },
            text = { Text("'${routineToDelete?.name}' 루틴을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRoutine(routineToDelete!!.id)
                        showDeleteDialog = false
                        routineToDelete = null
                    }
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    // Quick record dialog
    if (showQuickRecordDialog && routineToRecord != null) {
        QuickRecordDialog(
            routineName = routineToRecord!!.name,
            onDismiss = {
                showQuickRecordDialog = false
                routineToRecord = null
            },
            onConfirm = { date, duration, notes ->
                viewModel.addWorkoutRecord(
                    routineId = routineToRecord!!.id,
                    routineName = routineToRecord!!.name,
                    date = date,
                    duration = duration,
                    notes = notes
                )
                showQuickRecordDialog = false
                routineToRecord = null
            }
        )
    }
}

@Composable
fun RoutineListItem(
    routine: Routine,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onQuickRecord: () -> Unit = {}
) {
    val dateFormat = remember { SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.titleMedium
                )

                if (routine.description.isNotEmpty()) {
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (routine.category.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = routine.category,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${routine.exercises.size}개 운동",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "최근 수정: ${dateFormat.format(Date(routine.updatedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onQuickRecord) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "기록하기",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
