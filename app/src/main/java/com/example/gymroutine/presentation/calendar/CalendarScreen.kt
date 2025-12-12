package com.example.gymroutine.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.WorkoutRecord
import com.example.gymroutine.util.Resource
import java.util.Calendar

/**
 * 월별 운동 기록을 표시하는 캘린더 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val currentYear by viewModel.currentYear.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val selectedDateRecords by viewModel.selectedDateRecords.collectAsState()
    val routinesState by viewModel.routinesState.collectAsState()

    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("운동 캘린더") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.goToToday() }) {
                        Icon(Icons.Default.DateRange, "오늘")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedDay != null) {
                        showAddDialog = true
                    }
                }
            ) {
                Icon(Icons.Default.Add, "운동 기록 추가")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 월 네비게이션
            item {
                MonthNavigationBar(
                    year = currentYear,
                    month = currentMonth,
                    onPreviousMonth = { viewModel.previousMonth() },
                    onNextMonth = { viewModel.nextMonth() }
                )
            }

            // 통계 카드
            item {
                WorkoutStatisticsCard(viewModel = viewModel)
            }

            // 루틴 색상 범례
            item {
                RoutineColorLegend(viewModel = viewModel)
            }

            // 캘린더 그리드
            item {
                CalendarGrid(
                    year = currentYear,
                    month = currentMonth,
                    viewModel = viewModel,
                    selectedDay = selectedDay,
                    onDayClick = { day ->
                        selectedDay = day
                        viewModel.selectDate(currentYear, currentMonth, day)
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }

            // 선택된 날짜의 기록
            if (selectedDay != null) {
                item {
                    Text(
                        text = "${currentMonth}월 ${selectedDay}일 운동 기록",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                if (selectedDateRecords.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "이 날은 운동 기록이 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(selectedDateRecords) { record ->
                        WorkoutRecordCard(
                            record = record,
                            onDeleteClick = { recordToDelete = record.id },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "날짜를 선택하면 운동 기록을 볼 수 있습니다",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // 운동 기록 추가 다이얼로그
    if (showAddDialog && selectedDay != null) {
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, currentMonth - 1, selectedDay!!, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val routines = (routinesState as? Resource.Success)?.data ?: emptyList()

        AddWorkoutRecordDialog(
            selectedDate = calendar.timeInMillis,
            routines = routines,
            onDismiss = { showAddDialog = false },
            onConfirm = { routineId, routineName, duration, notes, color ->
                viewModel.addWorkoutRecord(
                    date = calendar.timeInMillis,
                    routineId = routineId,
                    routineName = routineName,
                    duration = duration,
                    notes = notes
                )
                showAddDialog = false
                // 선택된 날짜 기록 새로고침
                viewModel.selectDate(currentYear, currentMonth, selectedDay!!)
            }
        )
    }

    // 삭제 확인 다이얼로그
    if (recordToDelete != null) {
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text("운동 기록 삭제") },
            text = { Text("이 운동 기록을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteWorkoutRecord(recordToDelete!!)
                        recordToDelete = null
                    }
                ) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun MonthNavigationBar(
    year: Int,
    month: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.KeyboardArrowLeft, "이전 달")
        }

        Text(
            text = "${year}년 ${month}월",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.KeyboardArrowRight, "다음 달")
        }
    }
}

@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    viewModel: CalendarViewModel,
    selectedDay: Int?,
    onDayClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        // 요일 헤더
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (day) {
                        "일" -> Color.Red
                        "토" -> Color.Blue
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 캘린더 날짜들
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0 = 일요일
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val weeks = mutableListOf<List<Int?>>()
        var currentWeek = mutableListOf<Int?>()

        // 첫날 이전의 빈 셀 추가
        repeat(firstDayOfWeek) {
            currentWeek.add(null)
        }

        // 실제 날짜 추가
        for (day in 1..daysInMonth) {
            currentWeek.add(day)
            if (currentWeek.size == 7) {
                weeks.add(currentWeek.toList())
                currentWeek = mutableListOf()
            }
        }

        // 남은 빈 셀 추가
        if (currentWeek.isNotEmpty()) {
            while (currentWeek.size < 7) {
                currentWeek.add(null)
            }
            weeks.add(currentWeek.toList())
        }

        // 주 렌더링
        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                week.forEachIndexed { index, day ->
                    DayCell(
                        day = day,
                        year = year,
                        month = month,
                        isSelected = day == selectedDay,
                        hasWorkout = day?.let { viewModel.hasWorkoutOnDate(year, month, it) } ?: false,
                        workoutCount = day?.let { viewModel.getWorkoutCountOnDate(year, month, it) } ?: 0,
                        routineColors = day?.let { viewModel.getRoutineColorsForDate(year, month, it) } ?: emptyList(),
                        isSunday = index == 0,
                        isSaturday = index == 6,
                        onDayClick = { day?.let { onDayClick(it) } }
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.DayCell(
    day: Int?,
    year: Int,
    month: Int,
    isSelected: Boolean,
    hasWorkout: Boolean,
    workoutCount: Int,
    routineColors: List<String>,
    isSunday: Boolean,
    isSaturday: Boolean,
    onDayClick: () -> Unit
) {
    val today = Calendar.getInstance()
    val isToday = day != null &&
            year == today.get(Calendar.YEAR) &&
            month == today.get(Calendar.MONTH) + 1 &&
            day == today.get(Calendar.DAY_OF_MONTH)

    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isToday -> MaterialTheme.colorScheme.secondaryContainer
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = day != null) { onDayClick() },
        contentAlignment = Alignment.Center
    ) {
        if (day != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isSunday -> Color.Red
                        isSaturday -> Color.Blue
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )

                if (routineColors.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        routineColors.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(parseColor(colorHex))
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 16진수 색상 문자열을 Color로 변환
 */
