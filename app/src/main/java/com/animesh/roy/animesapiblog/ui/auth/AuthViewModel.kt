package com.animesh.roy.animesapiblog.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.animesh.roy.animesapiblog.api.auth.network_responses.LoginResponse
import com.animesh.roy.animesapiblog.api.auth.network_responses.RegistrationResponse
import com.animesh.roy.animesapiblog.repository.auth.AuthRepository
import com.animesh.roy.animesapiblog.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): ViewModel() {

    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>> {
        return authRepository.testLoginRequest(
            "mitchelltabian1234212@gmail.com",
            "codingwithmitch111"
        )
    }

    fun testRegister(): LiveData<GenericApiResponse<RegistrationResponse>> {
        return authRepository.testRegistrationRequest(
            "mitchelltabian1234212@gmail.com",
            "mitchelltabian1234212",
            "codingwithmitch111",
            "codingwithmitch111"
        )
    }

}