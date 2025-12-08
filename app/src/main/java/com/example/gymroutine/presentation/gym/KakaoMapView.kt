package com.example.gymroutine.presentation.gym

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
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

    val mapView = remember {
        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) { view ->
        view.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // Map destroyed
            }

            override fun onMapError(error: Exception?) {
                // Handle error
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // Move camera to current location or first gym
                val centerLocation = if (currentLocation != null) {
                    LatLng.from(currentLocation.first, currentLocation.second)
                } else if (gyms.isNotEmpty()) {
                    LatLng.from(gyms[0].latitude, gyms[0].longitude)
                } else {
                    LatLng.from(37.5665, 126.9780) // Default: Seoul
                }

                kakaoMap.moveCamera(
                    CameraUpdateFactory.newCenterPosition(centerLocation, 15)
                )

                val labelManager = kakaoMap.labelManager

                // Add current location marker (blue)
                if (currentLocation != null) {
                    val currentLocationLatLng = LatLng.from(currentLocation.first, currentLocation.second)
                    val currentLocationOptions = LabelOptions.from(currentLocationLatLng)

                    labelManager?.layer?.addLabel(currentLocationOptions)
                }

                // Add gym markers (red)
                gyms.forEach { gym ->
                    val position = LatLng.from(gym.latitude, gym.longitude)
                    val options = LabelOptions.from(position)

                    labelManager?.layer?.addLabel(options)
                }
            }
        })
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.finish()
        }
    }
}
