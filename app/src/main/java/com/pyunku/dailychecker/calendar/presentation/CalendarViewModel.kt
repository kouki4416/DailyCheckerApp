package com.pyunku.dailychecker.calendar.presentation

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyunku.dailychecker.calendar.data.CheckedDateRepository
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val checkedDateRepository: CheckedDateRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(
        CalendarScreenState(
            checkedDates = listOf(),
            isLoading = true
        )
    )

    val state: StateFlow<CalendarScreenState>
        get() = _state

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