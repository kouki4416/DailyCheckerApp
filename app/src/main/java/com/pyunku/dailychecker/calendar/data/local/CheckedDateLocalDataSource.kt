package com.pyunku.dailychecker.calendar.data.local

import com.pyunku.dailychecker.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckedDateLocalDataSource @Inject constructor(
    private val checkedDateDao: CheckedDateDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CheckedDateDataSource {

    override fun getCheckedDates(): Flow<List<CheckedDate>> {
        return checkedDateDao.findAll()
    }

    override suspend fun deleteCheckedDate(date: CheckedDate) = withContext(ioDispatcher) {
        checkedDateDao.delete(date)
    }

    override suspend fun addCheckedDate(date: CheckedDate) = withContext(ioDispatcher) {
        checkedDateDao.insert(date)
    }

    override fun getCheckedState(checkedDate: CheckedDate): Boolean {
        val resultList = checkedDateDao.find(checkedDate.dateString)
        return resultList.isNotEmpty()
    }
}