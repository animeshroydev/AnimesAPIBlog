package com.animesh.roy.animesapiblog.repository.main

import com.animesh.roy.animesapiblog.di.main.MainScope
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.ui.main.account.state.AccountViewState
import com.animesh.roy.animesapiblog.util.DataState
import com.animesh.roy.animesapiblog.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>
}