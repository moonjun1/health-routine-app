package com.example.gymroutine.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.gymroutine.data.model.Routine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// SharedPreferences를 사용하는 루틴 로컬 저장소
// 사용자가 로그인하지 않은 경우 사용됨
@Singleton
class LocalRoutineDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "local_routines",
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val KEY_ROUTINES = "routines"
    }

// 로컬 저장소에서 모든 루틴 조회
    fun getAllRoutines(): List<Routine> {
        val json = prefs.getString(KEY_ROUTINES, null) ?: return emptyList()
        val type = object : TypeToken<List<Routine>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

// 로컬 저장소에 루틴 저장
    fun saveRoutine(routine: Routine) {
        val routines = getAllRoutines().toMutableList()
        val index = routines.indexOfFirst { it.id == routine.id }

        if (index != -1) {
            routines[index] = routine
        } else {
            routines.add(routine)
        }

        saveAll(routines)
    }

// 로컬 저장소에서 루틴 삭제
    fun deleteRoutine(routineId: String) {
        val routines = getAllRoutines().filter { it.id != routineId }
        saveAll(routines)
    }

// id로 루틴 조회
    fun getRoutineById(routineId: String): Routine? {
        return getAllRoutines().find { it.id == routineId }
    }

// 모든 루틴 저장
    private fun saveAll(routines: List<Routine>) {
        val json = gson.toJson(routines)
        prefs.edit().putString(KEY_ROUTINES, json).apply()
    }

// 모든 루틴 삭제
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
