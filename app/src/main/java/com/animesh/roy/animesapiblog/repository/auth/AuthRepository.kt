package com.animesh.roy.animesapiblog.repository.auth

import androidx.lifecycle.LiveData
import com.animesh.roy.animesapiblog.api.auth.OpenApiAuthService
import com.animesh.roy.animesapiblog.api.auth.network_responses.LoginResponse
import com.animesh.roy.animesapiblog.api.auth.network_responses.RegistrationResponse
import com.animesh.roy.animesapiblog.persistence.AccountPropertiesDao
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao
import com.animesh.roy.animesapiblog.session.SessionManager
import com.animesh.roy.animesapiblog.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject
    constructor(
        val authTokenDao: AuthTokenDao,
        val accountPropertiesDao: AccountPropertiesDao,
        val openApiAuthService: OpenApiAuthService,
        val sessionManager: SessionManager
    ) {

    fun testLoginRequest(email: String, password: String):
            LiveData<GenericApiResponse<LoginResponse>> {
        return openApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(email: String, username: String, password: String, confirmPassword: String):
            LiveData<GenericApiResponse<RegistrationResponse>> {
        return openApiAuthService.register(email, username, password, confirmPassword)
    }
}