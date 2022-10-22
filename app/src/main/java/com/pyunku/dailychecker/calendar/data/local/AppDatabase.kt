package com.pyunku.dailychecker.calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CheckedDate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checkedDateDao() : CheckedDateDao
}