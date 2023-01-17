package com.pyunku.dailychecker.calendar.presentation

import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCheckedDateDao: CheckedDateDao {
    private var checkedDates = HashMap<String, CheckedDate>()
    override suspend fun insert(date: CheckedDate) {
        checkedDates[date.dateString] = date
    }

    override fun update(date: CheckedDate) {
        updateCheckedDate(date)
    }

    override suspend fun delete(date: CheckedDate) {
        checkedDates.remove(date.dateString)
    }

    override fun findAll(): Flow<List<CheckedDate>> {
        return flow{
            emit(
                checkedDates.toList().map {
                    it.second
                }
            )
        }
    }

    override fun find(primaryKey: String): List<CheckedDate> {
        TODO("Not yet implemented")
    }

    private fun updateCheckedDate(
        checkedDate: CheckedDate
    ){
        val date = this.checkedDates[checkedDate.dateString]
        if(date != null){
            this.checkedDates[checkedDate.dateString] = checkedDate
        }
    }

}