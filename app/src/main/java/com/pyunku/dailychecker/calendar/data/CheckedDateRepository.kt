package com.pyunku.dailychecker.calendar.data

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckedDateRepository @Inject constructor(private val checkedDateDao: CheckedDateDao) {

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