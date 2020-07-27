package com.animesh.roy.animesapiblog.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.animesh.roy.animesapiblog.api.auth.OpenApiAuthService
import com.animesh.roy.animesapiblog.api.auth.network_responses.LoginResponse
import com.animesh.roy.animesapiblog.api.auth.network_responses.RegistrationResponse
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.persistence.AccountPropertiesDao
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao
import com.animesh.roy.animesapiblog.session.SessionManager
import com.animesh.roy.animesapiblog.ui.DataState
import com.animesh.roy.animesapiblog.ui.Response
import com.animesh.roy.animesapiblog.ui.ResponseType
import com.animesh.roy.animesapiblog.ui.auth.state.AuthViewState
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.ERROR_UNKNOWN
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


    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        return openApiAuthService.login(email, password)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when(response) {
                            is GenericApiResponse.ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )
                            }

                            is GenericApiResponse.ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                            is GenericApiResponse.ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }



    fun attemptRegistration(email: String,
                            username: String,
                            password: String,
                            confirmPassword: String): LiveData<DataState<AuthViewState>> {
        return openApiAuthService.register(email, username, password, confirmPassword)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when(response) {
                            is GenericApiResponse.ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )
                            }

                            is GenericApiResponse.ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }

                            is GenericApiResponse.ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

}