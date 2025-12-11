package com.example.gymroutine

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

// Hilt 의존성 주입을 위한 Application 클래스
@HiltAndroidApp
class GymRoutineApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오맵 SDK 초기화
        // NOTE: x86_64 에뮬레이터에서는 크래시 발생 (libK3fAndroid.so 미지원)
        // 실제 기기나 ARM 에뮬레이터에서만 활성화하세요
        // KakaoMapSdk.init(this, BuildConfig.KAKAO_REST_API_KEY)
    }
}

