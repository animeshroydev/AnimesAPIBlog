package com.animesh.roy.animesapiblog.session

import android.app.Application
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao

class SessionManager
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
){

}