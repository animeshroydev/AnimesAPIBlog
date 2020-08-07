package com.animesh.roy.animesapiblog.di.main

import com.animesh.roy.animesapiblog.ui.main.account.AccountFragment
import com.animesh.roy.animesapiblog.ui.main.account.ChangePasswordFragment
import com.animesh.roy.animesapiblog.ui.main.account.UpdateAccountFragment
import com.animesh.roy.animesapiblog.ui.main.blog.BlogFragment
import com.animesh.roy.animesapiblog.ui.main.blog.UpdateBlogFragment
import com.animesh.roy.animesapiblog.ui.main.blog.ViewBlogFragment
import com.animesh.roy.animesapiblog.ui.main.create_blog.CreateBlogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment
}