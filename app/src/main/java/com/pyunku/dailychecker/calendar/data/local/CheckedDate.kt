package com.pyunku.dailychecker.calendar.data.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date")
data class CheckedDate(
    @PrimaryKey var dateString: String = "01/01/2000",
    @NonNull @ColumnInfo(name = "year") var year: Int = 2000,
    @NonNull @ColumnInfo(name = "month") var month: Int = 1,
    @NonNull @ColumnInfo(name = "day") var day: Int = 1
)
