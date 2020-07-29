package com.animesh.roy.animesapiblog.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.ui.DataState
import com.animesh.roy.animesapiblog.ui.Response
import com.animesh.roy.animesapiblog.ui.ResponseType
import com.animesh.roy.animesapiblog.ui.auth.state.AuthViewState
import com.animesh.roy.animesapiblog.util.Constants.Companion.NETWORK_TIMEOUT
import com.animesh.roy.animesapiblog.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.animesh.roy.animesapiblog.util.ErrorHandling
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.animesh.roy.animesapiblog.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import com.animesh.roy.animesapiblog.util.GenericApiResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

@InternalCoroutinesApi
abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (
    isNetworkAvailable: Boolean // is there a network connection
) {

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(isLoading = true, cachedData = null))

        if (isNetworkAvailable) {
            coroutineScope.launch {
                // simulate a network delay
                delay(TESTING_NETWORK_DELAY)

                withContext(Main) {

                    // make network call
                    val apiResponse = createCall()
                    result.addSource(apiResponse) {response ->
                        result.removeSource(apiResponse)

                        coroutineScope.launch {
                            handleNetworkCall(response)
                        }

                    }
                }
            }

            GlobalScope.launch(IO) {
                delay(NETWORK_TIMEOUT)

                if (job.isCompleted) {
                    Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT.")
                    job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
                }
            }

        } else {
            onErrorReturn(UNABLE_TODO_OPERATION_WO_INTERNET, shouldUseDialog = true, shouldUseToast = false)
        }
    }

    suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {

        when(response) {
            is GenericApiResponse.ApiSuccessResponse -> {
              handleApiSuccessResponse(response)
            }

            is GenericApiResponse.ApiErrorResponse -> {
               Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage, true, false)
            }

            is GenericApiResponse.ApiEmptyResponse -> {
                Log.e(TAG, "NetworkBoundResource: Requst returned nothinh (HTTP 204)")
                onErrorReturn("HTTP 204. Returned Nothing", true, false)
            }
        }
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {
        var msg = errorMessage
        var useDialog = shouldUseDialog
        var responseType: ResponseType = ResponseType.None()

        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (ErrorHandling.isNetworkError(msg)){
            msg = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }
        if (shouldUseToast) {
            responseType = ResponseType.Toast()
        }
        if (useDialog) {
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(DataState.error(
            response = Response(
                message = msg,
                responseType = responseType
            )
        ))
    }


    private fun initNewJob(): Job {
        Log.e(TAG, "initNewJob: called...")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler {
            override fun invoke(cause: Throwable?) {
                if (job.isCancelled) {
                    Log.e(TAG, "NetworkBoundResource: Job has been cancelled.")
                    cause?.let {
                        onErrorReturn(it.message, false, true)
                    }?: onErrorReturn(ERROR_UNKNOWN, false, true)
                }
                else if (job.isCompleted) {
                    Log.e(TAG, "NetworkBoundResource: Job has been completed.")
                }
            }

        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)

}