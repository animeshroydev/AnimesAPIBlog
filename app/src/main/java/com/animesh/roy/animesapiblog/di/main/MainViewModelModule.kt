package com.animesh.roy.animesapiblog.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animesh.roy.animesapiblog.di.main.keys.MainViewModelKey
import com.animesh.roy.animesapiblog.ui.main.account.AccountViewModel
import com.animesh.roy.animesapiblog.ui.main.blog.viewmodel.BlogViewModel
import com.animesh.roy.animesapiblog.ui.main.create_blog.CreateBlogViewModel
import com.animesh.roy.animesapiblog.viewmodels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accoutViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel
}








