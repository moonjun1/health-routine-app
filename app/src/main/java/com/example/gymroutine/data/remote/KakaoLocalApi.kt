package com.example.gymroutine.data.remote

import com.example.gymroutine.data.remote.dto.KakaoLocalSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Kakao Local API 서비스 인터페이스
interface KakaoLocalApi {

// 키워드로 장소 검색
// @param authorization KakaoAK {REST_API_KEY}
// @param query 검색 키워드 (예: "헬스장", "피트니스")
// @param x 경도 (선택사항)
// @param y 위도 (선택사항)
// @param radius 검색 반경(미터) (0-20000, 기본값: 5000)
// @param page 페이지 번호 (1-45, 기본값: 1)
// @param size 페이지당 결과 수 (1-15, 기본값: 15)
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("x") x: String? = null,
        @Query("y") y: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): KakaoLocalSearchResponse

// 카테고리로 장소 검색
// @param authorization KakaoAK {REST_API_KEY}
// @param categoryGroupCode 카테고리 코드 (예: "FD6"은 음식점, "CE7"은 카페)
// @param x 경도
// @param y 위도
// @param radius 검색 반경(미터) (0-20000)
// @param page 페이지 번호 (1-45, 기본값: 1)
// @param size 페이지당 결과 수 (1-15, 기본값: 15)
    @GET("v2/local/search/category.json")
    suspend fun searchByCategory(
        @Header("Authorization") authorization: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int = 5000,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15
    ): KakaoLocalSearchResponse
}
