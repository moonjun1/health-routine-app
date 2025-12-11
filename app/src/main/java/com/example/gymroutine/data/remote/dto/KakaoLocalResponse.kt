package com.example.gymroutine.data.remote.dto

import com.google.gson.annotations.SerializedName

// Kakao Local API 응답 DTO

data class KakaoLocalSearchResponse(
    @SerializedName("documents")
    val documents: List<KakaoPlace>,
    @SerializedName("meta")
    val meta: KakaoMeta
)

data class KakaoPlace(
    @SerializedName("id")
    val id: String,
    @SerializedName("place_name")
    val placeName: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_group_code")
    val categoryGroupCode: String,
    @SerializedName("category_group_name")
    val categoryGroupName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("road_address_name")
    val roadAddressName: String,
    @SerializedName("x")
    val longitude: String,
    @SerializedName("y")
    val latitude: String,
    @SerializedName("place_url")
    val placeUrl: String,
    @SerializedName("distance")
    val distance: String
)

data class KakaoMeta(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("same_name")
    val sameName: KakaoRegionInfo?
)

data class KakaoRegionInfo(
    @SerializedName("region")
    val region: List<String>,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("selected_region")
    val selectedRegion: String
)
