package com.pyunku.dailychecker.calendar.presentation

import java.time.LocalDate

data class CalendarScreenState(
    val checkedDates: List<LocalDate>,
    val isLoading: Boolean,
    val error: String? = null
)