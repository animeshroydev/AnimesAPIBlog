package com.animesh.roy.animesapiblog.ui

import com.animesh.roy.animesapiblog.util.Response
import com.animesh.roy.animesapiblog.util.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean
}