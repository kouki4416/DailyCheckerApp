package com.pyunku.dailychecker.calendar.presentation

import com.pyunku.dailychecker.common.data.CheckShape
import java.time.LocalDate

data class CalendarScreenState(
    var checkedDates: List<LocalDate>,
    var checkedDateNum: Int = 0,
    var isLoading: Boolean,
    var error: String? = null,
    var checkShape: CheckShape = CheckShape.CIRCLE
)