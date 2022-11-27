package com.pyunku.dailychecker.calendar.presentation

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCheckedDateDataSource:CheckedDateDataSource {
    private var checkedDates = HashMap<String, CheckedDate>()
    override fun getCheckedDates(): Flow<List<CheckedDate>> {
        return flow{
            emit(
                checkedDates.toList().map {
                    it.second
                }
            )
        }
    }

    override suspend fun deleteCheckedDate(date: CheckedDate) {
        checkedDates.remove(date.dateString)
    }

    override suspend fun addCheckedDate(date: CheckedDate) {
        checkedDates[date.dateString] = date
    }
}