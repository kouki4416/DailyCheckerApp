package com.pyunku.dailychecker.calendar.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// medium b/c it test both code in datasource and how it integrates with DAO
@MediumTest
class CheckedDateLocalDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: CheckedDateLocalDataSource
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        // use inMemory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        localDataSource = CheckedDateLocalDataSource(
            database.checkedDateDao(),
            Dispatchers.Main
        )
    }

    @After
    fun cleanUp(){
        database.close()
    }

    @Test
    fun addCheckedDate_FirstElement_Match() = runTest{
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
        localDataSource.addCheckedDate(checkedDate)
        val resultFlow = localDataSource.getCheckedDates()
        val list = resultFlow.take(1).toList()[0]
        val firstItem = list[0]
        // Assert
        MatcherAssert.assertThat(firstItem, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(firstItem.dateString, CoreMatchers.`is`(dateString))
        MatcherAssert.assertThat(firstItem.year, CoreMatchers.`is`(year))
        MatcherAssert.assertThat(firstItem.month, CoreMatchers.`is`(month))
        MatcherAssert.assertThat(firstItem.day, CoreMatchers.`is`(day))
    }

    @Test
    fun addAndDeleteCheckedDate_List_Empty() = runTest {
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
        localDataSource.addCheckedDate(checkedDate)
        localDataSource.deleteCheckedDate(checkedDate)
        val flow = localDataSource.getCheckedDates()
        val elements = flow.take(1).toList()
        val list = elements[0]
        // Assert
        MatcherAssert.assertThat(list.isEmpty(), CoreMatchers.`is`(true))
    }

}