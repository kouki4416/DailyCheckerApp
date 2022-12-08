package com.pyunku.dailychecker.domain.interactor

import com.pyunku.dailychecker.widget.Widget
import javax.inject.Inject

class GlanceInteractorImpl @Inject constructor(
    private val widget: Widget
): GlanceInteractor {

    override suspend fun onCalendarUpdated() {
        widget.loadData()
    }
}