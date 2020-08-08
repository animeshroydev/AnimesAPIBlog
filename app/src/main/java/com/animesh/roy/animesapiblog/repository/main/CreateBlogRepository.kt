package com.animesh.roy.animesapiblog.repository.main

import com.animesh.roy.animesapiblog.di.main.MainScope
import com.animesh.roy.animesapiblog.models.AuthToken
import com.animesh.roy.animesapiblog.ui.main.create_blog.state.CreateBlogViewState
import com.animesh.roy.animesapiblog.util.DataState
import com.animesh.roy.animesapiblog.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@FlowPreview
@MainScope
interface CreateBlogRepository {

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
        stateEvent: StateEvent
    ): Flow<DataState<CreateBlogViewState>>
}