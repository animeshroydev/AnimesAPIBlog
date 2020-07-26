package com.animesh.roy.animesapiblog.repository.auth

import com.animesh.roy.animesapiblog.api.auth.OpenApiAuthService
import com.animesh.roy.animesapiblog.persistence.AccountPropertiesDao
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao
import com.animesh.roy.animesapiblog.session.SessionManager
import javax.inject.Inject

class AuthRepository
@Inject
    constructor(
        val authTokenDao: AuthTokenDao,
        val accountPropertiesDao: AccountPropertiesDao,
        val openApiAuthService: OpenApiAuthService,
        val sessionManager: SessionManager
    ) {

}