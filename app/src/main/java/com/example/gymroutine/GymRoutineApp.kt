package com.example.gymroutine

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Hilt dependency injection
 */
@HiltAndroidApp
class GymRoutineApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Kakao Map SDK
        KakaoMapSdk.init(this, BuildConfig.KAKAO_REST_API_KEY)
    }
}
