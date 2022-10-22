package com.pyunku.dailychecker.calendar.presentation

import java.time.LocalDate

data class CalendarScreenState(
    var checkedDates: List<LocalDate>,
    var checkedDateNum: Int = 0,
    var isLoading: Boolean,
    var error: String? = null
)