private fun parseColor(colorHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        Color(0xFF2196F3) // 기본 파란색
    }
}

@Composable
fun WorkoutRecordCard(
    record: WorkoutRecord,
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.routineName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (record.duration > 0) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Text(
                                text = "${record.duration}분",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "삭제",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (record.exercises.isNotEmpty()) {
                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    record.exercises.forEach { exercise ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${exercise.exerciseName} - ${exercise.completedSets.size}세트",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            if (record.notes.isNotEmpty()) {
                HorizontalDivider()
                Text(
                    text = record.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WorkoutStatisticsCard(viewModel: CalendarViewModel) {
    val weeklyCount = viewModel.getWeeklyWorkoutCount()
    val monthlyCount = viewModel.getMonthlyWorkoutCount()
    val streak = viewModel.getConsecutiveWorkoutDays()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticItem(
                label = "이번 주",
                value = weeklyCount.toString(),
                icon = Icons.Default.DateRange
            )

            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 8.dp)
            )

            StatisticItem(
                label = "이번 달",
                value = monthlyCount.toString(),
                icon = Icons.Default.Star
            )

            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 8.dp)
            )

            StatisticItem(
                label = "연속",
                value = "${streak}일",
                icon = Icons.Default.Favorite
            )
        }
    }
}

@Composable
fun StatisticItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun RoutineColorLegend(viewModel: CalendarViewModel) {
    val routinesState by viewModel.routinesState.collectAsState()
    val routines = (routinesState as? Resource.Success)?.data ?: emptyList()

    if (routines.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "루틴 색상 범례",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 플로우 레이아웃으로 루틴 색상 표시
            routines.forEach { routine ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 색상 표시기
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = MaterialTheme.shapes.small,
                        color = parseColor(routine.color)
                    ) {}

                    // 루틴 이름
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * CalendarScreen에 다이얼로그 추가
 * Scaffold 후에 CalendarScreen composable 내부에 추가되어야 함
 */
private fun getSelectedDate(year: Int, month: Int, day: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
