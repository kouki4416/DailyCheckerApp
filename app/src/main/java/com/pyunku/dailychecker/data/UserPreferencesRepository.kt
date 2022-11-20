package com.pyunku.dailychecker.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) {
    val userDataStream: Flow<UserPreferences> = userPreferencesDataSource.userPreferencesFlow

    suspend fun setCheckShape(checkShape: CheckShape) =
        userPreferencesDataSource.setCheckShape(checkShape)

    suspend fun setShownFirstAppReview() = userPreferencesDataSource.setShownFirstAppReview()
    suspend fun setShownSecondAppReview() = userPreferencesDataSource.setShownSecondAppReview()
    suspend fun setShownThirdAppReview() = userPreferencesDataSource.setShownThirdAppReview()
    suspend fun setIsDarkMode(isDarkMode: Boolean) = userPreferencesDataSource.setIsDarkMode(isDarkMode)
    suspend fun updateCurrentTask(task: String) = userPreferencesDataSource.updateCurrentTask(task)

}