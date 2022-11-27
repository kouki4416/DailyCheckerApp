package com.pyunku.dailychecker.di

import com.pyunku.dailychecker.calendar.data.CheckedDateRepository
import com.pyunku.dailychecker.calendar.data.OfflineFirstCheckedDateRepository
import com.pyunku.dailychecker.calendar.data.local.CheckedDateDataSource
import com.pyunku.dailychecker.calendar.data.local.CheckedDateLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    // Bind is for 1-1 mapping of interface and impl
    // Since interface cannot use inject constructor, use this method
    @Binds
    abstract fun bindCheckedDateRepository(offlineFirstCheckedDateRepository: OfflineFirstCheckedDateRepository): CheckedDateRepository

    @Binds
    abstract fun bindCheckedDateDataSource(checkedDateLocalDataSource: CheckedDateLocalDataSource): CheckedDateDataSource
}