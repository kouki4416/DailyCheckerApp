package com.pyunku.dailychecker.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyunku.dailychecker.calendar.data.CheckedDateRepository
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.data.CheckShape
import com.pyunku.dailychecker.data.UserPreferences
import com.pyunku.dailychecker.data.UserPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val checkedDateRepository: CheckedDateRepository,
    private val userPreferencesRepository: UserPreferencesDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(
        CalendarScreenState(
            checkedDates = listOf(),
            isLoading = true
        )
    )
    val state: StateFlow<CalendarScreenState>
        get() = _state

    val userPreferencesState: StateFlow<UserPreferences> =
        userPreferencesRepository.userPreferencesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences(CheckShape.NONE)
        )

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }


    init {
        getCheckedDate()
    }

    private fun getCheckedDate() {
        viewModelScope.launch(errorHandler) {
            checkedDateRepository.getCheckedDates().collect {
                _state.value = _state.value.copy(
                    checkedDates = it.map { checkedDate ->
                        LocalDate.of(checkedDate.year, checkedDate.month, checkedDate.day)
                    },
                    isLoading = false
                )
            }
        }
    }


    fun deleteCheckedDate(date: LocalDate) {
        val checkedDate = CheckedDate(
            dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth
        )
        CoroutineScope(Dispatchers.IO).launch {
            checkedDateRepository.deleteCheckedDate(checkedDate)
        }
    }

    fun addCheckedDate(date: LocalDate) {
        val checkedDate = CheckedDate(
            dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth
        )
        CoroutineScope(Dispatchers.IO).launch {
            checkedDateRepository.addCheckedDate(checkedDate)
        }
    }
}