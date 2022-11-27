package com.pyunku.dailychecker.calendar.data.local

import kotlinx.coroutines.flow.Flow

interface CheckedDateDataSource {
    fun getCheckedDates(): Flow<List<CheckedDate>>

    suspend fun deleteCheckedDate(date: CheckedDate)

    suspend fun addCheckedDate(date: CheckedDate)
}