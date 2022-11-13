package com.pyunku.dailychecker.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyunku.dailychecker.data.CheckShape
import com.pyunku.dailychecker.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel(){
    fun setCheckShape(checkShape: CheckShape){
        viewModelScope.launch{
            userPreferencesRepository.setCheckShape(checkShape)
        }
    }
}