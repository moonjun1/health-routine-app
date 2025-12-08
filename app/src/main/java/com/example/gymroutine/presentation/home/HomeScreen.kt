package com.example.gymroutine.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Home screen (placeholder)
 */
@Composable
fun HomeScreen(
    onNavigateToGymSearch: () -> Unit = {},
    onNavigateToExerciseList: () -> Unit = {},
    onNavigateToRoutineList: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "헬스 루틴 앱",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "로그인 성공!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToGymSearch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("헬스장 검색")
            }

            Button(
                onClick = onNavigateToExerciseList,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("운동 목록")
            }

            Button(
                onClick = onNavigateToRoutineList,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("내 루틴")
            }

            Text(
                text = "Phase 4 완료!\n다음 단계: 루틴 생성 및 관리 구현",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}
