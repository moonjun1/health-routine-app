package com.example.gymroutine.data.model

// Equipment data model
// Firestore collection: equipments/{equipmentId}
data class Equipment(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val targetMuscle: String = "",
    val description: String = ""
) {
    constructor() : this("", "", "", "", "")

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "category" to category,
            "targetMuscle" to targetMuscle,
            "description" to description
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Equipment {
            return Equipment(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                category = map["category"] as? String ?: "",
                targetMuscle = map["targetMuscle"] as? String ?: "",
                description = map["description"] as? String ?: ""
            )
        }
    }
}
