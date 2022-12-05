package com.pyunku.dailychecker.domain.interactor

/**
 * Interface to interact with the glance feature.
 */
interface GlanceInteractor {

    /**
     * Function called when the calendar update that should reflect in the
     * glance/widget.
     */
    suspend fun onCalendarUpdated()
}