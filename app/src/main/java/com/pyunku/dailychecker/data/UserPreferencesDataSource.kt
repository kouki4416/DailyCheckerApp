package com.pyunku.dailychecker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pyunku.dailychecker.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

enum class CheckShape(val resId: Int) {
    CIRCLE(R.drawable.ic_circle),
    CHECK(R.drawable.ic_check_24);
}

data class UserPreferences(
    val checkShape: CheckShape,
)

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object PreferencesKeys {
        val CHECK_SHAPE = stringPreferencesKey("checkShape")
    }

    // Get user preferences flow
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun setCheckShape(checkShape: CheckShape) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHECK_SHAPE] = checkShape.name
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val checkShape = CheckShape.valueOf(
            preferences[PreferencesKeys.CHECK_SHAPE] ?: CheckShape.CIRCLE.name
        )
        return UserPreferences(checkShape = checkShape)
    }

}