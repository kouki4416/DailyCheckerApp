package com.pyunku.dailychecker.calendar.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyunku.dailychecker.R
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(
    state: State<CalendarScreenState>,
    onCheckDate: (date: LocalDate) -> Unit,
    onUncheckDate: (date: LocalDate) -> Unit,
) {
    if (!state.value.isLoading) {
        val calendarState = rememberSelectableCalendarState(
            initialSelection = state.value.checkedDates,
            initialSelectionMode = SelectionMode.Multiple
        )
        SelectableCalendar(
            modifier = Modifier
                .testTag(stringResource(id = R.string.calendar))
                .animateContentSize(),
            dayContent = { dayState ->
                if (dayState.date.month == LocalDate.now().month) {
                    DateBox(
                        RoundedCornerShape(2.dp),
                        dayState = dayState,
                        onCheckDate = onCheckDate,
                        onUncheckDate = onUncheckDate
                    )
                } else {
                    OtherMonthDateBox(
                        shape = RoundedCornerShape(2.dp),
                        dayState = dayState
                    )
                }
            },
            calendarState = calendarState,
            monthHeader = { monthState ->
                MonthHeader(
                    monthState = monthState,
                    checkedDateNum = state.value.checkedDates
                        .filter { it.month == monthState.currentMonth.month }.size
                )
            }
        )
    } else {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DateBox(
    shape: Shape,
    dayState: DayState<DynamicSelectionState>,
    onCheckDate: (date: LocalDate) -> Unit,
    onUncheckDate: (date: LocalDate) -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
        .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(shape)
                .border(
                    if (dayState.isCurrentDay) {
                        BorderStroke(color = Color.Green, width = 2.dp)
                    }
                    else BorderStroke(1.dp, Color.LightGray)
                )
                .clickable {
                    // Can click previous date and current date
                    if (dayState.date.isBefore(LocalDate.now()) || dayState.isCurrentDay) {
                        dayState.selectionState.onDateSelected(dayState.date)
                        if (dayState.selectionState.isDateSelected(dayState.date)) {
                            onCheckDate(dayState.date)
                        } else {
                            onUncheckDate(dayState.date)
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = getDateNumColor(dayState)
                )
                if (dayState.selectionState.isDateSelected(dayState.date)) {
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



@Composable
fun OtherMonthDateBox(
    shape: Shape,
    dayState: DayState<DynamicSelectionState>,
) {
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
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = Color.LightGray
                )
                if (dayState.selectionState.isDateSelected(dayState.date)) {
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

@Composable
fun MonthHeader(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    checkedDateNum: Int,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    )
    {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            androidx.compose.material.Text(
                modifier = Modifier.testTag("MonthLabel"),
                text = monthState.currentMonth.month
                    .getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .lowercase()
                    .replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.h4,
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material.Text(text = monthState.currentMonth.year.toString(),
                style = MaterialTheme.typography.h4)
        }

        CheckedCounter(monthState = monthState, modifier = modifier, size = checkedDateNum)
    }
}

@Composable
fun CheckedCounter(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    size: Int,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.weight(0.25f)) {
            IconButton(
                modifier = modifier
                    .testTag("Decrement")
                    .weight(0.25f)
                    .fillMaxSize(),
                onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
            ) {
                Image(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                    contentDescription = "Previous",
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight(),
                painter = painterResource(id = R.drawable.ic_check_24),
                contentDescription = "checked count",
            )
            //Text(text = "5 / ", modifier = Modifier.fillMaxSize(), fontSize = Typography.)
            val checkedRatio = size.toString() + "f"
            Text(
                text = "$size/${monthState.currentMonth.lengthOfMonth()}",
                fontSize = 24.sp
            )
        }
        // Show next button only if the month on the screen is not current month
        Row(modifier = Modifier.weight(0.25f)) {
            if (monthState.currentMonth != YearMonth.now()) {
                IconButton(
                    modifier = Modifier
                        .testTag("Increment")
                        .fillMaxSize(),
                    onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
                ) {
                    Image(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                        contentDescription = "Next",
                    )
                }
            }
        }
    }
}

private fun getDateNumColor(dayState: DayState<DynamicSelectionState>): Color {
    return when (dayState.date.dayOfWeek) {
        DayOfWeek.SATURDAY -> Color.Blue
        DayOfWeek.SUNDAY -> Color.Red
        else -> Color.Black
    }
}

@Preview
@Composable
fun previewCalendar() {
    CalendarScreen(
        state = mutableStateOf(
            CalendarScreenState(
                listOf(),
                0,
                false,
            )
        ),
        onCheckDate = {},
        onUncheckDate = {}
    )
}

