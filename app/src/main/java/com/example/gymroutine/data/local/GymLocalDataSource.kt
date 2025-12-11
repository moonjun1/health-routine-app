package com.example.gymroutine.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymroutine.data.model.Gym
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.gymDataStore: DataStore<Preferences> by preferencesDataStore(name = "gym_prefs")

// Local data source for gym data using DataStore
// Used when user is not logged in
@Singleton
class GymLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "GymLocalDataSource"
        private val GYMS_KEY = stringPreferencesKey("local_gyms")
        private val SELECTED_GYM_ID_KEY = stringPreferencesKey("selected_gym_id")
    }

// Save gym to local storage
    suspend fun saveGym(gym: Gym) {
        try {
            val currentGyms = getGyms().toMutableList()

            // Remove existing gym with same placeId
            currentGyms.removeAll { it.placeId == gym.placeId }

            // Add new/updated gym
            currentGyms.add(gym)

            context.gymDataStore.edit { prefs ->
                val gymsJson = gson.toJson(currentGyms)
                prefs[GYMS_KEY] = gymsJson
            }

            Log.d(TAG, "saveGym: Saved gym ${gym.name} locally")
        } catch (e: Exception) {
            Log.e(TAG, "saveGym: Failed to save gym", e)
            throw e
        }
    }

// Get all local gyms
    suspend fun getGyms(): List<Gym> {
        return try {
            val gymsJson = context.gymDataStore.data
                .map { prefs -> prefs[GYMS_KEY] }
                .first()

            if (gymsJson.isNullOrEmpty()) {
                Log.d(TAG, "getGyms: No local gyms found")
                emptyList()
            } else {
                val type = object : TypeToken<List<Gym>>() {}.type
                val gyms = gson.fromJson<List<Gym>>(gymsJson, type)
                Log.d(TAG, "getGyms: Found ${gyms.size} local gyms")
                gyms
            }
        } catch (e: Exception) {
            Log.e(TAG, "getGyms: Failed to load gyms", e)
            emptyList()
        }
    }

// Get gym by placeId
    suspend fun getGym(placeId: String): Gym? {
        return try {
            val gyms = getGyms()
            val gym = gyms.find { it.placeId == placeId }
            if (gym != null) {
                Log.d(TAG, "getGym: Found gym ${gym.name}")
            } else {
                Log.w(TAG, "getGym: Gym not found with placeId $placeId")
            }
            gym
        } catch (e: Exception) {
            Log.e(TAG, "getGym: Failed", e)
            null
        }
    }

// Delete gym
    suspend fun deleteGym(placeId: String) {
        try {
            val currentGyms = getGyms().toMutableList()
            currentGyms.removeAll { it.placeId == placeId }

            context.gymDataStore.edit { prefs ->
                val gymsJson = gson.toJson(currentGyms)
                prefs[GYMS_KEY] = gymsJson
            }

            Log.d(TAG, "deleteGym: Deleted gym with placeId $placeId")
        } catch (e: Exception) {
            Log.e(TAG, "deleteGym: Failed", e)
            throw e
        }
    }

// Set selected gym (for non-logged in users)
    suspend fun setSelectedGymId(placeId: String) {
        try {
            context.gymDataStore.edit { prefs ->
                prefs[SELECTED_GYM_ID_KEY] = placeId
            }
            Log.d(TAG, "setSelectedGymId: Set to $placeId")
        } catch (e: Exception) {
            Log.e(TAG, "setSelectedGymId: Failed", e)
        }
    }

// Get selected gym
    suspend fun getSelectedGym(): Gym? {
        return try {
            val selectedId = context.gymDataStore.data
                .map { prefs -> prefs[SELECTED_GYM_ID_KEY] }
                .first()

            if (selectedId.isNullOrEmpty()) {
                // Return first gym if no selection
                val gyms = getGyms()
                gyms.firstOrNull()
            } else {
                getGym(selectedId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getSelectedGym: Failed", e)
            null
        }
    }

// Clear all local gym data
    suspend fun clearAll() {
        try {
            context.gymDataStore.edit { prefs ->
                prefs.clear()
            }
            Log.d(TAG, "clearAll: Cleared all local gym data")
        } catch (e: Exception) {
            Log.e(TAG, "clearAll: Failed", e)
        }
    }
}
