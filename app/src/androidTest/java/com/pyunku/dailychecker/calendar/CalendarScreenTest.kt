package com.pyunku.dailychecker.calendar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.pyunku.dailychecker.calendar.presentation.MainActivity
import org.junit.Rule
import org.junit.Test

class CalendarScreenTest {

    @OptIn(ExperimentalMaterial3Api::class)
    @get:Rule
    val calendarTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun displayCalendar(){
        launchCalendarScreen(calendarTestRule){

        } verify {
            calendarPresent()
        }
    }
}