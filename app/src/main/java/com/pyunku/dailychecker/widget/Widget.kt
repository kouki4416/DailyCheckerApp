package com.pyunku.dailychecker.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class Widget @Inject constructor(
    @ApplicationContext val context: Context,
    val viewModel: WidgetViewModel,
) : GlanceAppWidget() {

    private val coroutineScope = MainScope()

    private var calendar by mutableStateOf<List<CheckedDate>>(emptyList())

    private var isTodayChecked by mutableStateOf<Boolean>(false)

    @Composable
    override fun Content() {
        CalendarWidget()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarWidget() {
        Button(
            text = isTodayChecked.toString(),
            onClick = actionStartActivity<MainActivity>()
//            actionRunCallback<LogActionCallback>(
//                parameters = actionParametersOf(
//                    actionWidgetKey to "log event"
//                )
//            )
        )
    }

    fun loadData() {
        coroutineScope.launch {
            calendar = viewModel.loadCheckedDate().first()
            isTodayChecked = isTodayChecked(calendar)
            updateAll(context)
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        coroutineScope.cancel()
    }

    private fun isTodayChecked(list: List<CheckedDate>): Boolean {
        return list.find {
            val todayString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            it.dateString == todayString
        } != null
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