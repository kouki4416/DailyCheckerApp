package com.pyunku.dailychecker.calendar.data

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckedDataRepository @Inject constructor(
    private val checkedDateLocalDataSource: CheckedDateLocalDataSource,
) {
    fun getCheckedDates(): Flow<List<CheckedDate>> {
        return checkedDateLocalDataSource.getCheckedDates()
    }

    suspend fun deleteCheckedDate(date: CheckedDate){
        checkedDateLocalDataSource.deleteCheckedDate(date)
    }

    suspend fun addCheckedDate(date: CheckedDate) {
        checkedDateLocalDataSource.addCheckedDate(date)
    }

}