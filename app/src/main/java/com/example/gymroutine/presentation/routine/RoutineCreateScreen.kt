package com.example.gymroutine.presentation.routine

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Routine create screen (placeholder for Phase 5+)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineCreateScreen(
    onNavigateBack: () -> Unit,
    onRoutineCreated: () -> Unit
) {
    var routineName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = routineName,
                onValueChange = { routineName = it },
                label = { Text("루틴 이름") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("설명 (선택사항)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Text(
                "운동 선택 기능은 다음 업데이트에서 추가됩니다",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onRoutineCreated() },
                modifier = Modifier.fillMaxWidth(),
                enabled = routineName.isNotEmpty()
            ) {
                Text("루틴 생성")
            }
        }
    }
}
