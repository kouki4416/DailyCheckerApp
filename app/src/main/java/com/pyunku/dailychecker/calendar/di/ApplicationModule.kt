package com.pyunku.dailychecker.calendar.di

import android.content.Context
import androidx.room.Room
import com.pyunku.dailychecker.calendar.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule{
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase) = db.checkedDateDao()
}


