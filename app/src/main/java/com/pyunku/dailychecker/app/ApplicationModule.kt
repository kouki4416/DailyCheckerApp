package com.pyunku.dailychecker.app

import com.pyunku.dailychecker.screen.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module{
    viewModel{
        CalendarViewModel()
    }
}