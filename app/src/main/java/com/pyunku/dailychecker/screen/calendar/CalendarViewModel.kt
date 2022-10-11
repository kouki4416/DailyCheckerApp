package com.pyunku.dailychecker.screen.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class CalendarViewModel: ViewModel() {
    private val selectionFlow = MutableStateFlow(emptyList<LocalDate>())


}