package com.example.gymroutine.data.remote

import com.example.gymroutine.data.model.*
import com.example.gymroutine.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Firestore 데이터 소스
// 모든 Firestore 데이터베이스 작업 처리
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // ==================== User Operations ====================

// Firestore에 사용자 문서 생성
    suspend fun createUser(user: User) {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(user.id)
            .set(user.toMap())
            .await()
    }

// ID로 사용자 조회
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

// 사용자의 헬스장 ID 업데이트
    suspend fun updateUserGym(userId: String, gymId: String) {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .update("gymId", gymId)
            .await()
    }

    // ==================== Gym Operations ====================

// 헬스장 문서 생성
    suspend fun createGym(gym: Gym) {
        val gymMap = gym.toMap()
        android.util.Log.d("FirestoreDataSource", "createGym: Saving gym ${gym.name} (${gym.placeId})")
        android.util.Log.d("FirestoreDataSource", "  registeredBy=${gymMap["registeredBy"]}")
        android.util.Log.d("FirestoreDataSource", "  equipments=${gymMap["equipments"]}")

        firestore.collection(Constants.COLLECTION_GYMS)
            .document(gym.placeId)
            .set(gymMap)
            .await()

        android.util.Log.d("FirestoreDataSource", "createGym: Saved successfully")
    }

// placeID로 헬스장 조회
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

// 헬스장 정보 업데이트
    suspend fun updateGym(gym: Gym) {
        firestore.collection(Constants.COLLECTION_GYMS)
            .document(gym.placeId)
            .set(gym.toMap())
            .await()
    }

// ID로 헬스장 삭제
    suspend fun deleteGym(placeId: String) {
        firestore.collection(Constants.COLLECTION_GYMS)
            .document(placeId)
            .delete()
            .await()
    }

// 모든 헬스장 조회
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

// Get user's gym by userId (첫 번째 헬스장)
    suspend fun getUserGym(userId: String): Gym? {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_GYMS)
                .whereEqualTo("registeredBy", userId)
                .limit(1)
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                Gym.fromMap(snapshot.documents[0].data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

// 사용자가 등록한 모든 헬스장 조회
    suspend fun getUserGyms(userId: String): List<Gym> {
        return try {
            android.util.Log.d("FirestoreDataSource", "getUserGyms: Querying for userId=$userId")
            val snapshot = firestore.collection(Constants.COLLECTION_GYMS)
                .whereEqualTo("registeredBy", userId)
                .get()
                .await()

            android.util.Log.d("FirestoreDataSource", "getUserGyms: Found ${snapshot.documents.size} documents")
            snapshot.documents.forEachIndexed { index, doc ->
                android.util.Log.d("FirestoreDataSource", "  Doc $index: ${doc.id}, registeredBy=${doc.data?.get("registeredBy")}")
            }

            val gyms = snapshot.documents.mapNotNull { doc ->
                Gym.fromMap(doc.data ?: emptyMap())
            }
            android.util.Log.d("FirestoreDataSource", "getUserGyms: Parsed ${gyms.size} gyms")
            gyms
        } catch (e: Exception) {
            android.util.Log.e("FirestoreDataSource", "getUserGyms: Failed", e)
            emptyList()
        }
    }

// 사용자의 내 헬스장 설정
    suspend fun setMyGym(userId: String, gymId: String) {
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .update("myGymId", gymId)
            .await()
    }

    // ==================== Equipment Operations ====================

// 모든 기구 조회
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

// ID로 기구 조회
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

// 모든 운동 조회
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

// 기구 ID로 운동 조회
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

// 카테고리별 운동 조회
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

// 루틴 생성
    suspend fun createRoutine(userId: String, routine: Routine) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routine.id)
            .set(routine.toMap())
            .await()
    }

// 사용자의 루틴 조회
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

// ID로 루틴 조회
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

// 루틴 업데이트
    suspend fun updateRoutine(userId: String, routine: Routine) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routine.id)
            .set(routine.toMap())
            .await()
    }

// 루틴 삭제
    suspend fun deleteRoutine(userId: String, routineId: String) {
        firestore.collection(Constants.COLLECTION_ROUTINES)
            .document(userId)
            .collection("user_routines")
            .document(routineId)
            .delete()
            .await()
    }

    // ==================== Workout Record Operations ====================

// 운동 기록 생성
    suspend fun createWorkoutRecord(userId: String, record: WorkoutRecord) {
        firestore.collection(Constants.COLLECTION_WORKOUT_RECORDS)
            .document(userId)
            .collection("records")
            .document(record.id)
            .set(record.toMap())
            .await()
    }

// 사용자의 운동 기록 조회
    suspend fun getUserWorkoutRecords(userId: String): List<WorkoutRecord> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_WORKOUT_RECORDS)
                .document(userId)
                .collection("records")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                WorkoutRecord.fromMap(doc.data ?: emptyMap())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

// ID로 운동 기록 조회
    suspend fun getWorkoutRecord(userId: String, recordId: String): WorkoutRecord? {
        return try {
            val document = firestore.collection(Constants.COLLECTION_WORKOUT_RECORDS)
                .document(userId)
                .collection("records")
                .document(recordId)
                .get()
                .await()

            if (document.exists()) {
                WorkoutRecord.fromMap(document.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

// 운동 기록 업데이트
    suspend fun updateWorkoutRecord(userId: String, record: WorkoutRecord) {
        firestore.collection(Constants.COLLECTION_WORKOUT_RECORDS)
            .document(userId)
            .collection("records")
            .document(record.id)
            .set(record.toMap())
            .await()
    }

// 운동 기록 삭제
    suspend fun deleteWorkoutRecord(userId: String, recordId: String) {
        firestore.collection(Constants.COLLECTION_WORKOUT_RECORDS)
            .document(userId)
            .collection("records")
            .document(recordId)
            .delete()
            .await()
    }
}
