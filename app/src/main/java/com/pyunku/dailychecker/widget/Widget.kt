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
import com.pyunku.dailychecker.calendar.data.local.CheckedDate
import com.pyunku.dailychecker.calendar.presentation.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class Widget: GlanceAppWidget() {

    @Inject lateinit var viewModel: WidgetViewModel

    private val coroutineScope = MainScope()

    private var calendar by mutableStateOf<List<CheckedDate>>(emptyList())

    @Composable
    override fun Content() {
        CalendarWidget()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarWidget(){
        Button(
            text = calendar.first().dateString,
            onClick = actionStartActivity<MainActivity>()
//            actionRunCallback<LogActionCallback>(
//                parameters = actionParametersOf(
//                    actionWidgetKey to "log event"
//                )
//            )
        )
    }

    fun loadCalendar(){
        coroutineScope.launch {
            calendar = viewModel.loadCheckedDate().first()
        }
    }
}

class LogActionCallback : ActionCallback{
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        Log.d("test","Item with id $glanceId and params $parameters clicked.")
    }

}

val actionWidgetKey = ActionParameters.Key<String>("action-widget-key")