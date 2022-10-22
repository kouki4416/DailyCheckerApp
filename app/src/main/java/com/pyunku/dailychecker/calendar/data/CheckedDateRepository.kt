package com.pyunku.dailychecker.calendar.data

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDao

class CheckedDateRepository(private val checkedDateDao: CheckedDateDao) {

    suspend fun getCheckedDates(): List<CheckedDate>{
        return checkedDateDao.findAll()
    }

    suspend fun deleteCheckedDate(date: CheckedDate){
        checkedDateDao.delete(date)
    }

    suspend fun addCheckedDate(date: CheckedDate) {
        checkedDateDao.insert(date)
    }
}