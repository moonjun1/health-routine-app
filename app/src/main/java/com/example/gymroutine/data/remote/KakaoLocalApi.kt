package com.example.gymroutine.data.remote

import com.example.gymroutine.data.remote.dto.KakaoLocalSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Kakao Local API service interface
 */
interface KakaoLocalApi {

    /**
     * Search places by keyword
     * @param authorization KakaoAK {REST_API_KEY}
     * @param query Search keyword (e.g., "헬스장", "피트니스")
     * @param x Longitude (optional)
     * @param y Latitude (optional)
     * @param radius Search radius in meters (0-20000, default: 5000)
     * @param page Page number (1-45, default: 1)
     * @param size Results per page (1-15, default: 15)
     */
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

    /**
     * Search places by category
     * @param authorization KakaoAK {REST_API_KEY}
     * @param categoryGroupCode Category code (e.g., "FD6" for restaurant, "CE7" for cafe)
     * @param x Longitude
     * @param y Latitude
     * @param radius Search radius in meters (0-20000)
     * @param page Page number (1-45, default: 1)
     * @param size Results per page (1-15, default: 15)
     */
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
