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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel(
    private val checkedDateRepository: CheckedDateRepository
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
            checkedDateRepository.getCheckedDates().collect{
                _state.value = _state.value.copy(
                    checkedDates = it.map { checkedDate ->
                        LocalDate.of(checkedDate.year, checkedDate.month, checkedDate.day)
                    },
                    isLoading = false
                )
            }
        }
    }

//    private suspend fun getLocalCheckedDates(): List<LocalDate> {
//        return withContext(Dispatchers.IO) {
//            checkedDateRepository.getCheckedDates().map { checkedDate ->
//                LocalDate.of(checkedDate.year, checkedDate.month, checkedDate.day)
//            }
//        }
//    }

    private fun getCheckedDateNum(){

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