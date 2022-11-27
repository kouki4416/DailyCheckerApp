package com.pyunku.dailychecker.calendar.data

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import kotlinx.coroutines.flow.Flow

interface CheckedDateRepository {
    fun getCheckedDates(): Flow<List<CheckedDate>>
    suspend fun deleteCheckedDate(date: CheckedDate)
    suspend fun addCheckedDate(date: CheckedDate)
}