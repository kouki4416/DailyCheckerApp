package com.pyunku.dailychecker.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.pyunku.dailychecker.calendar.data.local.AppDatabase
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalCoroutinesApi
// class using AndroidX Test
@RunWith(AndroidJUnit4::class)
// annotation for small run time
@SmallTest
class CheckedDataDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        // Using in memory database, data disappears when process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    // MethodName_StateUnderTest_ExpectedBehavior
    @Test
    fun insertAndFindAll_FirstElement_Match() = runTest {
        // Arrange
        val date = LocalDate.of(2000, 1, 1)
        val dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val checkedDate = CheckedDate(
            dateString = dateString,
            year = year,
            month = month,
            day = day
        )
        database.checkedDateDao().insert(checkedDate)
        // Act
        val flow = database.checkedDateDao().findAll()
        val elements = flow.take(1).toList()
        val list = elements[0]
        // Assert
        assertThat(list[0], notNullValue())
        assertThat(list[0].dateString, `is`(dateString))
        assertThat(list[0].year, `is`(year))
        assertThat(list[0].month, `is`(month))
        assertThat(list[0].day, `is`(day))
    }

    @Test
    fun insertAndDelete_List_Empty() = runTest {
        // Arrange
        val date = LocalDate.of(2000, 1, 1)
        val dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val checkedDate = CheckedDate(
            dateString = dateString,
            year = year,
            month = month,
            day = day
        )
        // Act
        database.checkedDateDao().insert(checkedDate)
        database.checkedDateDao().delete(checkedDate)
        val flow = database.checkedDateDao().findAll()
        val elements = flow.take(1).toList()
        val list = elements[0]
        // Assert
        assertThat(list.isEmpty(), `is`(true))
    }


}