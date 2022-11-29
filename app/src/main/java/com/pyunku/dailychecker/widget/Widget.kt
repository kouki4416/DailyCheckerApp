package com.pyunku.dailychecker.widget

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.text.Text

class Widget: GlanceAppWidget() {
    @Composable
    override fun Content() {
        Text(text = "Hello World")
    }
}