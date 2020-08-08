package com.animesh.roy.animesapiblog.di

import com.animesh.roy.animesapiblog.di.auth.AuthComponent
import com.animesh.roy.animesapiblog.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule