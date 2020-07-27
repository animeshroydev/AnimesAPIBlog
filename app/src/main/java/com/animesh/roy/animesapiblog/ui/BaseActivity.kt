package com.animesh.roy.animesapiblog.ui

import com.animesh.roy.animesapiblog.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager


}