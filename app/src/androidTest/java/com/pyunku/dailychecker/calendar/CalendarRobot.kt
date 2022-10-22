package com.pyunku.dailychecker.calendar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.pyunku.dailychecker.calendar.presentation.MainActivity
import com.pyunku.dailychecker.R

fun launchCalendarScreen(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: CalendarRobot.() -> Unit
): CalendarRobot {
    return CalendarRobot(rule).apply(block)
}

class CalendarRobot(private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    infix fun verify(block: CalendarVerification.() -> Unit): CalendarVerification{
        return CalendarVerification(rule).apply(block)
    }
}

class CalendarVerification(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
){
    fun calendarPresent(){
        val calendar = rule.activity.getString(R.string.calendar)
        rule.onNodeWithTag(calendar)
            .assertIsDisplayed()
    }
}