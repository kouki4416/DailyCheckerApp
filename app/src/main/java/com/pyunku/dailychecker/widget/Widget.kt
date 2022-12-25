package com.pyunku.dailychecker.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.common.data.CheckShape
import com.pyunku.dailychecker.common.data.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class Widget @Inject constructor(
    @ApplicationContext private val context: Context,
    private val viewModel: WidgetViewModel,
    private val userPreferencesRepository: UserPreferencesRepository,
) : GlanceAppWidget() {

    private val coroutineScope = MainScope()

    private var calendar by mutableStateOf<List<CheckedDate>>(emptyList())

    private var isTodayChecked by mutableStateOf<Boolean>(false)

    private var today: CheckedDate? by mutableStateOf(CheckedDate())

    private var checkShape: CheckShape by mutableStateOf(CheckShape.CIRCLE)

    @Composable
    override fun Content() {
        CalendarWidget()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarWidget() {
        val date = LocalDate.now()
        Box(modifier = GlanceModifier
            .size(155.dp)
            .padding(16.dp)
            .background(ColorProvider(MaterialTheme.colorScheme.background))
            .cornerRadius(21.dp)
        ) {
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    Text(
                        text = "Today",
                        style = TextStyle(
                            color = ColorProvider(MaterialTheme.colorScheme.primary),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                }
                Row(modifier = GlanceModifier.fillMaxWidth()
                    .clickable(actionRunCallback<ClickActionCallback>())
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = TextStyle(
                            color = ColorProvider(Color.Black),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    if (today != null) {
                        Image(
                            modifier = GlanceModifier.fillMaxSize()
                                .padding(
                                    top = 11.dp,
                                    start = 24.dp,
                                    end = 8.dp,
                                    bottom = 2.dp
                                )
                            ,
                            provider = ImageProvider(checkShape.resId),
                            contentDescription = ""
                        )
                    }
                }
//                Row(modifier = GlanceModifier.fillMaxWidth()) {
//                    Image(
//                        provider = ImageProvider(R.drawable.ic_launcher_foreground),
//                        contentDescription = "",
//                        modifier = GlanceModifier.size(32.dp)
//                            .clickable(actionStartActivity<MainActivity>())
//                    )
//                }
            }
        }
    }

    fun loadData() {
        coroutineScope.launch {
            calendar = viewModel.loadCheckedDate().first()

            findToday(calendar)
            updateAll(context)
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        coroutineScope.cancel()
    }

    private fun findToday(list: List<CheckedDate>) {
        today = list.find {
            val todayString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            it.dateString == todayString
        }
        isTodayChecked = (today != null)
    }

    inner class ClickActionCallback : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters,
        ) {
            today?.let {
                viewModel.toggleCheckedDate(it)
            }
        }
    }

}

class LogActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        Log.d("test", "Item with id $glanceId and params $parameters clicked.")
    }
}

val actionWidgetKey = ActionParameters.Key<String>("action-widget-key")

