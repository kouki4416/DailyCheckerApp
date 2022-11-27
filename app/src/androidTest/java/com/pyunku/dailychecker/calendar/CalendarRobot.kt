package com.pyunku.dailychecker.calendar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.calendar.presentation.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
fun launchCalendarScreen(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: CalendarRobot.() -> Unit
): CalendarRobot {
    return CalendarRobot(rule).apply(block)
}

class CalendarRobot @OptIn(ExperimentalMaterial3Api::class) constructor(private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    @OptIn(ExperimentalMaterial3Api::class)
    infix fun verify(block: CalendarVerification.() -> Unit): CalendarVerification{
        return CalendarVerification(rule).apply(block)
    }
}

class CalendarVerification @OptIn(ExperimentalMaterial3Api::class) constructor(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
){
    @OptIn(ExperimentalMaterial3Api::class)
    fun calendarPresent(){
        val calendar = rule.activity.getString(R.string.calendar)
        rule.onNodeWithTag(calendar)
            .assertIsDisplayed()
    }
}