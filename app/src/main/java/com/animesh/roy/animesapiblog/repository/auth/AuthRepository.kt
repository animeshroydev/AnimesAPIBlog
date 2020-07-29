package com.animesh.roy.animesapiblog.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.animesh.roy.animesapiblog.api.auth.OpenApiAuthService
import com.animesh.roy.animesapiblog.api.auth.network_responses.LoginResponse
import com.animesh.roy.animesapiblog.api.auth.network_responses.RegistrationResponse
import com.animesh.roy.animesapiblog.models.AccountProperties
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.persistence.AccountPropertiesDao
import com.animesh.roy.animesapiblog.persistence.AuthTokenDao
import com.animesh.roy.animesapiblog.repository.NetworkBoundResource
import com.animesh.roy.animesapiblog.session.SessionManager
import com.animesh.roy.animesapiblog.ui.DataState
import com.animesh.roy.animesapiblog.ui.Response
import com.animesh.roy.animesapiblog.ui.ResponseType
import com.animesh.roy.animesapiblog.ui.auth.state.AuthViewState
import com.animesh.roy.animesapiblog.ui.auth.state.LoginFields
import com.animesh.roy.animesapiblog.ui.auth.state.RegistrationFields
import com.animesh.roy.animesapiblog.util.AbsentLiveData
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.animesh.roy.animesapiblog.util.GenericApiResponse
import com.animesh.roy.animesapiblog.util.PreferenceKeys
import com.animesh.roy.animesapiblog.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import javax.inject.Inject

@InternalCoroutinesApi
class AuthRepository
@Inject
    constructor(
        val authTokenDao: AuthTokenDao,
        val accountPropertiesDao: AccountPropertiesDao,
        val openApiAuthService: OpenApiAuthService,
        val sessionManager: SessionManager,
        val sharedPreferences: SharedPreferences,
        val sharedPrefEditor: SharedPreferences.Editor
    ) {

    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null


    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {

        val loginFieldsErrors = LoginFields(email, password).isValidForLogin()

        if (!loginFieldsErrors.equals(LoginFields.LoginError.none())) {
            return returnErrorResponse(loginFieldsErrors, ResponseType.Dialog())
        }
        return object : NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
        true) {

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<LoginResponse>) {
              Log.e(TAG, "handleApiSuccessResponse: ${response}")

                // Incorrect Login credentials counts as a 200 response from the server, so need to handle that
                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    return onErrorReturn(response.body.errorMessage, true, false)
                }

                // don't care about result. Just insert if it doesn't exists b/c foreign key relationship
                // with AuthToken table.
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                // will return -1 if fail
                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
              return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }
        }.asLiveData()
    }



    fun attemptRegistration(email: String,
                            username: String,
                            password: String,
                            confirmPassword: String):
            LiveData<DataState<AuthViewState>> {

        val registrationFieldErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()

        if (!registrationFieldErrors.equals(RegistrationFields.RegistrationError.none())) {
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet(), true){
            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<RegistrationResponse>) {
               Log.d(TAG, "handleApiSuccessResponse: ${response}")

                if (response.body.response.equals(GENERIC_AUTH_ERROR)) {
                    return onErrorReturn(response.body.errorMessage, true, false)
                }

                // don't care about result. Just insert if it doesn't exists b/c foreign key relationship
                // with AuthToken table.
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                        response.body.email,
                        ""
                    )
                )

                // will return -1 if fail
                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if (result < 0) {
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            // not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

        }.asLiveData()


    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {

        val previousAuthUserEmail: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER,
            null)
        if (previousAuthUserEmail.isNullOrBlank()) {
            Log.e(TAG, "checkPreviousAuthUser: No previously auth user found...")
            return returnNoTokenFound()
        }

        return object : NetworkBoundResource<Void, AuthViewState> (
            sessionManager.isConnectedToTheInternet(),
            false
        ) {
            override suspend fun createCacheRequestAndReturn() {
             accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->
                 Log.d(TAG, "createCacheRequestAndReturn: searching for token: ${accountProperties}")

                 accountProperties?.let {
                     if (accountProperties.pk > -1) {
                         authTokenDao.searchByPk(accountProperties.pk).let {authToken ->
                             if (authToken != null) {
                                 onCompleteJob(
                                     DataState.data(
                                         data = AuthViewState(
                                             authToken = authToken
                                         )
                                     )
                                 )
                                 return
                             }
                         }
                     }
                 }
                 Log.d(TAG, "checkPreviousAuthUser: AuthToken not found...")
                 onCompleteJob(
                     DataState.data(
                         data = null,
                         response = Response(
                             RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                             ResponseType.None()
                         )
                     )
                 )
             }
            }

            // not used
            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<Void>) {

            }


            // not used in this case
            override fun createCall(): LiveData<GenericApiResponse<Void>> {
             return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
             repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.data(
                    data = null,
                    response = Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None())
                )
            }
        }
    }

    private fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPrefEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPrefEditor.apply()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType):
            LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs() {
        Log.d(TAG, "AuthRepository: Cancelling on-going jobs...")
        repositoryJob?.cancel()
    }

}