package com.animesh.roy.animesapiblog.ui.auth

import androidx.lifecycle.ViewModel
import com.animesh.roy.animesapiblog.repository.auth.AuthRepository

class AuthViewModel
constructor(
    val authRepository: AuthRepository
): ViewModel() {

}