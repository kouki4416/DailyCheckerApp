package com.pyunku.dailychecker.calendar.data.local

import androidx.room.*

@Dao
interface CheckedDateDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(date: CheckedDate)

    @Update
    fun update(date: CheckedDate)

    @Delete
    suspend fun delete(date: CheckedDate)

    @Query("SELECT * FROM date")
    suspend fun findAll(): List<CheckedDate>
}