package com.pyunku.dailychecker.calendar.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.data.CheckShape
import com.pyunku.dailychecker.data.UserPreferences
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
fun CalendarRoute(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val userPreferences by viewModel.userPreferencesState.collectAsState()
    CalendarScreen(
        state = state,
        userPreferences,
        onCheckDate = { date ->
            viewModel.addCheckedDate(date)
        },
        onUncheckDate = { date ->
            viewModel.deleteCheckedDate(date)
        }
    )
}

@Composable
fun CalendarScreen(
    state: CalendarScreenState,
    userPreferences: UserPreferences,
    onCheckDate: (date: LocalDate) -> Unit,
    onUncheckDate: (date: LocalDate) -> Unit,
) {
    if (!state.isLoading) {
        val calendarState = rememberSelectableCalendarState(
            initialSelection = state.checkedDates,
            initialSelectionMode = SelectionMode.Multiple
        )
        SelectableCalendar(
            modifier = Modifier
                .testTag(stringResource(id = R.string.calendar))
                .animateContentSize(),
            dayContent = { dayState ->
                if (dayState.date.year == LocalDate.now().year && dayState.date.month == LocalDate.now().month) {
                    DateBox(
                        RoundedCornerShape(2.dp),
                        dayState = dayState,
                        onCheckDate = onCheckDate,
                        onUncheckDate = onUncheckDate,
                        userPreferences.checkShape
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
                    checkedDateNum = state.checkedDates
                        .filter {
                            it.month == monthState.currentMonth.month &&
                                    it.year == monthState.currentMonth.year
                        }.size,
                    checkShape = userPreferences.checkShape
                )
            },
            weekHeader = { list ->
                WeekHeader(daysOfWeek = list)
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
    checkShape: CheckShape,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
        .padding(3.dp)
    ) {
        // Define clickable modifier depending on date
        var clickableModifier = Modifier
            .size(70.dp)
            .clip(shape)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            )
        clickableModifier =
            if (dayState.date.isBefore(LocalDate.now()) || dayState.isCurrentDay) {
                clickableModifier.then(
                    Modifier.clickable {
                        dayState.selectionState.onDateSelected(dayState.date)
                        if (dayState.selectionState.isDateSelected(dayState.date)) {
                            onCheckDate(dayState.date)
                        } else {
                            onUncheckDate(dayState.date)
                        }
                    }
                )
            } else {
                clickableModifier.then(Modifier.disableClickAndRipple())
            }

        Box(
            modifier = Modifier.then(clickableModifier)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val primary = MaterialTheme.colorScheme.primary
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .drawBehind {
                            if (dayState.isCurrentDay) {
                                drawCircle(
                                    color = primary,
                                    radius = this.size.maxDimension / 2
                                )
                            }
                        },
                    text = dayState.date.dayOfMonth.toString(),
                    color = if (dayState.isCurrentDay) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        when (dayState.date.dayOfWeek) {
                            DayOfWeek.SATURDAY -> Color.Blue
                            DayOfWeek.SUNDAY -> Color.Red
                            else -> MaterialTheme.colorScheme.onBackground
                        }
                    },
                )
                if (dayState.selectionState.isDateSelected(dayState.date)) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = checkShape.resId),
                        contentDescription = stringResource(R.string.CheckedDate),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}

@Composable
fun WeekHeader(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .weight(1f)
                    .wrapContentHeight()
            )
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
                .disableClickAndRipple()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = MaterialTheme.colorScheme.outline
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
    checkShape: CheckShape,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    )
    {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.testTag("MonthLabel"),
                text = monthState.currentMonth.month
                    .getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .lowercase()
                    .replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = monthState.currentMonth.year.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        CheckedCounter(monthState = monthState,
            modifier = modifier,
            size = checkedDateNum,
            checkShape = checkShape)
    }
}

@Composable
fun CheckedCounter(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    size: Int,
    checkShape: CheckShape,
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
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
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
                painter = painterResource(id = checkShape.resId),
                contentDescription = "checked count",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        contentDescription = "Next",
                    )
                }
            }
        }
    }
}

// Disable clickable
inline fun Modifier.disableClickAndRipple(): Modifier = composed {
    clickable(
        enabled = false,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = { },
    )
}

//@Preview
//@Composable
//fun previewCalendar() {
//    CalendarScreen(
//        state = mutableStateOf(
//            CalendarScreenState(
//                listOf(),
//                0,
//                false,
//            )
//        ),
//        onCheckDate = {}
//    ) {}
//}

