package com.pyunku.dailychecker.calendar.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckedDateLocalDataSource @Inject constructor(private val checkedDateDao: CheckedDateDao) {

    fun getCheckedDates(): Flow<List<CheckedDate>>{
        return checkedDateDao.findAll()
    }

    suspend fun deleteCheckedDate(date: CheckedDate){
        checkedDateDao.delete(date)
    }

    suspend fun addCheckedDate(date: CheckedDate) {
        checkedDateDao.insert(date)
    }
}