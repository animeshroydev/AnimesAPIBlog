package com.animesh.roy.animesapiblog.di

import android.app.Application
import com.animesh.roy.animesapiblog.di.auth.AuthComponent
import com.animesh.roy.animesapiblog.di.main.MainComponent
import com.animesh.roy.animesapiblog.session.SessionManager
import com.animesh.roy.animesapiblog.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent  {

    val sessionManager: SessionManager

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory

}








