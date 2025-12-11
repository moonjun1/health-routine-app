package com.example.gymroutine.presentation.gym

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymroutine.data.model.Gym
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// 구글맵 컴포저블 - 헬스장 마커가 표시된 구글맵
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    currentLocation: Pair<Double, Double>?,
    gyms: List<Gym>,
    onGymMarkerClick: (Gym) -> Unit = {}
) {
    var isError by remember { mutableStateOf(false) }

    // 카메라 위치 상태
    val cameraPositionState = rememberCameraPositionState()

    // 초기 카메라 위치 설정
    LaunchedEffect(currentLocation, gyms) {
        try {
            val centerLocation = when {
                currentLocation != null -> {
                    LatLng(currentLocation.first, currentLocation.second)
                }
                gyms.isNotEmpty() && gyms[0].latitude != 0.0 && gyms[0].longitude != 0.0 -> {
                    LatLng(gyms[0].latitude, gyms[0].longitude)
                }
                else -> {
                    LatLng(37.5665, 126.9780) // Default: Seoul
                }
            }

            cameraPositionState.position = CameraPosition.fromLatLngZoom(centerLocation, 14f)
        } catch (e: Exception) {
            e.printStackTrace()
            isError = true
        }
    }

    if (isError) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("지도를 불러올 수 없습니다")
        }
        return
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // 현재 위치 마커 (파란색 마커)
        currentLocation?.let { location ->
            Marker(
                state = MarkerState(position = LatLng(location.first, location.second)),
                title = "현재 위치",
                snippet = "내 위치",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }

        // 헬스장 마커 (빨간색 마커)
        gyms.forEach { gym ->
            if (gym.latitude != 0.0 && gym.longitude != 0.0) {
                Marker(
                    state = MarkerState(position = LatLng(gym.latitude, gym.longitude)),
                    title = gym.name,
                    snippet = gym.address,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                    onClick = {
                        onGymMarkerClick(gym)
                        true
                    }
                )
            }
        }
    }
}
