package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

/**
 * 루틴 목록을 위한 빠른 운동 기록 다이얼로그
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickRecordDialog(
    routineName: String,
    onDismiss: () -> Unit,
    onConfirm: (date: Long, duration: Int, notes: String) -> Unit
) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("운동 기록하기")
                Text(
                    text = routineName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 날짜 선택
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "날짜 *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = dateFormat.format(Date(selectedDate)),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "날짜 선택",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // 운동 시간 입력
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

                // 메모 입력
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
                    onConfirm(
                        selectedDate,
                        duration.toIntOrNull() ?: 0,
                        notes
                    )
                }
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("기록하기")
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

    // 날짜 선택 다이얼로그
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = millis
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
