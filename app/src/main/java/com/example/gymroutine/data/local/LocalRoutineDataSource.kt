package com.example.gymroutine.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.gymroutine.data.model.Routine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local storage for routines using SharedPreferences
 * Used when user is not logged in
 */
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

    /**
     * Get all routines from local storage
     */
    fun getAllRoutines(): List<Routine> {
        val json = prefs.getString(KEY_ROUTINES, null) ?: return emptyList()
        val type = object : TypeToken<List<Routine>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Save routine to local storage
     */
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

    /**
     * Delete routine from local storage
     */
    fun deleteRoutine(routineId: String) {
        val routines = getAllRoutines().filter { it.id != routineId }
        saveAll(routines)
    }

    /**
     * Get routine by id
     */
    fun getRoutineById(routineId: String): Routine? {
        return getAllRoutines().find { it.id == routineId }
    }

    /**
     * Save all routines
     */
    private fun saveAll(routines: List<Routine>) {
        val json = gson.toJson(routines)
        prefs.edit().putString(KEY_ROUTINES, json).apply()
    }

    /**
     * Clear all routines
     */
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
