package com.animesh.roy.animesapiblog.di

import androidx.lifecycle.ViewModelProvider
import com.animesh.roy.animesapiblog.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}