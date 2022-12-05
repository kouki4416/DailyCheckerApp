package com.pyunku.dailychecker.calendar.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckedDateDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(date: CheckedDate)

    @Update
    fun update(date: CheckedDate)

    @Delete
    suspend fun delete(date: CheckedDate)

    @Query("SELECT * FROM date")
    fun findAll(): Flow<List<CheckedDate>>

    @Query("select * from date where dateString=:primaryKey")
    fun find(primaryKey: String): List<CheckedDate>
}