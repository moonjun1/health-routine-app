package com.example.gymroutine.presentation.gym

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.data.model.GymEquipment
import com.example.gymroutine.util.Resource

/**
 * Gym registration screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymRegisterScreen(
    gym: Gym,
    viewModel: GymRegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val registerState by viewModel.registerState.collectAsState()
    val selectedEquipments by viewModel.selectedEquipments.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val availableEquipments = remember { GymEquipment.getStandardEquipmentList() }

    // Set selected gym on init
    LaunchedEffect(gym) {
        viewModel.setSelectedGym(gym)
    }

    // Handle registration state
    LaunchedEffect(registerState) {
        when (registerState) {
            is Resource.Success -> {
                onRegistrationSuccess()
                viewModel.resetState()
            }
            is Resource.Error -> {
                errorMessage = (registerState as Resource.Error).message
                showError = true
            }
            else -> {
                showError = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("헬스장 등록") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Gym info card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "선택한 헬스장",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = gym.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = gym.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (gym.phone.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "전화: ${gym.phone}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Equipment selection card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "보유 기구 선택 (필수)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "이 헬스장에 있는 기구를 선택해주세요. AI 루틴 생성 시 선택한 기구만 사용됩니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Equipment selection list
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(availableEquipments) { equipment ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selectedEquipments.contains(equipment),
                                        onCheckedChange = { checked ->
                                            viewModel.toggleEquipment(equipment)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = equipment,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "선택된 기구: ${selectedEquipments.size}개",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selectedEquipments.isEmpty()) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error message
                if (showError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Register button
                Button(
                    onClick = viewModel::registerGym,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registerState !is Resource.Loading && selectedEquipments.isNotEmpty()
                ) {
                    if (registerState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            if (selectedEquipments.isEmpty()) {
                                "기구를 선택해주세요"
                            } else {
                                "이 헬스장으로 등록하기"
                            }
                        )
                    }
                }

                // Cancel button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registerState !is Resource.Loading
                ) {
                    Text("취소")
                }
            }
        }
    }
}
