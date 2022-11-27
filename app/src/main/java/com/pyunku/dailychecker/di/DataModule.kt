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
    @Binds
    abstract fun bindCheckedDateRepository(offlineFirstCheckedDateRepository: OfflineFirstCheckedDateRepository): CheckedDateRepository

    @Binds
    abstract fun bindCheckedDateDataSource(checkedDateLocalDataSource: CheckedDateLocalDataSource): CheckedDateDataSource
}