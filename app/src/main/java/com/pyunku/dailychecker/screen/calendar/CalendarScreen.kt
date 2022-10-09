package com.pyunku.dailychecker.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.ui.theme.DailyCheckerTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun CalendarScreen() {
    SelectableCalendar(
        modifier = Modifier
            .testTag(stringResource(id = R.string.calendar))
            .animateContentSize(),
        dayContent = { dayState ->  MyDay(dayState = dayState)},
        calendarState = rememberSelectableCalendarState(
            initialSelectionMode = SelectionMode.Multiple
        )
    )
}

@Composable
fun MyDay(dayState: DayState<DynamicSelectionState>){
    ExampleBox(
        shape = RoundedCornerShape(2.dp),
        dayState
    )
}

@Composable
fun ExampleBox(
    shape: Shape,
    dayState: DayState<DynamicSelectionState>
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
        .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(shape)
                .border(BorderStroke(1.dp, Color.LightGray))
                .clickable {
                    dayState.selectionState.onDateSelected(dayState.date)
                }
        ){
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                )
                if(dayState.selectionState.isDateSelected(dayState.date)){
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_circle),
                        contentDescription = stringResource(R.string.CheckedDate)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DailyCheckerTheme {
        CalendarScreen()
    }
}