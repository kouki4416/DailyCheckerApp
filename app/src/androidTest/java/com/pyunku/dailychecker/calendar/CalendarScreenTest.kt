package com.pyunku.dailychecker.calendar

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.pyunku.dailychecker.calendar.presentation.MainActivity
import org.junit.Rule
import org.junit.Test

class CalendarScreenTest {

    @get:Rule
    val calendarTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displayCalendar(){
        launchCalendarScreen(calendarTestRule){

        } verify {
            calendarPresent()
        }
    }
}