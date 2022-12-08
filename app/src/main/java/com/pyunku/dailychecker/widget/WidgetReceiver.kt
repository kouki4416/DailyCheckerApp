package com.pyunku.dailychecker.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.pyunku.dailychecker.calendar.data.OfflineFirstCheckedDateRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetReceiver : GlanceAppWidgetReceiver(){
    @Inject lateinit var widget: Widget
    @Inject lateinit var viewModel: WidgetViewModel
    override val glanceAppWidget: GlanceAppWidget
        get() = widget.apply { loadData() }



    @Inject
    lateinit var offlineFirstCheckedDateRepository: OfflineFirstCheckedDateRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}