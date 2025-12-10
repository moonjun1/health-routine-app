package com.example.gymroutine.presentation.gym

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymroutine.data.model.Gym
import com.example.gymroutine.util.Resource

/**
 * Gym search screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymSearchScreen(
    viewModel: GymSearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onGymSelected: (Gym) -> Unit
) {
    val searchState by viewModel.searchState.collectAsState()
    val searchKeyword by viewModel.searchKeyword.collectAsState()
    val hasLocationPermission by viewModel.hasLocationPermission.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showMap by remember { mutableStateOf(true) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            viewModel.checkLocationPermission()
            viewModel.getCurrentLocation()
        }
    }

    // Request location on start
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            viewModel.getCurrentLocation()
        }
    }

    // Handle search state
    LaunchedEffect(searchState) {
        when (searchState) {
            is Resource.Error -> {
                errorMessage = (searchState as Resource.Error).message
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
                title = { Text("헬스장 검색") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showMap = !showMap }) {
                        Icon(
                            imageVector = if (showMap) Icons.Default.List else Icons.Default.Place,
                            contentDescription = if (showMap) "리스트 보기" else "지도 보기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = viewModel::onSearchKeywordChange,
                label = { Text("헬스장 이름 검색") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "검색")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.searchByKeyword() },
                        enabled = searchKeyword.isNotEmpty() && searchState !is Resource.Loading
                    ) {
                        Icon(Icons.Default.Search, "검색 실행")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nearby search button
            Button(
                onClick = { viewModel.searchNearbyGyms() },
                modifier = Modifier.fillMaxWidth(),
                enabled = currentLocation != null && searchState !is Resource.Loading
            ) {
                Icon(Icons.Default.LocationOn, "내 위치", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("내 주변 헬스장 찾기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Search results (Map or List)
            when (searchState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val gyms = (searchState as Resource.Success<List<Gym>>).data
                    if (gyms.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("검색 결과가 없습니다")
                        }
                    } else {
                        if (showMap) {
                            // Map view
                            KakaoMapView(
                                modifier = Modifier.fillMaxSize(),
                                currentLocation = currentLocation,
                                gyms = gyms,
                                onGymMarkerClick = { gym ->
                                    onGymSelected(gym)
                                }
                            )
                        } else {
                            // List view
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(gyms) { gym ->
                                    GymListItem(
                                        gym = gym,
                                        onClick = { onGymSelected(gym) }
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "검색어를 입력하거나\n내 주변 헬스장 찾기 버튼을 눌러주세요",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GymListItem(
    gym: Gym,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = gym.name,
                style = MaterialTheme.typography.titleMedium
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
}
