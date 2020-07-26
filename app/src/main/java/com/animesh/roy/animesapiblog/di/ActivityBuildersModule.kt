package com.animesh.roy.animesapiblog.di

import com.animesh.roy.animesapiblog.di.auth.AuthFragmentBuildersModule
import com.animesh.roy.animesapiblog.di.auth.AuthModule
import com.animesh.roy.animesapiblog.di.auth.AuthScope
import com.animesh.roy.animesapiblog.di.auth.AuthViewModelModule
import com.animesh.roy.animesapiblog.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}