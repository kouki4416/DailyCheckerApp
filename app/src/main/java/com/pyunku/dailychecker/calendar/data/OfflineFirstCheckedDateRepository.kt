package com.pyunku.dailychecker.calendar.data

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstCheckedDateRepository @Inject constructor(
    private val checkedDateLocalDataSource: CheckedDateDataSource,
) : CheckedDateRepository {
    override fun getCheckedDates(): Flow<List<CheckedDate>> {
        return checkedDateLocalDataSource.getCheckedDates()
    }

    override suspend fun deleteCheckedDate(date: CheckedDate){
        checkedDateLocalDataSource.deleteCheckedDate(date)
    }

    override suspend fun addCheckedDate(date: CheckedDate) {
        checkedDateLocalDataSource.addCheckedDate(date)
    }

}