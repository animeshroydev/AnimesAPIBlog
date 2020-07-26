package com.animesh.roy.animesapiblog.session

import android.app.Application
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
){

}