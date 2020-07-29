package com.animesh.roy.animesapiblog.di.auth

import androidx.lifecycle.ViewModel
import com.animesh.roy.animesapiblog.di.ViewModelKey
import com.animesh.roy.animesapiblog.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}