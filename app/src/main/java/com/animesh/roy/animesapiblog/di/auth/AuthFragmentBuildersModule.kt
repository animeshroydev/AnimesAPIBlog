package com.animesh.roy.animesapiblog.di.auth

import com.animesh.roy.animesapiblog.ui.auth.ForgotPasswordFragment
import com.animesh.roy.animesapiblog.ui.auth.LauncherFragment
import com.animesh.roy.animesapiblog.ui.auth.LoginFragment
import com.animesh.roy.animesapiblog.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}