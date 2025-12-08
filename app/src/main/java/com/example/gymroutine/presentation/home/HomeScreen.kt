package com.example.gymroutine.presentation.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.model.Routine
import com.example.gymroutine.util.Resource

/**
 * Home screen with gym info and quick access
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToGymSearch: () -> Unit = {},
    onNavigateToExerciseList: () -> Unit = {},
    onNavigateToRoutineList: () -> Unit = {},
    onRoutineSelected: (Routine) -> Unit = {}
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val gymState by viewModel.gymState.collectAsState()
    val recentRoutinesState by viewModel.recentRoutinesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("홈") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, "새로고침")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome message
            item {
                WelcomeCard(isLoggedIn = isLoggedIn)
            }

            // Gym info card
            if (isLoggedIn) {
                item {
                    when (val state = gymState) {
                        is Resource.Success -> {
                            GymInfoCard(
                                gym = state.data,
                                isOpen = viewModel.isGymOpen(state.data),
                                hours = viewModel.getTodayHours(state.data),
                                onChangeGym = onNavigateToGymSearch
                            )
                        }
                        is Resource.Loading -> {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        is Resource.Error -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "헬스장 미등록",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "헬스장을 등록하면 맞춤 운동을 추천받을 수 있습니다",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Button(onClick = onNavigateToGymSearch) {
                                        Icon(Icons.Default.LocationOn, null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("헬스장 검색")
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            // Recent routines
            item {
                Text(
                    text = "최근 루틴",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            when (val state = recentRoutinesState) {
                is Resource.Success -> {
                    if (state.data.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onNavigateToRoutineList
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "첫 루틴 만들기",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    } else {
                        items(state.data) { routine ->
                            RecentRoutineCard(
                                routine = routine,
                                onClick = { onRoutineSelected(routine) }
                            )
                        }
                        item {
                            TextButton(
                                onClick = onNavigateToRoutineList,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("모든 루틴 보기")
                                Icon(Icons.Default.KeyboardArrowRight, null)
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                else -> {}
            }

            // Quick menu
            item {
                Text(
                    text = "빠른 메뉴",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickMenuCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.FitnessCenter,
                        title = "운동 목록",
                        onClick = onNavigateToExerciseList
                    )
                    QuickMenuCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.List,
                        title = "내 루틴",
                        onClick = onNavigateToRoutineList
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickMenuCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocationOn,
                        title = "헬스장",
                        onClick = onNavigateToGymSearch
                    )
                    QuickMenuCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Star,
                        title = "AI 루틴",
                        onClick = onNavigateToRoutineList
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(isLoggedIn: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isLoggedIn) "안녕하세요! 👋" else "헬스 루틴 앱에 오신 것을 환영합니다",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isLoggedIn) "오늘도 힘찬 운동 하세요!" else "로그인 없이도 사용할 수 있습니다",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun GymInfoCard(
    gym: Gym,
    isOpen: Boolean,
    hours: String,
    onChangeGym: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "내 헬스장",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onChangeGym) {
                    Text("변경")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = gym.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = gym.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Divider()

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (isOpen)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = if (isOpen) "영업 중" else "영업 종료",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = hours,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun RecentRoutineCard(
    routine: Routine,
    onClick: () -> Unit
) {
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (routine.category.isNotEmpty()) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = routine.category,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "${routine.exercises.size}개 운동",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Icon(
                Icons.Default.KeyboardArrowRight,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickMenuCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
