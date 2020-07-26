package com.animesh.roy.animesapiblog.ui.auth

import androidx.lifecycle.ViewModel
import com.animesh.roy.animesapiblog.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): ViewModel() {

}