package com.pyunku.dailychecker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
    val checkShape: CheckShape = CheckShape.CIRCLE,
    val shownFirstAppReview: Boolean = false,
    val shownSecondAppReview: Boolean = false,
    val shownThirdAppReview: Boolean = false,
    val isDarkMode: Boolean = false,
    val currentTask: String = ""
)

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object PreferencesKeys {
        val CHECK_SHAPE = stringPreferencesKey("checkShape")
        val SHOWN_FIRST_APP_REVIEW = booleanPreferencesKey("shownFirstAppReview")
        val SHOWN_SECOND_APP_REVIEW = booleanPreferencesKey("shownSecondAppReview")
        val SHOWN_THIRD_APP_REVIEW = booleanPreferencesKey("shownThirdAppReview")
        val IS_DARK_MODE = booleanPreferencesKey("isDarkMode")
        val CURRENT_TASK = stringPreferencesKey("currentTask")
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

    suspend fun setShownFirstAppReview() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOWN_FIRST_APP_REVIEW] = true
        }
    }

    suspend fun setShownSecondAppReview() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOWN_SECOND_APP_REVIEW] = true
        }
    }

    suspend fun setShownThirdAppReview() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOWN_THIRD_APP_REVIEW] = true
        }
    }

    suspend fun setIsDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateCurrentTask(task: String){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_TASK] = task
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val checkShape = CheckShape.valueOf(
            preferences[PreferencesKeys.CHECK_SHAPE] ?: CheckShape.CIRCLE.name
        )
        val shownFirstAppReview = preferences[PreferencesKeys.SHOWN_FIRST_APP_REVIEW] ?: false
        val shownSecondAppReview = preferences[PreferencesKeys.SHOWN_SECOND_APP_REVIEW] ?: false
        val shownThirdAppReview = preferences[PreferencesKeys.SHOWN_THIRD_APP_REVIEW] ?: false
        val isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        val currentTask = preferences[PreferencesKeys.CURRENT_TASK] ?: ""
        return UserPreferences(
            checkShape = checkShape,
            shownFirstAppReview = shownFirstAppReview,
            shownSecondAppReview = shownSecondAppReview,
            shownThirdAppReview = shownThirdAppReview,
            isDarkMode = isDarkMode,
            currentTask = currentTask
        )
    }

}