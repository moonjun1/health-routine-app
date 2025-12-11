package com.example.gymroutine.data.model

// Exercise data model
// Firestore collection: exercises/{exerciseId}
data class Exercise(
    val id: String = "",
    val name: String = "",
    val equipmentId: String = "",
    val category: String = "",
    val description: String = "",
    val tips: String = "",
    val youtubeUrl: String? = null
) {
    constructor() : this("", "", "", "", "", "", null)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "equipmentId" to equipmentId,
            "category" to category,
            "description" to description,
            "tips" to tips,
            "youtubeUrl" to youtubeUrl
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Exercise {
            return Exercise(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                equipmentId = map["equipmentId"] as? String ?: "",
                category = map["category"] as? String ?: "",
                description = map["description"] as? String ?: "",
                tips = map["tips"] as? String ?: "",
                youtubeUrl = map["youtubeUrl"] as? String
            )
        }
    }
}
