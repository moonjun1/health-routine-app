package com.example.gymroutine.presentation.gym

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gymroutine.data.model.Gym
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

/**
 * Kakao Map Composable
 * Displays Kakao Map with gym markers
 */
@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    currentLocation: Pair<Double, Double>?,
    gyms: List<Gym>,
    onGymMarkerClick: (Gym) -> Unit = {}
) {
    val context = LocalContext.current
    var isMapReady by remember { mutableStateOf(false) }

    val mapView = remember {
        try {
            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    if (mapView == null) {
        // Fallback UI if map initialization fails
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("지도를 불러올 수 없습니다")
        }
        return
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) { view ->
        if (!isMapReady) {
            try {
                view.start(object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        isMapReady = false
                    }

                    override fun onMapError(error: Exception?) {
                        error?.printStackTrace()
                        isMapReady = false
                    }
                }, object : KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        try {
                            isMapReady = true

                            // Move camera to current location or first gym
                            val centerLocation = if (currentLocation != null) {
                                LatLng.from(currentLocation.first, currentLocation.second)
                            } else if (gyms.isNotEmpty() && gyms[0].latitude != 0.0 && gyms[0].longitude != 0.0) {
                                LatLng.from(gyms[0].latitude, gyms[0].longitude)
                            } else {
                                LatLng.from(37.5665, 126.9780) // Default: Seoul
                            }

                            kakaoMap.moveCamera(
                                CameraUpdateFactory.newCenterPosition(centerLocation, 14)
                            )

                            val labelManager = kakaoMap.labelManager

                            // Add current location marker
                            if (currentLocation != null && labelManager != null) {
                                try {
                                    val currentLocationLatLng = LatLng.from(currentLocation.first, currentLocation.second)
                                    val currentLocationOptions = LabelOptions.from(currentLocationLatLng)
                                    labelManager.layer?.addLabel(currentLocationOptions)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            // Add gym markers
                            if (labelManager != null) {
                                gyms.forEach { gym ->
                                    try {
                                        if (gym.latitude != 0.0 && gym.longitude != 0.0) {
                                            val position = LatLng.from(gym.latitude, gym.longitude)
                                            val options = LabelOptions.from(position)
                                            labelManager.layer?.addLabel(options)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                mapView.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
