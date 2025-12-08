package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Dialog for editing exercise settings (sets, reps, weight, rest time)
 */
@Composable
fun ExerciseSettingsDialog(
    selectedExercise: SelectedExercise,
    onDismiss: () -> Unit,
    onSave: (sets: Int, reps: Int, weight: Double, restTime: Int) -> Unit
) {
    var sets by remember { mutableStateOf(selectedExercise.sets.toString()) }
    var reps by remember { mutableStateOf(selectedExercise.reps.toString()) }
    var weight by remember { mutableStateOf(selectedExercise.weight.toString()) }
    var restTime by remember { mutableStateOf(selectedExercise.restTime.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "운동 설정",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedExercise.exercise.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sets
                OutlinedTextField(
                    value = sets,
                    onValueChange = { sets = it },
                    label = { Text("세트 수") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("세트") }
                )

                // Reps
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("반복 횟수") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("회") }
                )

                // Weight
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("무게 (선택사항)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("kg") },
                    placeholder = { Text("0") }
                )

                // Rest time
                OutlinedTextField(
                    value = restTime,
                    onValueChange = { restTime = it },
                    label = { Text("휴식 시간") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("초") }
                )

                // Quick rest time buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickRestButton("30초", 30) { restTime = "30" }
                    QuickRestButton("60초", 60) { restTime = "60" }
                    QuickRestButton("90초", 90) { restTime = "90" }
                    QuickRestButton("120초", 120) { restTime = "120" }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val setsInt = sets.toIntOrNull() ?: selectedExercise.sets
                    val repsInt = reps.toIntOrNull() ?: selectedExercise.reps
                    val weightDouble = weight.toDoubleOrNull() ?: selectedExercise.weight
                    val restTimeInt = restTime.toIntOrNull() ?: selectedExercise.restTime

                    onSave(setsInt, repsInt, weightDouble, restTimeInt)
                }
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

/**
 * Quick rest time button
 */
@Composable
fun RowScope.QuickRestButton(
    label: String,
    value: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
