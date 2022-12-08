package com.pyunku.dailychecker.di

import com.pyunku.dailychecker.domain.interactor.GlanceInteractor
import com.pyunku.dailychecker.domain.interactor.GlanceInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule{
    @Binds
    abstract fun glanceInteractor(glanceInteractorImpl: GlanceInteractorImpl): GlanceInteractor
}