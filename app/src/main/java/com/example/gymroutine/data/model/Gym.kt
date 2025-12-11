package com.example.gymroutine.data.model

// 헬스장 데이터 모델
// Firestore 컬렉션: gyms/{placeId}
data class Gym(
    val placeId: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phone: String = "",
    val registeredBy: String = "",
    val hours: Map<String, Hours> = emptyMap(),
    val equipments: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", 0.0, 0.0, "", "", emptyMap(), emptyList(), 0L)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "placeId" to placeId,
            "name" to name,
            "address" to address,
            "latitude" to latitude,
            "longitude" to longitude,
            "phone" to phone,
            "registeredBy" to registeredBy,
            "hours" to hours.mapValues { it.value.toMap() },
            "equipments" to equipments,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Gym {
            val hoursMap = (map["hours"] as? Map<String, Map<String, Any?>>)?.mapValues {
                Hours.fromMap(it.value)
            } ?: emptyMap()

            return Gym(
                placeId = map["placeId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                address = map["address"] as? String ?: "",
                latitude = (map["latitude"] as? Number)?.toDouble() ?: 0.0,
                longitude = (map["longitude"] as? Number)?.toDouble() ?: 0.0,
                phone = map["phone"] as? String ?: "",
                registeredBy = map["registeredBy"] as? String ?: "",
                hours = hoursMap,
                equipments = (map["equipments"] as? List<String>) ?: emptyList(),
                createdAt = map["createdAt"] as? Long ?: 0L
            )
        }
    }
}

// 각 요일별 운영 시간
data class Hours(
    val open: String = "00:00",
    val close: String = "23:59"
) {
    constructor() : this("00:00", "23:59")

    fun toMap(): Map<String, String> {
        return mapOf(
            "open" to open,
            "close" to close
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Hours {
            return Hours(
                open = map["open"] as? String ?: "00:00",
                close = map["close"] as? String ?: "23:59"
            )
        }
    }
}
