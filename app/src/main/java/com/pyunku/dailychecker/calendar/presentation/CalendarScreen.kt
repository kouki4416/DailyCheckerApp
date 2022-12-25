package com.pyunku.dailychecker.calendar.presentation

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.common.data.CheckShape
import com.pyunku.dailychecker.common.data.UserPreferences
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

    if(state.isLoading){
        LaunchedEffect(Unit){
            viewModel.getCheckedDate()

        }
    }

    CalendarScreen(
        state = state,
        userPreferences,
        onCheckDate = { date ->
            viewModel.addCheckedDate(date)
        },
        onUncheckDate = { date ->
            viewModel.deleteCheckedDate(date)
        },
        onTaskTextChanged = { task ->
            viewModel.updateCurrentTask(task)
        },
        initialTask = userPreferences.currentTask
    )

    // create in app review
    if (state.checkedDates.size == 5 && !userPreferences.shownFirstAppReview) {
        val localContext = LocalContext.current
        val reviewManager = remember {
            ReviewManagerFactory.create(localContext)
        }
        val reviewInfo = rememberReviewTask(reviewManager)
        reviewInfo?.let {
            val flow = reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
            flow.addOnCompleteListener {
                viewModel.setShownFirstAppReview()
            }
        }
    }
    if (state.checkedDates.size == 10 && !userPreferences.shownSecondAppReview) {
        val localContext = LocalContext.current
        val reviewManager = remember {
            ReviewManagerFactory.create(localContext)
        }
        val reviewInfo = rememberReviewTask(reviewManager)
        reviewInfo?.let {
            val flow = reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
            flow.addOnCompleteListener {
                viewModel.setShownSecondAppReview()
            }
        }
    }
    if (state.checkedDates.size == 15 && !userPreferences.shownThirdAppReview) {
        val localContext = LocalContext.current
        val reviewManager = remember {
            ReviewManagerFactory.create(localContext)
        }
        val reviewInfo = rememberReviewTask(reviewManager)
        reviewInfo?.let {
            val flow = reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
            flow.addOnCompleteListener {
                viewModel.setShownThirdAppReview()
            }
        }
    }
}

@Composable
fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember {
        mutableStateOf(null)
    }
    reviewManager.requestReviewFlow().addOnCompleteListener {
        if (it.isSuccessful) {
            reviewInfo = it.result
        }
    }
    return reviewInfo
}


@Composable
fun CalendarScreen(
    state: CalendarScreenState,
    userPreferences: UserPreferences,
    onCheckDate: (date: LocalDate) -> Unit,
    onUncheckDate: (date: LocalDate) -> Unit,
    initialTask: String,
    onTaskTextChanged: (task: String) -> Unit,
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
                if (dayState.date.year == LocalDate.now().year
                    && dayState.date.month == LocalDate.now().month
                    && dayState.date.dayOfMonth <= LocalDate.now().dayOfMonth
                ) {
                    DateBox(
                        RoundedCornerShape(2.dp),
                        dayState = dayState,
                        onCheckDate = onCheckDate,
                        onUncheckDate = onUncheckDate,
                        userPreferences.checkShape
                    )
                } else {
                    UnselectableDateBox(
                        shape = RoundedCornerShape(2.dp),
                        dayState = dayState,
                        checkShape = userPreferences.checkShape
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
                    checkShape = userPreferences.checkShape,
                    onTaskTextChanged = onTaskTextChanged,
                    initialTask = initialTask
                )
            },
            weekHeader = { list ->
                WeekHeader(daysOfWeek = list)
            }
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
        .padding(2.dp)
    ) {
        // Define clickable modifier depending on date
        var clickableModifier = Modifier
            .size(70.dp)
            .clip(shape)
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
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
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
fun UnselectableDateBox(
    shape: Shape,
    dayState: DayState<DynamicSelectionState>,
    checkShape: CheckShape,
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
                .disableClickAndRipple()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                if (dayState.selectionState.isDateSelected(dayState.date)) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = checkShape.resId),
                        contentDescription = stringResource(R.string.CheckedDate),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondaryContainer)
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
    initialTask: String,
    onTaskTextChanged: (task: String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    )
    {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                ),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                modifier = Modifier
                    .testTag("Decrement")
                    .weight(0.1f)
                   ,
                onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
            ) {
                Image(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    contentDescription = "Previous",
                )
            }

            Row(
                modifier = Modifier.weight(0.8f),
                horizontalArrangement = Arrangement.Center
            ){

                Text(
                    modifier = Modifier.testTag("MonthLabel"),
                    text = monthState.currentMonth.month
                        .getDisplayName(TextStyle.FULL, Locale.getDefault())
                        .lowercase()
                        .replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = monthState.currentMonth.year.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            // Show next button only if the month on the screen is not current month
            Box(modifier = Modifier.weight(0.1f)){
                if (monthState.currentMonth != YearMonth.now()) {
                    IconButton(
                        modifier = Modifier
                            .testTag("Increment")
                        ,
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

        CheckedCounter(
            monthState = monthState,
            modifier = modifier
                .padding(
                    bottom = 12.dp
                ),
            size = checkedDateNum,
            checkShape = checkShape,
            onTaskTextChanged = onTaskTextChanged,
            initialTask = initialTask
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckedCounter(
    monthState: MonthState,
    modifier: Modifier = Modifier,
    size: Int,
    checkShape: CheckShape,
    initialTask: String,
    onTaskTextChanged: (task: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var text by remember { mutableStateOf(initialTask) }
            val focusManager = LocalFocusManager.current
            TextField(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(
                        start = 8.dp,
                        bottom = 8.dp,
                    ),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                maxLines = 1,
                value = text,
                placeholder = {
                    Text(text = stringResource(R.string.title_example))
                },
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_edit),
                        contentDescription = "edit",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline)
                    )
                },
                onValueChange = { text = it },
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onTaskTextChanged(text)
                    },
                    onNext = { focusManager.clearFocus() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),

            )
            Image(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(36.dp)
                    .weight(0.10f),
                painter = painterResource(id = checkShape.resId),
                contentDescription = "checked count",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
            Text(
                modifier = Modifier
                    .weight(0.20f)
                    .padding(start = 2.dp, end = 4.dp),
                text = "$size/${monthState.currentMonth.lengthOfMonth()}",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
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

@Preview
@Composable
fun PreviewCalendar() {
    CalendarScreen(
        CalendarScreenState(checkedDates = listOf(), isLoading = false),
        UserPreferences(),
        onCheckDate = {},
        onUncheckDate = {},
        onTaskTextChanged = {},
        initialTask = ""
    )
}

