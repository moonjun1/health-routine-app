package com.example.gymroutine.presentation.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gymroutine.data.model.Routine
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dialog for adding a workout record
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutRecordDialog(
    selectedDate: Long,
    routines: List<Routine>,
    onDismiss: () -> Unit,
    onConfirm: (routineId: String, routineName: String, duration: Int, notes: String, color: String) -> Unit
) {
    var selectedRoutine by remember { mutableStateOf<Routine?>(null) }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showRoutineSelector by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()) }
    val formattedDate = remember(selectedDate) { dateFormat.format(Date(selectedDate)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("운동 기록 추가")
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Routine selection
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "루틴 선택 *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRoutineSelector = true }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (selectedRoutine != null) {
                                Text(
                                    text = selectedRoutine!!.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            } else {
                                Text(
                                    text = "루틴을 선택해주세요",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Duration input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "운동 시간 (분)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it.filter { char -> char.isDigit() } },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("예: 60") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                // Notes input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "메모 (선택사항)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("오늘 운동 소감을 기록해보세요") },
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedRoutine?.let { routine ->
                        onConfirm(
                            routine.id,
                            routine.name,
                            duration.toIntOrNull() ?: 0,
                            notes,
                            routine.color
                        )
                    }
                },
                enabled = selectedRoutine != null
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("취소")
            }
        }
    )

    // Routine selector dialog
    if (showRoutineSelector) {
        AlertDialog(
            onDismissRequest = { showRoutineSelector = false },
            title = { Text("루틴 선택") },
            text = {
                if (routines.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "등록된 루틴이 없습니다",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(routines) { routine ->
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedRoutine = routine
                                        showRoutineSelector = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = routine.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${routine.exercises.size}개 운동",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (selectedRoutine?.id == routine.id) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoutineSelector = false }) {
                    Text("닫기")
                }
            }
        )
    }
}
