package com.pyunku.dailychecker.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyunku.dailychecker.data.CheckShape
import com.pyunku.dailychecker.data.UserPreferences
import com.pyunku.dailychecker.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val userPreferencesState: StateFlow<UserPreferences> =
        userPreferencesRepository.userDataStream.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences(
                checkShape = CheckShape.CIRCLE,
                shownFirstAppReview = false,
                shownSecondAppReview = false,
                shownThirdAppReview = false
            )
        )

    fun setCheckShape(checkShape: CheckShape) {
        viewModelScope.launch {
            userPreferencesRepository.setCheckShape(checkShape)
        }
    }

    fun setIsDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setIsDarkMode(isDarkMode)
        }
    }
}