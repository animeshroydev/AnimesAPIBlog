package com.animesh.roy.animesapiblog.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.animesh.roy.animesapiblog.api.auth.network_responses.LoginResponse
import com.animesh.roy.animesapiblog.api.auth.network_responses.RegistrationResponse
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.repository.auth.AuthRepository
import com.animesh.roy.animesapiblog.ui.BaseViewModel
import com.animesh.roy.animesapiblog.ui.DataState
import com.animesh.roy.animesapiblog.ui.auth.state.AuthStateEvent
import com.animesh.roy.animesapiblog.ui.auth.state.AuthViewState
import com.animesh.roy.animesapiblog.ui.auth.state.LoginFields
import com.animesh.roy.animesapiblog.ui.auth.state.RegistrationFields
import com.animesh.roy.animesapiblog.util.AbsentLiveData
import com.animesh.roy.animesapiblog.util.GenericApiResponse
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent, AuthViewState>() {


    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent) {

            is AuthStateEvent.LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is AuthStateEvent.RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()

        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()

        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()

        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun cancelActiveJobs() {
        authRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}