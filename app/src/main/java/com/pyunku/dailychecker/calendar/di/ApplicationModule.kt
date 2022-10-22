package com.pyunku.dailychecker.app

import androidx.room.Room
import com.pyunku.dailychecker.calendar.data.local.AppDatabase
import com.pyunku.dailychecker.calendar.data.CheckedDateRepository
import com.pyunku.dailychecker.calendar.presentation.CalendarViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    // Create Dao
    factory { get<AppDatabase>().checkedDateDao() }
    viewModel {
        CalendarViewModel(get())
    }
    single { CheckedDateRepository(get()) }
}

