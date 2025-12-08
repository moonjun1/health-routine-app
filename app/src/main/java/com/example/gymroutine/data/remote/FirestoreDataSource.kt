package com.example.gymroutine.data.remote

import com.example.gymroutine.data.model.*
import com.example.gymroutine.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firestore data source
 * Handles all Firestore database operations
 */
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // ==================== User Operations ====================

    /**
     * Create user document in Firestore
     */
    suspend fun createUser(user: User) {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(user.id)
            .set(user.toMap())
            .await()
    }

    /**
     * Get user by ID
     */
    suspend fun getUser(userId: String): User? {
        return try {
            val document = firestore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                User.fromMap(document.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Update user's gym ID
     */
    suspend fun updateUserGym(userId: String, gymId: String) {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .update("gymId", gymId)
            .await()
    }

    // ==================== Gym Operations ====================

    /**
     * Create gym document
     */
    suspend fun createGym(gym: Gym) {
        firestore.collection(Constants.COLLECTION_GYMS)
            .document(gym.placeId)
            .set(gym.toMap())
            .await()
    }

    /**
     * Get gym by place ID
     */
    suspend fun getGym(placeId: String): Gym? {
        return try {
            val document = firestore.collection(Constants.COLLECTION_GYMS)
                .document(placeId)
                .get()
                .await()

            if (document.exists()) {
                Gym.fromMap(document.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Update gym information
     */
    suspend fun updateGym(gym: Gym) {
        firestore.collection(Constants.COLLECTION_GYMS)
            .document(gym.placeId)
            .set(gym.toMap())
            .await()
    }

    /**
     * Delete gym by ID
     */
    suspend fun deleteGym(placeId: String) {
        firestore.collection(Constants.COLLECTION_GYMS)
            .document(placeId)
            .delete()
            .await()
    }

    /**
     * Get all gyms
     */
    suspend fun getGyms(): List<Gym> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_GYMS)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Gym.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ==================== Equipment Operations ====================

    /**
     * Get all equipments
     */
    suspend fun getAllEquipments(): List<Equipment> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_EQUIPMENTS)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Equipment.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get equipment by ID
     */
    suspend fun getEquipment(equipmentId: String): Equipment? {
        return try {
            val document = firestore.collection(Constants.COLLECTION_EQUIPMENTS)
                .document(equipmentId)
                .get()
                .await()

            if (document.exists()) {
                Equipment.fromMap(document.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // ==================== Exercise Operations ====================

    /**
     * Get all exercises
     */
    suspend fun getAllExercises(): List<Exercise> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_EXERCISES)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Exercise.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get exercises by equipment ID
     */
    suspend fun getExercisesByEquipment(equipmentId: String): List<Exercise> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_EXERCISES)
                .whereEqualTo("equipmentId", equipmentId)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Exercise.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get exercises by category
     */
    suspend fun getExercisesByCategory(category: String): List<Exercise> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_EXERCISES)
                .whereEqualTo("category", category)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Exercise.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ==================== Routine Operations ====================

    /**
     * Create routine
     */
    suspend fun createRoutine(userId: String, routine: Routine) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routine.id)
            .set(routine.toMap())
            .await()
    }

    /**
     * Get user's routines
     */
    suspend fun getUserRoutines(userId: String): List<Routine> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_ROUTINES)
                .document(userId)
                .collection("user_routines")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                Routine.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get routine by ID
     */
    suspend fun getRoutine(userId: String, routineId: String): Routine? {
        return try {
            val document = firestore.collection(Constants.COLLECTION_ROUTINES)
                .document(userId)
                .collection("user_routines")
                .document(routineId)
                .get()
                .await()

            if (document.exists()) {
                Routine.fromMap(document.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Update routine
     */
    suspend fun updateRoutine(userId: String, routine: Routine) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routine.id)
            .set(routine.toMap())
            .await()
    }

    /**
     * Delete routine
     */
    suspend fun deleteRoutine(userId: String, routineId: String) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routineId)
            .delete()
            .await()
    }
}
