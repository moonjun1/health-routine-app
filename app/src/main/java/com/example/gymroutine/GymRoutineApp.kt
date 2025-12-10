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
        // NOTE: x86_64 에뮬레이터에서는 크래시 발생 (libK3fAndroid.so 미지원)
        // 실제 기기나 ARM 에뮬레이터에서만 활성화하세요
        // KakaoMapSdk.init(this, BuildConfig.KAKAO_REST_API_KEY)
    }
}
