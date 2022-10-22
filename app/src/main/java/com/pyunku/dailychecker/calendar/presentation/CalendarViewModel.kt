package com.pyunku.dailychecker.calendar.presentation

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.CheckedDateRepository
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel(
    private val checkedDateRepository: CheckedDateRepository
) : ViewModel() {
    private val _state = mutableStateOf(
        CalendarScreenState(
            checkedDates = listOf(),
            isLoading = true
        )
    )

    val state: State<CalendarScreenState>
        get() = _state

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        getCheckedDate()
    }

    private fun getCheckedDate() {
        viewModelScope.launch(errorHandler) {
            val dates = getLocalCheckedDates()
            _state.value = _state.value.copy(
                checkedDates = dates,
                isLoading = false
            )
        }
    }

    private suspend fun getLocalCheckedDates(): List<LocalDate> {
        return withContext(Dispatchers.IO) {
            checkedDateRepository.getCheckedDates().map { checkedDate ->
                LocalDate.of(checkedDate.year, checkedDate.month, checkedDate.day)
            }
        }
    }

    fun playClickSound(context: Context){
        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.click_sound)
        mp.start()
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